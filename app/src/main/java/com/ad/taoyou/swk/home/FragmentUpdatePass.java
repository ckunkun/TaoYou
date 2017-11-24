package com.ad.taoyou.swk.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.ad.taoyou.MyApplication;
import com.ad.taoyou.R;
import com.ad.taoyou.common.base.BaseFragment;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.alibaba.fastjson.JSONObject;


import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

/**
 * Created by sunweike on 2017/8/29.
 */

public class FragmentUpdatePass extends BaseFragment {
    public static final String TAG = "FragmentUpdatePass";

    public EditText etPass;
    public EditText etNewPass;
    public EditText etTruePass;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_updatepass;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        etPass=(EditText)mRootView.findViewById(R.id.et_password);
        etNewPass=(EditText)mRootView.findViewById(R.id.et_password2);
        etTruePass=(EditText)mRootView.findViewById(R.id.et_password3);
        initView();
        initDialog();
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.btn_back) {
            backFragment(this, FragmentSafe.TAG);

        } else if (i == R.id.btn_true) {
            String oldPass = etPass.getText().toString();
            String newPass = etNewPass.getText().toString();
            String newPass2 = etTruePass.getText().toString();
            if (TextUtils.isEmpty(oldPass)) {
                //旧密码
                MyApplication.showToast(getResources().getString(R.string.null_oldpass));
                return;
            }
            if (TextUtils.isEmpty(newPass)) {
                //检测密码
                MyApplication.showToast(getResources().getString(R.string.null_pass));
                return;
            }
            if (TextUtils.isEmpty(newPass2)) {
                //确认密码
                MyApplication.showToast(getResources().getString(R.string.null_pass2));
                return;
            }

            update(oldPass, newPass);

        } else if (i == R.id.btn_2) {
            myUniversalDialog.dismiss();
            backFragment(this, FragmentSafe.TAG);

        }
    }

    @Override
    public void initView() {
        mRootView.findViewById(R.id.btn_back).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_true).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    private void update(String oldPass, String newPass) {
        String url = HttpTaskValues.API_POST_SAFE_SETPWD;
        RequestParams params = new RequestParams();
        params.addFormDataPart("oldPwd", oldPass);
        params.addFormDataPart("pwd", newPass);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.6.2 修改密码接口" + rawJsonData);
                    mDilogTvMessage.setText("密码修改成功");
                    myUniversalDialog.show();
                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }
}
