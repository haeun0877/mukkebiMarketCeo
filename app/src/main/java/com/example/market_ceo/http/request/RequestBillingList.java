package com.example.market_ceo.http.request;

import android.content.Context;

import com.example.market_ceo.constant.Constant;
import com.example.market_ceo.http.HttpApiRequest;
import com.example.market_ceo.http.Response;
import com.example.market_ceo.item.BillingListItem;
import com.example.market_ceo.member.MemberManager;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpResponse;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestBillingList extends HttpApiRequest<ArrayList<BillingListItem>> {

    private String sp_idx;
    private String start_date;
    private String end_date;

    public RequestBillingList(Context context) {
        super(context);
    }

    public String getSp_idx() {
        return sp_idx;
    }

    public void setSp_idx(String sp_idx) {
        this.sp_idx = sp_idx;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    @Override
    public String getMethod() {
        return "GET";
    }

    @Override
    public String getMediaType() {
        return null;
    }

    @Override
    public String getUrl() {
        return Constant.HTTP_API_BASE_URL + "cmk054_billing_list.php";
    }

    @Override
    public Map<String, Object> getParameters() {
        Map<String, Object> params = new HashMap<>();
        params.put("return_type", "json");
        params.put("sp_idx", sp_idx);
        params.put("start_day", start_date);
        params.put("end_day", end_date);
        params.put("type", "market");
        params = MemberManager.getInstance().setAccessInfo(params);
        params = MemberManager.getInstance().setCommonParamater(params);
        return params;
    }

    @Override
    public HttpContent getBody() throws Exception {
        return null;
    }

    @Override
    public Response<ArrayList<BillingListItem>> parseResponse(Context context, HttpResponse response) throws Exception {
        ArrayList<BillingListItem> info = new ArrayList<>();

        final JSONObject json = (JSONObject) parseJSONRespone(response);
        JSONArray list = json.getJSONArray("list");
        for (int i = 0; i < list.length(); i++) {
            JSONObject object = (JSONObject) list.get(i);
            Gson gson = new Gson();
            BillingListItem item = gson.fromJson(object.toString(), BillingListItem.class);
            info.add(item);
        }
        return Response.success(info);
    }

    @Override
    protected void saveResponseData(Response<ArrayList<BillingListItem>> response) {
        super.saveResponseData(response);
    }
}

