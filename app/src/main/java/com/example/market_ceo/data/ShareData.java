package com.example.market_ceo.data;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ShareData {

    private static ShareData m_shared = new ShareData();

    private ShareData() {
    }

    public static ShareData shared() {
        return m_shared;
    }

    private Activity m_activity = null;
    private SharedPreferences m_preferences;


    public void init(Activity mainActivity) {
        m_activity = mainActivity;
        m_preferences = m_activity.getSharedPreferences("MUKKEBI.CEO", Context.MODE_PRIVATE);
    }

    public Activity getActivity() {
        return m_activity;
    }

    public Context getApplicationContext() {
        return m_activity.getApplicationContext();
    }

    public String getVersion() {
        try {
            return getApplicationContext().getPackageManager().getPackageInfo(getApplicationContext().getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "";
        }
    }

    public void savePfData(String key, String val) {
        SharedPreferences.Editor editor = m_preferences.edit();
        editor.putString(key, val);
        editor.apply();
        editor.commit();
    }

    public void deletePfData(String key){
        SharedPreferences.Editor editor = m_preferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public void savePfData(String key, int val) {
        SharedPreferences.Editor editor = m_preferences.edit();
        editor.putInt(key, val);
        editor.apply();
        editor.commit();
    }

    public String getPfData(String key) {
        return m_preferences.getString(key, "");
    }

    public String getPfData(String key, String defValue) {
        return m_preferences.getString(key, defValue);
    }

    public int getPfData(String key, int defValue) {
        if (m_preferences == null){
            return 9;
        }
        return m_preferences.getInt(key, defValue);
    }
}
