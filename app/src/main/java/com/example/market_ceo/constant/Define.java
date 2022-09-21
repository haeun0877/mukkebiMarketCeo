package com.example.market_ceo.constant;

import android.content.ContentValues;

import com.example.market_ceo.BuildConfig;

import java.util.LinkedList;
import java.util.Queue;

public class Define {
    public static String TAG = "[MUKKEBI.CEO]";

    public static Boolean g_isDebug = BuildConfig.DEBUG;

    public static Boolean g_isLogin = false;

    public static String g_strFragmentName = "";

    public static Boolean g_isDisableAnimations = false;

    public static Boolean g_isOrderPopup = false;

    public static Boolean g_isUnderUpdate = false;//!g_isDebug || false;

    public static Queue<ContentValues> g_PopupQue = new LinkedList<>();
    public static Boolean g_PopupQue_request_first = true; //처음 앱 접속했을때 주문건수가 있는지 확인하기 위한 변수
    public static String g_PopupQue_odIdx = "";

    public static Queue<ContentValues> g_PopupCallQue = new LinkedList<>();

    public static boolean m_bIsShowDialog = true;

    public static String[] g_categories = new String[]{
            "특가상품",
            "식재료",
            "용기",
            "박스",
            "봉투",
            "수저"
    };
}
