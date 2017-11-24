package com.ad.taoyou.swk.gift;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ad.taoyou.R;
import com.ad.taoyou.common.base.BaseFragment;
import com.ad.taoyou.common.imageloder.UILUtil;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.ad.taoyou.swk.login.UserInfo;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;

import org.json.JSONException;

import java.math.BigDecimal;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

/**
 * Created by sunweike on 2017/8/30.
 */

public class FragmentGiftDetails extends BaseFragment {
    public static final String TAG = "FragmentGiftDetails";
    private int state = 0, giftId = 0;
    private GiftInfo info;


    public ImageView imgIcon;
    public TextView mTvGameTitle;
    public TextView mTvGiftTitle;
    public TextView mTvPlatform;
    public TextView mTvProgress;
    public TextView mTvTime;
    public TextView mTvContent;
    public TextView mTvUser;
    public TextView mBtnReceive;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_giftdetails;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        state = this.getArguments().getInt("state");
        giftId = this.getArguments().getInt("giftId");
        initView();
        initData();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_back) {
            backFragment(this, "");

        } else if (i == R.id.btn_to_mygift) {
            if (mBtnReceive.getText().toString().equals("已领取"))
                return;

            if (state == 1) {
                //领取礼包
                receive();

            } else {
                //查看我的礼包
                Intent intent = new Intent(FragmentGift.TAG);
                intent.putExtra("to", 1);
                getActivity().sendBroadcast(intent);
                backFragment(this, "");
            }

        }
    }

    @Override
    public void initView() {
        imgIcon = (ImageView) mRootView.findViewById(R.id.img_icon);
        mTvGameTitle = (TextView) mRootView.findViewById(R.id.tv_title);
        mTvGiftTitle = (TextView) mRootView.findViewById(R.id.tv_subtitle);
        mTvPlatform = (TextView) mRootView.findViewById(R.id.tv_platform);
        mTvProgress = (TextView) mRootView.findViewById(R.id.tv_progress);
        mTvTime = (TextView) mRootView.findViewById(R.id.tv_time);
        mTvContent = (TextView) mRootView.findViewById(R.id.tv_content);
        mTvUser = (TextView) mRootView.findViewById(R.id.tv_user);
        mBtnReceive = (TextView) mRootView.findViewById(R.id.btn_to_mygift);

        mRootView.findViewById(R.id.btn_back).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_to_mygift).setOnClickListener(this);

        if (state == 1) {
            mBtnReceive.setText("领取礼包");
        }
    }

    @Override
    public void initData() {
        String url = HttpTaskValues.API_POST_GIFT_INFO;
        RequestParams params = new RequestParams();
        params.addFormDataPart("giftId", giftId);
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.7.5 礼包信息接口" + rawJsonData);
                    info = new Gson().fromJson(jsonObject.get("data").toString(), GiftInfo.class);
                    UILUtil.getLoader(mContext).displayImage(info.getLogoPath(), imgIcon, UILUtil.normalOption());
                    mTvGameTitle.setText(info.getGameName());
                    mTvGiftTitle.setText(info.getName());
                    float progress = (Integer.valueOf(info.getTotal()) - Integer.valueOf(info.getCount())) / Integer.valueOf(info.getTotal()) * 100;
                    BigDecimal bd = new BigDecimal(progress);
                    bd = bd.setScale(1, BigDecimal.ROUND_HALF_UP);
                    String p = bd.toString();
                    if (p.substring(p.length() - 1, p.length()).equals("0")) {
                        p = p.substring(0, p.length() - 2);
                    }
                    mTvProgress.setText(Html.fromHtml("剩余<font color=#ffee2d>" + p + "%</font>"));
                    mTvTime.setText(info.getStartTime() + " - " + info.getEndTime());
                    mTvPlatform.setText("适用平台：" + info.getRunPf());
                    mTvContent.setText(info.getInfo());
                    mTvUser.setText(info.getUseInfo());
                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }

    public void receive() {
        String url = HttpTaskValues.API_POST_GIFT_RECEIVE;
        RequestParams params = new RequestParams();
        params.addFormDataPart("giftId", giftId);
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.7.4 领取礼包接口" + rawJsonData);
                    mBtnReceive.setText("已领取");
                    try {
                        skipFragment(FragmentReceiveGift.TAG,
                                new FragmentReceiveGift(new org.json.JSONObject(jsonObject.get("data").toString()).getString("giftCode"), info.getUseInfo()),
                                FragmentGiftDetails.this);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return super.onSuccess(jsonObject, msg);
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                skipFragment(FragmentReceiveGift.TAG,
                        new FragmentReceiveGift(),
                        FragmentGiftDetails.this);
            }
        });
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden && FragmentReceiveGift.isOther) {

            backFragment(this, "");
        }
    }
}
