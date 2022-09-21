package com.example.market_ceo.http;

import android.app.Activity;
import android.util.Log;
import android.util.Pair;
import android.webkit.MimeTypeMap;

import com.example.market_ceo.constant.Define;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class Http {

    private class HttpData {
        private Pair<String, String> data;

        HttpData(String key, String value) {
            data = new Pair<>(key, value);
        }

        public String getKey() {
            return data.first;
        }

        public String getValue() {
            return data.second;
        }

        public String getEncodedValue() {
            try {
                return URLEncoder.encode(data.second, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            return data.second;
        }
    }

    private class HttpFileData {
        private Pair<String, String> data;
        private File file;

        HttpFileData(String key, File _file, String fileName) {
            data = new Pair<>(key, fileName);
            file = _file;
        }

        public String getKey() {
            return data.first;
        }

        public String getFileName() {
            return data.second;
        }

        public File getFile() {
            return file;
        }
    }

    public enum Method {
        GET("GET"), POST("POST"), PUT("PUT"), DELETE("DELETE");

        private final String value;

        private Method(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public interface HttpCallback extends Serializable {
        public void callback(JSONObject obj);
    }

    private Activity _activity;
    private Method method = Method.GET;
    private String urlStr = "";
    private Boolean useMultiPart = false;

    private HttpURLConnection connection;

    private ArrayList<HttpData> addDatas = new ArrayList<HttpData>();
    private ArrayList<HttpData> addHeaderDatas = new ArrayList<HttpData>();

    // multi-part
    private ArrayList<HttpFileData> addFileDatas = new ArrayList<HttpFileData>();

    public Http(String url, Method meth) {
        urlStr = url;
        method = meth;
        useMultiPart = false;
    }

    public Http(Activity activity, String url, Method meth, Boolean multiPart) {
        _activity = activity;
        urlStr = url;
        method = meth;
        useMultiPart = multiPart;
    }

    public void add(String key, String value) {
        String addValue = value;
        if (method == Method.GET || method == Method.DELETE) {
            addValue = addValue.replace(" ", "+");
        }

        value = value.replaceAll("'", "''");
        addDatas.add(new HttpData(key, value));
    }

    public void add(String key, File file, String fileName) {
        addFileDatas.add(new HttpFileData(key, file, fileName));
    }

    public void addHeader(String key, String value) {
        addHeaderDatas.add(new HttpData(key, value));
    }

    private void prepare() {
        try {
            if (method == Method.GET || method == Method.DELETE) {
                if (urlStr.contains("?") == false) {
                    urlStr += "?";
                }
                urlStr += getAddDataQueryString();

                URL url = new URL(urlStr);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(method.getValue());
            } else {
                if (useMultiPart == true) {
                    final String boundary = "0xKhTmLb-Android-0uNdAry-NeGAIPro";
                    final String endBoundary = "\r\n--" + boundary + "\r\n";

                    URL url = new URL(urlStr);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(method.getValue());
                    connection.setDoInput(true);
                    connection.setDoOutput(true);
                    connection.setRequestProperty("Connection", "Keep-Alive");
                    connection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    connection.setRequestProperty("Cookie", HttpClient.getCookeis());

                    DataOutputStream dos = new DataOutputStream(connection.getOutputStream());
                    dos.writeBytes("--" + boundary + "\r\n");

                    int cnt = 0;
                    for (HttpData data : addDatas) {
                        dos.writeBytes("Content-Disposition: form-data; name=\"" + data.getKey() + "\r\n\r\n");
                        dos.writeBytes(data.getEncodedValue());
                        dos.writeBytes("\r\n");

                        cnt++;

                        if (addDatas.size() != cnt || addFileDatas.size() > 0) {
                            dos.writeBytes(endBoundary);
                        }
                    }

                    cnt = 0;
                    for (HttpFileData data : addFileDatas) {
                        dos.writeBytes("Content-Disposition: form-data; name=\"" + data.getKey() + "\"; filename=\"" + data.getFileName() + "\"\r\n");
                        dos.writeBytes("Content-Type: " + getMimeType(data.getFile()) + "\r\n\r\n");

                        // write file
                        FileInputStream inputStream = new FileInputStream(data.getFile());
                        int bytesAvailable = inputStream.available();
                        int maxBufferSize = 1024;
                        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        byte[] buffer = new byte[bufferSize];

                        int bytesRead = inputStream.read(buffer, 0, bufferSize);
                        while (bytesRead > 0) {
                            dos.write(buffer, 0, bufferSize);
                            bytesAvailable = inputStream.available();
                            bufferSize = Math.min(bytesAvailable, maxBufferSize);
                            bytesRead = inputStream.read(buffer, 0, bufferSize);
                        }

                        if (addFileDatas.size() != cnt) {
                            dos.writeBytes(endBoundary);
                        }

                        inputStream.close();
                    }

                    dos.writeBytes(endBoundary);

                    dos.flush();
                    dos.close();
                } else {
                    URL url = new URL(urlStr);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod(method.getValue());
                    connection.setDoOutput(true);

                    DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                    wr.writeBytes(getAddDataQueryString());
                    wr.flush();
                    wr.close();
                }
            }

            for (HttpData data : addHeaderDatas) {
                connection.setRequestProperty(data.getKey(), data.getValue());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMimeType(File file) {

        String extension = getExtension(file.getName());

        if (extension.length() > 0)
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));

        return "application/octet-stream";
    }

    public String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    private String getAddDataQueryString() {
        String ret = "";
        int cnt = 0;
        for (HttpData data : addDatas) {
            ret += data.getKey() + "=" + data.getEncodedValue();
            cnt++;

            if (addDatas.size() != cnt) {
                ret += "&";
            }
        }

        return ret;
    }

    public void run(final HttpCallback callback) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                prepare();
                try {
                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String inputLine;
                        StringBuffer response = new StringBuffer();

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }
                        in.close();

                        final String retStr = response.toString();
                        Log.d(Define.TAG, retStr);

                        _activity.runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    callback.callback(new JSONObject(retStr));
                                } catch (JSONException | NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
