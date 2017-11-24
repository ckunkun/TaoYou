package com.ad.taoyou.swk.home;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ad.taoyou.R;
import com.ad.taoyou.common.base.BaseActivity;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.ad.taoyou.swk.login.UserInfo;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

/**
 * Created by sunweike on 2017/8/30.
 */

public class ActivityMessageDetails extends BaseActivity {
    private static final String TAG = "ActivityMessageDetails";

    @Override
    public void initView() {
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    @Override
    public void initData() {
        getMsg();
    }

    private void getMsg() {
        String url = HttpTaskValues.API_POST_MESSAGE_DETAILS;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        params.addFormDataPart("tidingsId", getIntent().getStringExtra("id"));
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.9.6 系统消息详情" + rawJsonData);
                    try {
                        ((TextView) findViewById(R.id.tv_title)).setText(new org.json.JSONObject(jsonObject.get("data").toString()).getString("title"));
                        ((TextView) findViewById(R.id.tv_content)).setText(new org.json.JSONObject(jsonObject.get("data").toString()).getString("info"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }

    @Override
    public int getContentViewId() {
        return R.layout.layout_message_details;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }
}
