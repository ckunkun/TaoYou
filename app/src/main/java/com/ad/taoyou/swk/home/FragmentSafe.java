package com.ad.taoyou.swk.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
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
 * Created by sunweike on 2017/8/28.
 */

public class FragmentSafe extends BaseFragment {
    public static final String TAG = "FragmentSafe";


    public ImageView imgSafeLevel;
    public TextView mTvSetPwd;
    public TextView mTvTel;
    public TextView mTvSafe;

    private SafeInfo safeInfo;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_safe;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mTvSafe = (TextView) mRootView.findViewById(R.id.tv_safe);
        mTvTel = (TextView) mRootView.findViewById(R.id.tv_tel);
        mTvSetPwd = (TextView) mRootView.findViewById(R.id.tv_pwd);
        imgSafeLevel = (ImageView) mRootView.findViewById(R.id.img_safe_level);
        initView();
        initData();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_back) {
            backFragment(this, "");

        } else if (i == R.id.btn_bindphone) {//绑定手机号
            Bundle bundle = new Bundle();
            bundle.putString("tel", safeInfo.getTel());
            skipFragment(FragmentBindPhone.TAG, new FragmentBindPhone(), this, bundle);

        } else if (i == R.id.btn_setPass) {//修改密码
            skipFragment(FragmentUpdatePass.TAG, new FragmentUpdatePass(), this);

        }
    }

    @Override
    public void initView() {
        mRootView.findViewById(R.id.btn_back).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_bindphone).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_setPass).setOnClickListener(this);
    }

    @Override
    public void initData() {
        String url = HttpTaskValues.API_POST_SAFE_CENTER;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.6.1 安全中心接口" + rawJsonData);
                    safeInfo = new Gson().fromJson(jsonObject.get("data").toString(), SafeInfo.class);
                    if (Integer.valueOf(safeInfo.getSecurityLevel()) != 30) {
                        imgSafeLevel.setImageResource(R.mipmap.ic_safe_low);
                        mTvSafe.setText(getResources().getString(R.string.safe_low));
                    } else {
                        imgSafeLevel.setImageResource(R.mipmap.ic_safe_hight);
                        mTvSafe.setText(getResources().getString(R.string.safe_hight));
                    }
                    mTvSetPwd.setText(getResources().getString(Boolean.valueOf(safeInfo.getPwd()) ? R.string.text1 : R.string.update_pwd));
                    String tel = "";
                    if (!TextUtils.isEmpty(safeInfo.getTel())) {
                        tel = safeInfo.getTel().substring(0, 3) + "****" + safeInfo.getTel().substring(7, safeInfo.getTel().length());
                    }
                    mTvTel.setText(TextUtils.isEmpty(safeInfo.getTel()) ? getResources().getString(R.string.no_bind_phone) : tel);
                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        //当用户修改绑定手机时刷新绑定手机
        if (!hidden && !TextUtils.isEmpty(UserInfo.getInstance().getPhone()))
            mTvTel.setText(safeInfo.getTel().substring(0, 3) + "****" + safeInfo.getTel().substring(7, safeInfo.getTel().length()));
    }

}
