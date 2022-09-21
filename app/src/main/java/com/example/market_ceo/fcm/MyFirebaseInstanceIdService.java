package com.example.market_ceo.fcm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.example.market_ceo.constant.Constant;
import com.example.market_ceo.constant.Define;
import com.example.market_ceo.member.MemberManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.e(TAG, "Refreshed token: " + refreshedToken);
        FirebaseMessaging.getInstance().subscribeToTopic("ceo");
        checkDeviceToken(refreshedToken);
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String token) {

    }

    private void checkDeviceToken(String token) {
        String token_before = getTokenFromPrefs(getApplicationContext());
        if (token == null) {
            refreshToken();
        } else if (!TextUtils.equals(token, token_before)) {
            saveTokenToPrefs(token, getApplicationContext());
            if (Define.g_isLogin) {
                MemberManager.getInstance().setFcmToken(this, getAccessId(this), getAccessToken(this));
            }
        }
    }

    private void refreshToken() {
        //refresh device token
        //MyFirebaseInstanceIdService.saveTokenToPrefs("", this);
        if (MyFirebaseInstanceIdService.getTokenFromPrefs(getApplicationContext()) == null ||
                TextUtils.isEmpty(MyFirebaseInstanceIdService.getTokenFromPrefs(getApplicationContext()))) {
            Intent intent = new Intent(this, DeleteTokenService.class);
            startService(intent);
        }
    }

    public static void saveTokenToPrefs(String token, Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MUKKEBI.CEO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Constant.PREF_TOKEN, token);
        editor.apply();
        editor.commit();
    }

    public static String getTokenFromPrefs(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MUKKEBI.CEO", Context.MODE_PRIVATE);
        return preferences.getString(Constant.PREF_TOKEN, null);
    }

    private String getAccessId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MUKKEBI.CEO", Context.MODE_PRIVATE);
        return preferences.getString("ACCESS_ID", null);
    }

    private String getAccessToken(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("MUKKEBI.CEO", Context.MODE_PRIVATE);
        return preferences.getString("ACCESS_TOKEN", null);
    }
}