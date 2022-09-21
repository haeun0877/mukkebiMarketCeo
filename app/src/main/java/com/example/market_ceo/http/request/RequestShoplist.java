package com.example.market_ceo.http.request;

import android.content.Context;

import com.example.market_ceo.constant.Constant;
import com.example.market_ceo.data.ShopInfo;
import com.example.market_ceo.http.HttpApiRequest;
import com.example.market_ceo.http.Response;
import com.example.market_ceo.member.MemberManager;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.UrlEncodedContent;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestShoplist extends HttpApiRequest<ArrayList<ShopInfo>> {


    public RequestShoplist(Context context) {
        super(context);
    }

    @Override
    public String getMethod() {
        return "POST";
    }

    @Override
    public String getMediaType() {
        return null;
    }

    @Override
    public String getUrl() {
        return Constant.HTTP_API_BASE_URL + "cmk023_shop_list.php";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("return_type", "json");
        return params;
    }

    @Override
    public HttpContent getBody() throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("return_type", "json");
        params = MemberManager.getInstance().setAccessInfo(params);
        params = MemberManager.getInstance().setCommonParamater(params);
        return new UrlEncodedContent(params);
    }

    @Override
    public Response<ArrayList<ShopInfo>> parseResponse(Context context, HttpResponse response) throws Exception {
        ArrayList<ShopInfo> info = new ArrayList<>();

        final JSONObject json = (JSONObject) parseJSONRespone(response);
        JSONArray list = json.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject object = (JSONObject) list.get(i);
            Gson gson = new Gson();
            ShopInfo item = gson.fromJson(object.toString(), ShopInfo.class);
            info.add(item);
        }
        return Response.success(info);
    }

    @Override
    protected void saveResponseData(Response<ArrayList<ShopInfo>> response) {
        super.saveResponseData(response);
    }


}
