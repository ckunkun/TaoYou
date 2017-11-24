package com.ad.taoyou.swk.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.ad.taoyou.R;
import com.ad.taoyou.common.base.BaseFragment;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.ad.taoyou.swk.login.UserInfo;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

/**
 * Created by sunweike on 2017/8/29.
 */

public class FragmentActivityDetails extends BaseFragment {
    public static final String TAG = "FragmentActivityDetails";
    private String actId;

    public TextView mTvTitle;
    public TextView mTvTime;
    public WebView mWebView;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_activitydetails;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initData();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_back) {
            backFragment(this, "");

        }
    }

    @Override
    public void initView() {
        mTvTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        mTvTime = (TextView) mRootView.findViewById(R.id.tv_time);
        mWebView = mRootView.findViewById(R.id.webview);
        mRootView.findViewById(R.id.btn_back).setOnClickListener(this);

        mWebView.setBackgroundColor(0);
        WebSettings settings = mWebView.getSettings();
        mWebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        mWebView.getSettings().setMinimumFontSize(settings.getMinimumFontSize() + 8);
        mWebView.getSettings().setAllowFileAccess(false);
        mWebView.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
        mWebView.setVerticalScrollbarOverlay(true);
        String url = this.getArguments().getString("url");
        try {
            url = new java.net.URLDecoder().decode(url, "UTF-8");
            Log.e(TAG,url);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        mWebView.loadUrl(url);

    }

    @Override
    public void initData() {
        actId = this.getArguments().getString("actId");
        String url = HttpTaskValues.API_POST_ACTIVITY_DETAIL;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        params.addFormDataPart("actId", actId);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.8.2 活动详情接口" + rawJsonData);
                    ActivityInfo info = new Gson().fromJson(jsonObject.get("data").toString(), ActivityInfo.class);
                    mTvTitle.setText(info.getName());
                    mTvTime.setText(info.getCreateTime());
                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }
}
