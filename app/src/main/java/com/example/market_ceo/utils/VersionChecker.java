package com.example.market_ceo.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class VersionChecker {

    private final String MARKET_URL = "https://play.google.com/store/apps/details?id=";

    private static VersionChecker uniqueInstance;
    private Activity m_activity;

    private VersionChecker() {}

    public static VersionChecker getInstance() {
        if (uniqueInstance == null) {
            synchronized (VersionChecker.class) {
                if (uniqueInstance == null)
                    uniqueInstance = new VersionChecker();
            }
        }
        return uniqueInstance;
    }

    public String getMarketVersion(String pkgName) {
        try {
            Document doc = Jsoup.connect(MARKET_URL + pkgName).get();
            Elements elements = doc.select(".content");

            for (Element element : elements) {
                if (element.attr("itemprop").equals("softwareVersion")) {
                    return element.text().trim();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Error e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getMarketVersion2(String pkgName) {
        StringBuilder sb = new StringBuilder();

        try {
            Reader reader = new InputStreamReader(
                    new URL(MARKET_URL + pkgName + "&hl=en")
                            .openConnection()
                            .getInputStream()
                    , "UTF-8");
            while (true) {
                int ch = reader.read();
                if (ch < 0) {
                    break;
                }
                sb.append((char) ch);
            }
        } catch (MalformedURLException ex) {
            Log.d("ERROR", ex.getMessage());
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d("ERROR", e.getMessage());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ERROR", e.getMessage());
            return null;
        }

        String parts[] = sb.toString().split("Current Version");
        parts = parts[1].split("<div><span class=\"htlgb\">");
        parts = parts[1].split("</span></div>");
        String res = parts[0].trim();
        return res;
    }

    public String getMarketVersionFast(String pkgName) {
        String data = null, ver = null;
        try {
            URL url = new URL(MARKET_URL + pkgName);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn == null)
                return null;

            conn.setConnectTimeout(5000);
            conn.setUseCaches(false);
            conn.setDoOutput(true);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                while (true) {
                    String line = reader.readLine();
                    if (line == null)
                        break;
                    data += line;
                }

                reader.close();
            }

            conn.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String startToken = "softwareVersion\">";
        String endToken = "<";
        int index = data.indexOf(startToken);

        if (index == -1) {
            ver = null;
        } else {
            ver = data.substring(index + startToken.length(), index + startToken.length() + 100);
            ver = ver.substring(0, ver.indexOf(endToken)).trim();
        }

        return ver;
    }

    public boolean checkVersion(Context context) {
        try {
            String pkgName = context.getPackageName();
            //String marketPkgName = getMarketVersion(pkgName);
            //String marketPkgName = getMarketVersionFast(pkgName);
            String marketPkgName = getMarketVersion2(pkgName);
            if (marketPkgName == null || marketPkgName.isEmpty())
                return false;
            int marketVersion = Integer.parseInt(marketPkgName.replace(".", ""));
            int appVersion = Integer.parseInt(context.getPackageManager().getPackageInfo(pkgName, 0).versionName.replace(".", ""));
            return marketVersion > appVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean checkVersion(Context context, String version) {
        try {
            String pkgName = context.getPackageName();
            String marketPkgName = version;
            if (marketPkgName == null || marketPkgName.isEmpty())
                return false;
            int marketVersion = Integer.parseInt(marketPkgName.replace(".", ""));
            int appVersion = Integer.parseInt(context.getPackageManager().getPackageInfo(pkgName, 0).versionName.replace(".", ""));
            return marketVersion > appVersion;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getVersionName(Context context) {
        try {
            String pkgName = context.getPackageName();
            return context.getPackageManager().getPackageInfo(pkgName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return "";
    }
}
