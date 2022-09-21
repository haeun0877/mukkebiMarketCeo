package com.example.market_ceo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.ExifInterface;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private final static String TAG = "Utils";


    public static boolean StringFormatEmailCheck(String email) {
        if (email.contains("@") && email.contains(".")) {
            return true;
        }
        return false;
    }

    public static boolean sendSMS(String tel, String body) {
        boolean result = false;
        if (tel != null) {
            SmsManager sms = SmsManager.getDefault();

            try {
                sms.sendTextMessage(tel, null, body, null, null);
                result = true;
            } catch (Exception e) {
                try {
                    body = sms.divideMessage(body).get(0);
                    sms.sendTextMessage(tel, null, body, null, null);
                    result = true;
                } catch (Exception e2) {

                }
            }
        }
        return result;
    }

    public static void slideDownAnimation(final View view, final boolean startvisible) {
        AnimationSet set = new AnimationSet(true);
        Animation trAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, -1,
                Animation.RELATIVE_TO_PARENT, 0);
        trAnimation.setDuration(500);
        set.addAnimation(trAnimation);
        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(500);
        set.addAnimation(anim);
        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                if (!startvisible)
                    view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                if (startvisible)
                    view.setVisibility(View.VISIBLE);
            }

        });
        view.startAnimation(set);
    }

    public static void slideUpAnimation(final View view, final boolean endgone) {
        AnimationSet set = new AnimationSet(true);
        Animation trAnimation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, -1);
        trAnimation.setDuration(500);
        set.addAnimation(trAnimation);
        Animation anim = new AlphaAnimation(1.0f, 0.0f);
        anim.setDuration(500);
        set.addAnimation(anim);
        set.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation arg0) {
                if (endgone)
                    view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {

            }

            @Override
            public void onAnimationStart(Animation arg0) {
                if (!endgone)
                    view.setVisibility(View.VISIBLE);
            }

        });
        view.startAnimation(set);
    }

    public static void playNotificationSound(Context context) {
        try {
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone rt = RingtoneManager.getRingtone(context, uri);
            rt.setStreamType(AudioManager.STREAM_NOTIFICATION);
            rt.play();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e);
        }
    }

    public static long getTimestampByDateOn(String dateon) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        try {
            return df.parse(dateon).getTime();
        } catch (ParseException e) {
        }
        return -1;
    }

    private static float calcLocationDistance(double lat1, double lng1, double lat2, double lng2) {
        float results[] = new float[1];
        Location.distanceBetween(lat1, lng1, lat2, lng2, results);
        return results[0];
    }

    public static String getDistance(double lat1, double lng1, double lat2, double lng2) {
        if ((lat1 == 0 && lng1 == 0) || (lat2 == 0 && lng2 == 0) || (lat1 == -1 && lng1 == -1) || (lat2 == -1 && lng2 == -1))
            return "??km";

        String result = "";
        int distance = getDistanceInt(lat1, lng1, lat2, lng2);
        if (distance <= 10) {
            result = "Near";
        } else if (distance <= 100) {
            result = (distance / 10 * 10) + "m";
        } else if (distance <= 1000) {
            result = (distance / 100 * 100) + "m";
        } else
            result = (distance / 1000) + "km";
        return result;
    }

    public static int getDistanceInt(double lat1, double lng1, double lat2, double lng2) {

        if ((lat1 == 0 && lng1 == 0) || (lat2 == 0 && lng2 == 0) || (lat1 == -1 && lng1 == -1) || (lat2 == -1 && lng2 == -1))
            return Integer.MAX_VALUE;

        return Math.round(calcLocationDistance(lat1, lng1, lat2, lng2));
    }


    public static boolean isLocationProviderEnabled(Context cxt, String provider) {
        boolean result = false;

        LocationManager lm = (LocationManager) cxt.getSystemService(Context.LOCATION_SERVICE);
        if (lm.getProvider(provider) != null) {
            if (lm.isProviderEnabled(provider))
                result = true;
            else
                result = false;
        }

        return result;
    }

    public static Bitmap makeCircleBitmap(Bitmap bitmap, boolean hasstroke) {
        Bitmap output;

        if (bitmap.getWidth() > bitmap.getHeight()) {
            output = Bitmap.createBitmap(bitmap.getHeight(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getWidth(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(output);

        final int color = Color.BLACK;
        final Paint paint = new Paint();

        float r = 0;

//	    BitmapFactory.Options options = new BitmapFactory.Options();
//	    options.inSampleSize = 4;
//	    Bitmap src = BitmapFactory.decodeFile("/sdcard/image.jpg", options);

        Bitmap dstBmp;
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            r = bitmap.getHeight() / 2;
            dstBmp = Bitmap.createBitmap(bitmap, bitmap.getWidth() / 2 - bitmap.getHeight() / 2, 0, bitmap.getHeight(), bitmap.getHeight());
        } else {
            r = bitmap.getWidth() / 2;
            dstBmp = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight() / 2 - bitmap.getWidth() / 2, bitmap.getWidth(), bitmap.getWidth());
        }
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawCircle(r, r, r, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(dstBmp, rect, rect, paint);

        if (hasstroke) {
            paint.setColor(Color.parseColor("#77000000"));
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(4);
            paint.setXfermode(null);
            canvas.drawCircle(r, r, r, paint);
        }

//	    if (dstBmp != null)
//	    	dstBmp.recycle();
        return output;
    }

    public static Bitmap getFastblur(Bitmap sentBitmap, int radius) {

        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

        if (radius < 1) {
            return (null);
        }

        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        int[] pix = new int[w * h];
        bitmap.getPixels(pix, 0, w, 0, 0, w, h);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                // Preserve alpha channel: ( 0xff000000 & pix[yi] )
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        bitmap.setPixels(pix, 0, w, 0, 0, w, h);

        return (bitmap);
    }

    public static String subString(String src, String start, String end) {
        int startPos;
        int endPos;

        startPos = src.indexOf(start);
        if (startPos == -1)
            return null;

        startPos += start.length();
        endPos = src.indexOf(end, startPos);
        if (endPos == -1)
            return null;

        return src.substring(startPos, endPos);
    }

    public static int getScreenWidth(Context con) {
        return con.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getDpToPix(Context con, double dp) {
        float density = 0.0f;
        density = con.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5);
    }

    public static String makePriceComma(String str) {
//		try {
//			DecimalFormat df = new DecimalFormat("#,###");
//			DecimalFormatSymbols dfs = new DecimalFormatSymbols();
//			dfs.setGroupingSeparator(',');
//			df.setDecimalFormatSymbols(dfs);
//
//			long aa = Long.parseLong(str);
//			return df.format(aa);// + "원";
//		}
//		catch(Exception e)
//		{
//			e.printStackTrace();
//		}
//		return str;

        DecimalFormat df = new DecimalFormat("#,##0");
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');
        df.setGroupingSize(3);
        df.setDecimalFormatSymbols(dfs);

        str = getNumberOnly(str);
        if (str != null && !str.isEmpty()) {
            double inputNum = Double.parseDouble(str);
            return df.format(inputNum);
        }
        return "";
    }

    /**
     * 숫자만 가져오기
     */
    public static String getNumberOnly(String str) {
        return str.replaceAll("[^0-9]", "");

        // 또는
        // return str.replaceAll("[^\\d]", "");

        // 또는
        // return str.replaceAll("\\D", "");

        // 모두 사용가능하다.
    }


    public static String makeStringWithComma(String string, boolean isphone) {
        if (string.length() == 0) {
            return "";
        }
        try {
            String result = "";
            if (!isphone) {
                int len = string.length();
                if (len <= 3)
                    ;
                else if (len <= 5) {
                    result += string.substring(0, 3) + "-" + string.substring(3);
                    return result;
                } else {
                    result += string.substring(0, 3) + "-" + string.substring(3, 5) + "-" + string.substring(5);
                    return result;
                }
            } else {
                int len = string.length();
                if (len <= 3)
                    ;
                else if (len <= 6) {
                    if (string.startsWith("02"))
                        result += string.substring(0, 2) + "-" + string.substring(2);
                    else
                        result += string.substring(0, 3) + "-" + string.substring(3);
                    return result;
                } else {
                    if (string.startsWith("02")) {
                        if (len > 9)
                            result += string.substring(0, 2) + "-" + string.substring(2, 6) + "-" + string.substring(6);
                        else
                            result += string.substring(0, 2) + "-" + string.substring(2, 5) + "-" + string.substring(5);
                    } else {
                        if (len > 10)
                            result += string.substring(0, 3) + "-" + string.substring(3, 7) + "-" + string.substring(7);
                        else
                            result += string.substring(0, 3) + "-" + string.substring(3, 6) + "-" + string.substring(6);
                    }
                    return result;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return string;
    }

    private static TelephonyManager getTelephonyManager(Context context) {
        return (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static String getDefaultCountryIso(Context context) {
        String region = getTelephonyManager(context).getSimCountryIso();
        if (TextUtils.isEmpty(region))
            region = getTelephonyManager(context).getNetworkCountryIso();
        if (TextUtils.isEmpty(region))
            region = Locale.getDefault().getCountry();
        return region != null ? region.toUpperCase(Locale.US) : "";
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getVersion(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "";
        }
    }

    public static boolean isUiThread() {
        try {
            return Thread.currentThread() == Looper.getMainLooper().getThread();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isPhoneNumber(String phoneNumber) {
        boolean return_value = false;
        if (!phoneNumber.startsWith("010") || phoneNumber.length() < 10)
            return_value = false;
        else
            return_value = true;

        return return_value;
    }

    public static String createRandomString(int minLength, int maxLength) {
        if (maxLength - minLength < 0)
            return "";

        final Random r = new Random();
        return createRandomString(r.nextInt((maxLength - minLength + 1)) + minLength);
    }

    public static String createRandomString(int length) {
        if (length <= 0) {
            return "";
        }

        final char[] numbersAndLetters = ("0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray();
        final Random r = new Random();
        final StringBuffer result = new StringBuffer();
        for (int i = 0; i < length - 1; i++)
            result.append(numbersAndLetters[r.nextInt(numbersAndLetters.length)]);
        return result.toString();
    }

    public static String createRandomNumber(int length) {
        if (length <= 0) {
            return "";
        }

        final Random r = new Random();
        final StringBuffer result = new StringBuffer();
        result.append(r.nextInt(9) + 1);
        for (int i = 0; i < length - 1; i++)
            result.append(r.nextInt(10));
        return result.toString();
    }


    public static boolean copyStream(InputStream is, OutputStream os) {
        final int bufSize = 8 * 1024;
        final byte[] buf = new byte[bufSize];
        int readBytes;
        try {
            while ((readBytes = is.read(buf, 0, bufSize)) != -1) {
                os.write(buf, 0, readBytes);
            }
            os.flush();
        } catch (Exception e) {

        }
        return true;
    }

    public static void closeSilently(Closeable... closeable) {
        try {
            if (closeable != null) {
                for (Closeable c : closeable) {
                    try {
                        c.close();
                    } catch (Exception e) {

                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public static void closeSilently(AssetFileDescriptor... closeable) {
        try {
            if (closeable != null) {
                for (AssetFileDescriptor c : closeable) {
                    try {
                        c.close();
                    } catch (Exception e) {

                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public static String formatByteCount(long bytes) {
        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        char pre = "kMGTPE".charAt(exp - 1);
        return String.format(Locale.US, "%.1f %cB", bytes / Math.pow(unit, exp), pre);
    }

    public static String getMD5Hash(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    public static String getMD5Hash(File file) {
        String hash = null;
        InputStream is = null;
        try {
            final MessageDigest digest = MessageDigest.getInstance("MD5");
            final byte[] buf = new byte[8192];
            int readBytes;

            is = new FileInputStream(file);
            while ((readBytes = is.read(buf)) > 0) {
                digest.update(buf, 0, readBytes);
            }
            hash = bytesToHexString(digest.digest());
        } catch (Exception e) {

        } finally {
            Utils.closeSilently(is);
        }
        return hash;
    }

    public static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }

    public static void runOnMainUi(Runnable action) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            action.run();
        } else {
            new Handler(Looper.getMainLooper()).post(action);
        }
    }

    public static boolean sdIsCardMounted() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return false;
        } else {
            return false;
        }
    }

    /**
     * EXIF정보를 회전각도로 변환하는 메서드
     *
     * @param exifOrientation EXIF 회전각
     * @return 실제 각도
     */
    public static int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }


    /**
     * Function to convert milliseconds time to
     **/
    public static String milliSecondsToTimer(long milliseconds) {
        String finalTimerString = "";
        String secondsString = "";
        String minutesString = "";

        // Convert total duration into time
        int hours = (int) (milliseconds / (1000 * 60 * 60));
        int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        // Add hours if there
        if (hours > 0) {
            finalTimerString = hours + ":";
        }

//		   minutesString = "" + minutes;
//		   // Prepending 0 to seconds if it is one digit
        if (seconds < 10) {
            secondsString = "0" + seconds;
        } else {
            secondsString = "" + seconds;
        }
//
//		   finalTimerString = minutesString + ":" + secondsString;

        finalTimerString = secondsString;

        // return timer string
        return finalTimerString;
    }

    /**
     * Function to get Progress percentage
     **/
    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        Double percentage = (double) 0;

        long currentSeconds = (int) (currentDuration / 1000);
        long totalSeconds = (int) (totalDuration / 1000);

        // calculating percentage
        percentage = (((double) currentSeconds) / totalSeconds) * 100;

        // return percentage
        return percentage.intValue();
    }

    /**
     * Function to change progress to timer
     **/
    public static int progressToTimer(int progress, int totalDuration) {
        int currentDuration = 0;
        totalDuration = (int) (totalDuration / 1000);
        currentDuration = (int) ((((double) progress) / 100) * totalDuration);

        // return current duration in milliseconds
        return currentDuration * 1000;
    }


    public static long getTimestampByDateDay(String dateon) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return df.parse(dateon).getTime();
        } catch (ParseException e) {
        }
        return -1;
    }

    public static long getTimestampByDateTime(String dateon) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        try {
            return df.parse(dateon).getTime();
        } catch (ParseException e) {
        }
        return -1;
    }

    public static String getTimeBefore(Context context, String dayDate) {

        return getTimeBefore(context, Calendar.getInstance().getTimeInMillis(), getTimestampByDateDay(dayDate));
    }

    public static String getTimeBeforeTime(Context context, String timeDate) {

        return getTimeBefore(context, Calendar.getInstance().getTimeInMillis(), getTimestampByDateTime(timeDate));
    }

    public static String getTimeBefore(Context context, long now, long time) {
        String result = "";
        long delay = now - time;
        if (delay < DateUtils.MINUTE_IN_MILLIS) {
            int second = (int) (delay / DateUtils.SECOND_IN_MILLIS);
            if (second <= 0)
                result = "방금 전";
            else
                result = second + "초 전";

        } else if (delay <= DateUtils.HOUR_IN_MILLIS) {
            int minute = (int) (delay / DateUtils.MINUTE_IN_MILLIS);
            result = minute + "분 전";
        } else if (delay < DateUtils.DAY_IN_MILLIS) {
            int hour = (int) (delay / DateUtils.HOUR_IN_MILLIS);
            result = hour + "시간 전";
        } else if (delay < DateUtils.DAY_IN_MILLIS * 30) {
            int day = (int) (delay / DateUtils.DAY_IN_MILLIS);
            result = day + "일 전";
        } else {
            result = DateFormat.format("yyyy-MM-dd kk:mm", time).toString();
        }
        return result;
    }

    public static String getFormatDate(String date) {
        if (date == null || date.isEmpty()) {
            return date;
        }
        String strDate = date.replaceAll("-", "");
        return String.format("%s.%s.%s", strDate.substring(0, 4), strDate.substring(4, 6), strDate.substring(6, 8));
    }


    public static void hiddenKeyboard(Context context, IBinder token) {
        if (context != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(token, 0);
        }
    }

    public static void showKeyboard(Context context, EditText et) {
        if (context != null) {
            InputMethodManager inputManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(et, 0);
        }
    }


    public static void setStatusBarColor(Activity activity, int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
        }
    }

    public static void preventClick(final View v) {
        preventClick(v, 200);
    }


    public static void preventLongClick(final View v) {
        preventClick(v, 1000);
    }

    public static void preventClick(final View v, int delay) {
        v.setEnabled(false);
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setEnabled(true);
            }
        }, delay);
    }

    /**
     * 표시할 수 있는 화면 가로크기
     */
    public static int getDisplayWidth(Context context) {
        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        return displayRectangle.width();
    }

    /**
     * 표시할 수 있는 화면 세로크기
     */
    public static int getDisplayHeight(Context context) {
        Rect displayRectangle = new Rect();
        Window window = ((Activity) context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        return displayRectangle.height();
    }

    /**
     * 날짜시간 문자열 반환
     */
    public static String getDatetimeString() {
        final Calendar c = Calendar.getInstance();
        int Year = c.get(Calendar.YEAR);
        int Month = c.get(Calendar.MONTH); // 1월(0), 2월(1), ..., 12월(11)
        int Day = c.get(Calendar.DAY_OF_MONTH);
        int DayOfWeek = c.get(Calendar.DAY_OF_WEEK); // 일요일(1), 월요일(2), ...,
        // 토요일(7)
        int Hour = c.get(Calendar.HOUR_OF_DAY); // HOUR는 12시간, HOUR_OF_DAY는 24시간

        int Minute = c.get(Calendar.MINUTE);
        int Second = c.get(Calendar.SECOND);
        int MilSec = c.get(Calendar.MILLISECOND);
        int AmPm = c.get(Calendar.AM_PM); // AM(0), PM(1)

        return String.format("%4d%02d%02d%02d%02d%02d%03d", Year, Month + 1, Day, Hour, Minute, Second, MilSec);
    }

    public static void calculateListHeight(ListView listView) {

        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null)
            return;

        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);

            mView.measure(View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(listView.getHeight(), View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
            //Log.w("HEIGHT" + i, String.valueOf(totalHeight));
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (mAdapter.getCount() - 1));

        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void calculateListGridview(GridView gridView) {

        ListAdapter mAdapter = gridView.getAdapter();
        if (mAdapter == null)
            return;

        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, gridView);

            mView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
            //Log.w("HEIGHT" + i, String.valueOf(totalHeight));
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + (gridView.getHeight() * (mAdapter.getCount() - 1));

        gridView.setLayoutParams(params);
        gridView.requestLayout();
    }

    public static String getSqlKeyword(String str) {
        return str.replaceAll("['\"/\\\\]", "");
    }


    public static boolean isNumeric(String s) {
        return s.matches("-?\\d+(\\.\\d+)?");
    }


    //전화번호 형식 검증
    public static boolean isValidCellPhoneNumber(String cellphoneNumber) {
        boolean returnValue = false;
        try {
            String regex = "^\\s*(010|011|016|017|018|019)(-|\\)|\\s)*(\\d{3,4})(-|\\s)*(\\d{4})\\s*$";

            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(cellphoneNumber);
            if (m.matches()) {
                returnValue = true;
            }

            if (returnValue && cellphoneNumber != null
                    && cellphoneNumber.length() > 0
                    && cellphoneNumber.startsWith("010")) {
                cellphoneNumber = cellphoneNumber.replace("-", "");
                if (cellphoneNumber.length() != 11) {
                    returnValue = false;
                }
            }
            return returnValue;
        } catch (Exception e) {
            return false;
        }
    }
    public static boolean isNullorEmptyString(String s) {
        if (s == null)
            return true;
        else return s.isEmpty();
    }

}
