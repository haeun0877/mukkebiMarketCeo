package com.example.market_ceo.member;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.market_ceo.MainActivity;
import com.example.market_ceo.constant.Constant;
import com.example.market_ceo.constant.Define;
import com.example.market_ceo.data.DeliveryMyListItem;
import com.example.market_ceo.data.ShareData;
import com.example.market_ceo.data.ShopInfo;
import com.example.market_ceo.data.UserInfo;
import com.example.market_ceo.fcm.MyFirebaseInstanceIdService;
import com.example.market_ceo.http.HttpError;
import com.example.market_ceo.http.Request;
import com.example.market_ceo.http.Response;
import com.example.market_ceo.http.request.RequestLogin;
import com.example.market_ceo.http.request.RequestLogout;
import com.example.market_ceo.http.request.RequestMydeliveryList;
import com.example.market_ceo.http.request.RequestShoplist;
import com.example.market_ceo.http.request.RequestUserInfo;
import com.example.market_ceo.http.request.Request_Device_Logout;
import com.example.market_ceo.main.MainFragment;
import com.example.market_ceo.utils.VersionChecker;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class MemberManager {

    private static MemberManager m_instance;

    private UserInfo userinfo;
    private DeliveryMyListItem deliveryinfo;
    private ArrayList<ShopInfo> m_ShopInfoarr;
    private OnCompleteLoadShopInfoListener m_listener;

    public interface OnCompleteLoadShopInfoListener {
        void listener(boolean isOpen);
    }


    public MemberManager() {
    }

    public static MemberManager getInstance() {
        if (m_instance == null) {
            m_instance = new MemberManager();
        }
        return m_instance;
    }

    public void initInfo() {
        userinfo = null;
        if (m_ShopInfoarr != null)
            m_ShopInfoarr.clear();
        m_ShopInfoarr = null;
    }

    public void setListener(OnCompleteLoadShopInfoListener listener) {
        m_listener = listener;
    }

    public UserInfo getUserinfo() {
        return userinfo;
    }

    public void setUserinfo(UserInfo userinfo) {
        this.userinfo = userinfo;
        if (userinfo != null) {
            this.userinfo.setAccess_token(getAccessToken());
            this.userinfo.setAccess_id(getAccessId());
        }
    }

    public DeliveryMyListItem getDeliveryInfo(){
        return deliveryinfo;
    }

    public void setDeliveryinfo(DeliveryMyListItem deliveryMyListItem){
        deliveryinfo = deliveryMyListItem;
    }

    public ArrayList<ShopInfo> getShopInf() {
        return m_ShopInfoarr;
    }

    public void setShopInf(ArrayList<ShopInfo> ShopInfo) {
        this.m_ShopInfoarr = ShopInfo;
    }


    public void setAutoLogin(String strAuto, String token, String id) {
        ShareData.shared().savePfData("AUTO_LOGIN", strAuto);
        setAccessInfo(token, id);
    }

    public boolean getAutoLogin() {
        return ShareData.shared().getPfData("AUTO_LOGIN", "0").equals("1");
    }

    public String getAutoLoginID() {
        return ShareData.shared().getPfData("AUTO_ID");
    }

    public String getAutoLoginPW() {
        return ShareData.shared().getPfData("AUTO_PW");
    }

    public void setAccessToken(String token) {
        ShareData.shared().savePfData("ACCESS_TOKEN", token);
    }

    public String getAccessToken() {
        return ShareData.shared().getPfData("ACCESS_TOKEN");
    }

    public void setAccessId(String id) {
        ShareData.shared().savePfData("ACCESS_ID", id);
    }

    public String getAccessId() {
        return ShareData.shared().getPfData("ACCESS_ID");
    }



    /**
     * HTTP
     **/

    public boolean checkLogin(final Context context) {
//        RequestLoginCheck request = new RequestLoginCheck(context);
//        request.setOnHttpResponseListener(new Response.OnHttpResponseListener<JSONObject>() {
//            @Override
//            public void onHttpResponse(Request<?> request, JSONObject response) {
//
//                String strOK = response.optString("result");
//                if (strOK.equals("ok")) { //로그인 중
//                    Utils.showToast(response.optString("result_text"));
//                }
//            }
//        }).setOnHttpResponseErrorListener(new Response.OnHttpResponseErrorListener() {
//            @Override
//            public void onHttpResponseError(Request<?> request, HttpError error) {
//                Utils.showToast(error.getMessage());
//            }
//        }).send(context);

        return true;
    }

    //TODO: 아이디 로그인
    public void doLogin(final Context context, final String strAuto, final String strID, final String strPW, final ContentValues params) {
        String strOsType = "A";
        String strUUID = ShareData.shared().getPfData(Constant.PREF_UUID);
        String strToken = ShareData.shared().getPfData(Constant.PREF_TOKEN);
        RequestLogin request = new RequestLogin(context);
        request.setMb_id(strID);
        request.setMb_pw(strPW);
        request.setAuto_login(strAuto);
        request.setOs_type(strOsType);
        request.setDevice_uuid(strUUID);
        request.setFcm_token(strToken);
        request.setOnHttpResponseListener(new Response.OnHttpResponseListener<JSONObject>() {
            @Override
            public void onHttpResponse(Request<?> request, JSONObject response) {
                String strOK = response.optString("result");
                String strMessage = response.optString("result_text");
                if (strOK.equals("ok")) {
                    setAutoLogin(strAuto, response.optString("AccessToken"), response.optString("AccessID"));
                    getUserInfo(context, params);
                    //Toast.makeText(context, response.optString("result_text"), Toast.LENGTH_SHORT).show();
                } else {
                    //__kisoojo__ 2018.03.08 result=error"시 "access_error" output이 있으면(값은 무관) result_text를 alert
                    if (strOK.equals("error") && !response.optString("access_error").isEmpty()) {
                        showErrorDialog(context, strMessage);
                    } else {
                        doLogout(context, false, null);
                    }
                }
                Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();

            }
        }).setOnHttpResponseErrorListener(new Response.OnHttpResponseErrorListener() {
            @Override
            public void onHttpResponseError(Request<?> request, HttpError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).send(context);
    }

    //TODO: 핸드폰 로그인
    public void doLoginPhone(final Context context, final String strAuto, final String strPhone, final ContentValues params) {
        String strOsType = "A";
        String strUUID = ShareData.shared().getPfData(Constant.PREF_UUID);
        String strToken = ShareData.shared().getPfData(Constant.PREF_TOKEN);

//        Log.e("MainActivity", "uuid - " + strUUID);
//        Log.e("MainActivity", "token - " + strToken);

        RequestLogin request = new RequestLogin(context);
        request.setMb_hp(strPhone);
        request.setType("1");
        request.setAuto_login(strAuto);
        request.setOs_type(strOsType);
        request.setDevice_uuid(strUUID);
        request.setFcm_token(strToken);
        request.setOnHttpResponseListener(new Response.OnHttpResponseListener<JSONObject>() {
            @Override
            public void onHttpResponse(Request<?> request, JSONObject response) {

                String strOK = response.optString("result");
                String strMessage = response.optString("result_text");
                if (strOK.equals("ok")) {
                    setAutoLogin(strAuto, response.optString("AccessToken"), response.optString("AccessID"));
                    getUserInfo(context, params);
                    //Toast.makeText(context, response.optString("result_text"), Toast.LENGTH_SHORT).show();
                } else {
                    //__kisoojo__ 2018.03.08 result=error"시 "access_error" output이 있으면(값은 무관) result_text를 alert
                    if (strOK.equals("error") && !response.optString("access_error").isEmpty()) {
                        showErrorDialog(context, strMessage);
                    } else {
                        doLogout(context, false, null);
                    }
                }
                Toast.makeText(context, strMessage, Toast.LENGTH_SHORT).show();

            }
        }).setOnHttpResponseErrorListener(new Response.OnHttpResponseErrorListener() {
            @Override
            public void onHttpResponseError(Request<?> request, HttpError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).send(context);
    }

    public void getUserInfo(final Context context, final ContentValues params) {
        RequestUserInfo request = new RequestUserInfo(context);
        request.setFcm_token(ShareData.shared().getPfData(Constant.PREF_TOKEN));
        request.setUuid(ShareData.shared().getPfData(Constant.PREF_UUID));
        request.setVersion(VersionChecker.getInstance().getVersionName(context));
        request.setOnHttpResponseListener(new Response.OnHttpResponseListener<JSONObject>() {
            @Override
            public void onHttpResponse(Request<?> request, JSONObject response) {
                String result = response.optString("result");
                String result_text = response.optString("result_text");
                if (result.equals("ok")) {
                    // 사용자 정보 동기화
                    Gson gson = new Gson();
                    UserInfo info = gson.fromJson(response.toString(), UserInfo.class);
                    setUserinfo(info);
                    doShopList(context);
                    getDeliveryMainInfo(context, info);
                    Define.g_isLogin = true;
                } else {
                    //__kisoojo__ 2018.03.08 result=error"시 "access_error" output이 있으면(값은 무관) result_text를 alert
                    if (result.equals("error") && !response.optString("access_error").isEmpty()) {
                        showErrorDialog(context, result_text);
                    } else {
                        doLogout(context, true, null);
                    }
                }
            }
        }).send(context);
    }

    public void getDeliveryMainInfo(final Context context, UserInfo info){
        RequestMydeliveryList requestMydeliveryList = new RequestMydeliveryList(context);
        requestMydeliveryList.setMb_id(info.getMb_id());
        requestMydeliveryList.setMb_idx(info.getMb_idx());
        requestMydeliveryList.setOnHttpResponseListener(new Response.OnHttpResponseListener<JSONObject>() {
            @Override
            public void onHttpResponse(Request<?> request, JSONObject response) {
                String strOk = response.optString("result");
                if (strOk.equals("ok")) {
                    try {
                        Gson gson = new Gson();
                        DeliveryMyListItem deliveryMyListItem = gson.fromJson(response.toString(), DeliveryMyListItem.class);
                        setDeliveryinfo(deliveryMyListItem);
                        JSONArray object = response.getJSONArray("list");
                        for (int i = 0; i < object.length(); i++) {
                            JSONObject store = object.getJSONObject(i);
                            if(store.optString("dsl_main").equals("Y")){
                                deliveryinfo.setDc_idx(store.optString("dc_idx"));
                            }
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    //MainActivity.setFragmentRemoveAll(MainFragment.newInstance(params), 0);
                }
            }
        }).send(context);

    }

    public void setFcmToken(Context context, String accessId, String accessToken) {
        Define.m_bIsShowDialog = false;
        RequestUserInfo request = new RequestUserInfo(context);
        request.setFcm_token(MyFirebaseInstanceIdService.getTokenFromPrefs(context));
        request.setAccess_id(accessId);
        request.setAccess_token(accessToken);
        request.setOnHttpResponseListener(new Response.OnHttpResponseListener<JSONObject>() {
            @Override
            public void onHttpResponse(Request<?> request, JSONObject response) {
                String result = response.optString("result");
                String result_text = response.optString("result_text");
                if (result.equals("ok")) {
                    Log.d("setFcmToken", "Fcm token updated");
                }
                Define.m_bIsShowDialog = true;
            }
        }).setOnHttpResponseErrorListener(new Response.OnHttpResponseErrorListener() {
            @Override
            public void onHttpResponseError(Request<?> request, HttpError error) {
                Define.m_bIsShowDialog = true;
            }
        }).send(context);
    }

    public void doLogout(final Context context, final boolean isUpdate, final LogoutListener listener) {

        String strUUID = ShareData.shared().getPfData(Constant.PREF_UUID);
        doUuidLogout(context, strUUID, isUpdate, listener);
    }

    public void doExit(final Activity activity) {
        if (!getAutoLogin()) {
            RequestLogout request = new RequestLogout(activity);
            request.setOnHttpResponseListener(new Response.OnHttpResponseListener<JSONObject>() {
                @Override
                public void onHttpResponse(Request<?> request, JSONObject response) {
                    //사용자 정보 초기화
                    initInfo();
                    setAutoLogin("0", "", "");
                    Define.g_isLogin = false;
                    activity.finish();
                }
            }).send(activity);
        } else {
            initInfo();
            Define.g_isLogin = false;
            activity.overridePendingTransition(0, 0);
            activity.finish();
        }
        Define.g_strFragmentName = "";
    }


    public void doShopList(final Context context) {
        RequestShoplist request = new RequestShoplist(context);
        request.setOnHttpResponseListener(new Response.OnHttpResponseListener<ArrayList<ShopInfo>>() {
            @Override
            public void onHttpResponse(Request<?> request, ArrayList<ShopInfo> response) {
                setShopInf(response);
                if (m_listener != null) {
                    if (m_ShopInfoarr.size() > 0) {
                        int stopShopNum = 0;
                        Boolean isOpen = true;

                        for(int i=0; i<m_ShopInfoarr.size(); i++){
                            if(!m_ShopInfoarr.get(i).getSp_sale_status().equals("SALE")){
                                stopShopNum++;
                            }
                        }

                        if(stopShopNum==m_ShopInfoarr.size()){
                            isOpen = false;
                        }

                        m_listener.listener(isOpen);
                    }
                    m_listener = null;
                }
            }
        }).setOnHttpResponseErrorListener(new Response.OnHttpResponseErrorListener() {
            @Override
            public void onHttpResponseError(Request<?> request, HttpError error) {
                //todo: shop list 없을 경우 처리
                //Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).send(context);

    }

    public void doUuidLogout(final Context context , String strUuid, final boolean isUpdate, final LogoutListener listener){
        Request_Device_Logout request_device_logout= new Request_Device_Logout(context);
        request_device_logout.setDevice_uuid(strUuid);
        request_device_logout.setOs_type("A");
        request_device_logout.setOnHttpResponseListener(new Response.OnHttpResponseListener<JSONObject>() {
            @Override
            public void onHttpResponse(Request<?> request, JSONObject response) {
                String strOk = response.optString("result");
                if (strOk.equals("ok")) {

                    //사용자 정보 초기화
                    initInfo();

                    Define.g_isLogin = false;
                    if (isUpdate) {
                        setAutoLogin("0", "", "");
                        //MainActivity.removeFragmentBackStack(MainFragment.class.getSimpleName());
                        ShareData.shared().deletePfData("delivery_time");
                        Toast.makeText(context, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                    }

                    if (listener != null) {
                        listener.callback();
                    }
                } else {
                    setAutoLogin("0", "", "");
                }
            }
        }).setOnHttpResponseErrorListener(new Response.OnHttpResponseErrorListener() {
            @Override
            public void onHttpResponseError(Request<?> request, HttpError error) {

            }
        }).send(context);
    }

    //__kisoojo__ 20180307 accessId, token 추가 - 모든 api 호출에 추가 필요함.
    public Map<String, Object> setAccessInfo(Map<String, Object> params) {
        if (Define.g_isLogin) {
            UserInfo userInfo = MemberManager.getInstance().getUserinfo();
            if (!TextUtils.isEmpty(userInfo.getAccess_id()) && !TextUtils.isEmpty(userInfo.getAccess_token())) {
                params.put("AccessID", userInfo.getAccess_id());
                params.put("AccessToken", userInfo.getAccess_token());
            }
        } else {
            params.put("AccessID", getAccessId());
            params.put("AccessToken", getAccessToken());
        }
        return params;
    }

    public Map<String, Object> setCommonParamater(Map<String, Object> params) {
        // 1.0.44 -> 1
//        params.put("Android_ceo_app", "63");
//        params.put("new_version", "2"); //21.09.16 팀장님 요청으로 추가(아이폰과 버전 동일하게 하기 위해서)
        return params;
    }

    public void setAccessInfo(String token, String id) {
        setAccessToken(token);
        setAccessId(id);
    }

    public boolean checkAccessInfo(String token, String id) {
        String storedToken = getAccessToken();
        String storedId = getAccessId();
        return storedToken.equals(token) && storedId.equals(id);
    }

    public void initAccessInfo() {
        setAccessToken("");
        setAccessId("");
    }

    private void showErrorDialog(final Context context, String message) {
//        DialogUtil.getInstance().showAlertDialog((Activity) context, message, "확인",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        doLogout(context, true, null);
//                        dialog.dismiss();
//                    }
//                });

        setAutoLogin("0", "", "");
    }

    public interface LogoutListener {
        void callback();
    }

    public interface LoginListener {
        void callback();
    }

}