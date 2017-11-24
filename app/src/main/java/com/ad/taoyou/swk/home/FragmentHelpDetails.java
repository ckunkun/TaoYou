package com.ad.taoyou.swk.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ad.taoyou.R;
import com.ad.taoyou.common.base.BaseFragment;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.ad.taoyou.swk.login.UserInfo;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

/**
 * Created by sunweike on 2017/8/29.
 */

public class FragmentHelpDetails extends BaseFragment {
    public static final String TAG = "FragmentHelpDetails";

    @Override
    public int getContentViewId() {
        return R.layout.fragment_helpdetails;
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
            backFragment(this, FragmentHelp.TAG);

        }
    }

    @Override
    public void initView() {
        mRootView.findViewById(R.id.btn_back).setOnClickListener(this);
    }

    @Override
    public void initData() {
        String url = HttpTaskValues.API_POST_HELP_DETAIL;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        params.addFormDataPart("id", getArguments().getString("id"));
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "2.1.3 帮助问题详情" + rawJsonData);
                    HelpInfo info = new Gson().fromJson(jsonObject.get("data").toString(), HelpInfo.class);
                    ((TextView)mRootView.findViewById(R.id.tv_title)).setText(info.getTitle());
                    ((TextView)mRootView.findViewById(R.id.tv_content)).setText(info.getInfo());
                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }
}
