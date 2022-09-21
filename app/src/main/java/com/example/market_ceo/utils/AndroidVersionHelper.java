package com.example.market_ceo.utils;

import android.os.Build;

public class AndroidVersionHelper {
    public static class VersionCode {
        public static boolean isOver2_3() {
            return AndroidVersionHelper.isOverGingerbread();
        }

        public static boolean isOver2_3_3() {
            return AndroidVersionHelper.isOverGingerbreadMR1();
        }

        public static boolean isOver3_0() {
            return AndroidVersionHelper.isOverHoneyComb();
        }

        public static boolean isOver4_0() {
            return AndroidVersionHelper.isOverIcecreamSandwitch();
        }

        public static boolean isOver4_0_3() {
            return AndroidVersionHelper.isOverIcecreamSandwitchMR1();
        }

        public static boolean isOver4_1() {
            return AndroidVersionHelper.isOverJellyBean();
        }

        public static boolean isOver4_2() {
            return AndroidVersionHelper.isOverJellyBeanMR1();
        }

        public static boolean isOver4_3() {
            return AndroidVersionHelper.isOverJellyBeanMR2();
        }

        public static boolean isOver4_4() {
            return AndroidVersionHelper.isOverKitkat();
        }
    }

    public static boolean isOver(int level) {
        return Build.VERSION.SDK_INT >=  level;
    }

    public static boolean isOverFroyo() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
    }

    public static boolean isOverGingerbread() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
    }

    public static boolean isOverGingerbreadMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD_MR1;
    }

    public static boolean isOverHoneyComb() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }

    public static boolean isOverHoneyCombMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
    }

    public static boolean isOverHoneyCombMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2;
    }

    public static boolean isOverIcecreamSandwitch() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
    }

    public static boolean isOverIcecreamSandwitchMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1;
    }

    public static boolean isOverJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean isOverJellyBeanMR1() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    public static boolean isOverJellyBeanMR2() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
    }

    public static boolean isOverKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }
}
