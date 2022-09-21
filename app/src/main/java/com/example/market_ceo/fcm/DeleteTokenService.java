package com.example.market_ceo.fcm;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.market_ceo.constant.Constant;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

public class DeleteTokenService extends IntentService {
    public static final String TAG = DeleteTokenService.class.getSimpleName();

    public DeleteTokenService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            // Check for current token
            String originalToken = getTokenFromPrefs();
            Log.d(TAG, "Token before deletion: " + originalToken);

            // Resets Instance ID and revokes all tokens.
            FirebaseInstanceId.getInstance().deleteInstanceId();

            // Clear current saved token
            saveTokenToPrefs("");

            // Check for success of empty token
            String tokenCheck = getTokenFromPrefs();
            Log.d(TAG, "Token deleted. Proof: " + tokenCheck);

            // Now manually call onTokenRefresh()
            Log.d(TAG, "Getting new token");
            saveTokenToPrefs(FirebaseInstanceId.getInstance().getToken());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveTokenToPrefs(String _token) {
        // Access Shared Preferences
        SharedPreferences preferences = getSharedPreferences("MUKKEBI.CEO", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Save to SharedPreferences
        editor.putString(Constant.PREF_TOKEN, _token);
        editor.apply();
    }

    private String getTokenFromPrefs() {
        SharedPreferences preferences = getSharedPreferences("MUKKEBI.CEO", Context.MODE_PRIVATE);
        return preferences.getString(Constant.PREF_TOKEN, null);
    }
}
