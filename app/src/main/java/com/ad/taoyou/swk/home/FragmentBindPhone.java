package com.ad.taoyou.swk.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ad.taoyou.MyApplication;
import com.ad.taoyou.R;
import com.ad.taoyou.common.base.BaseFragment;
import com.ad.taoyou.common.utils.AppManager;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.utils.SharedPreferencesUtil;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.ad.taoyou.swk.login.ActivityLogin;
import com.ad.taoyou.swk.login.UserInfo;
import com.alibaba.fastjson.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

/**
 * Created by sunweike on 2017/8/28.
 */

public class FragmentBindPhone extends BaseFragment {
    public static final String TAG = "FragmentBindPhone";
    private static final int HANDLER_SEND_IDENTIFY_CODE = 1;
    private static final int OLD_PHONE = 10;
    private static final int NEW_PHONE = 11;

    public TextView mBtnSend;
    public TextView mTvBindTel;
    public EditText mEtPhone;
    public EditText mEtCode;

    private String newPhone, code, resetTicket;
    private String tel, tel1;
    private int state = 11;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_bindphone;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mBtnSend = (TextView) mRootView.findViewById(R.id.btn_send);
        // 当前绑定的手机号
        mTvBindTel = (TextView) mRootView.findViewById(R.id.tv_bind_phone);
        mEtCode = (EditText) mRootView.findViewById(R.id.et_code);
        mEtPhone = (EditText) mRootView.findViewById(R.id.et_phone);
        initView();
        initData();
        initDialog();
    }

    @Override
    public void onClick(View view) {
        newPhone = mEtPhone.getText().toString();
        code = mEtCode.getText().toString();
        int i = view.getId();
        if (i == R.id.btn_back) {
            backFragment(this, FragmentSafe.TAG);

        } else if (i == R.id.btn_send) {//发送验证码

            //if (state == NEW_PHONE) {
            //    if (TextUtils.isEmpty(newPhone) || newPhone.length() < 11) {
            //        //手机号判空和位数判断
            //        MyApplication.showToast(getResources().getString(R.string.judge_phone), true);
            //        return;
            //    }
            //} else {
            //
            //    //if (newPhone.equals(tel)) {
            //    //    MyApplication.showToast(getResources().getString(R.string.judge_phone), true);
            //    //    return;
            //    //}
            //}
            if (TextUtils.isEmpty(newPhone) || newPhone.length() < 11) {
                //手机号判空和位数判断
                MyApplication.showToast(getResources().getString(R.string.judge_phone), true);
                return;
            }
            getCode();
            changeSendSMSButtonState();

        } else if (i == R.id.btn_true) {
            if (TextUtils.isEmpty(newPhone) || newPhone.length() < 11) {
                //检测手机号
                MyApplication.showToast(getResources().getString(R.string.judge_phone), true);
                return;
            }
            if (TextUtils.isEmpty(code)) {
                //检测验证码
                MyApplication.showToast(getResources().getString(R.string.null_code), true);
                return;
            }

            update();

        } else if (i == R.id.btn_2) {
            if (state == OLD_PHONE && mDilogTvMessage.getText().toString().equals("手机号码解绑成功")) {
                myUniversalDialog.dismiss();
                mTvBindTel.setText("当前绑定手机号 " + tel1);
                mEtPhone.setText("");
                mEtCode.setText("");
                mEtPhone.setHint("请输入新手机号");
                state = NEW_PHONE;

                mBtnSend.setClickable(true);
                mBtnSend.setText(mContext.getResources().getString(R.string.get_code));
                mBtnSend.setAlpha(1f);
            } else {
                myUniversalDialog.dismiss();
                backFragment(this, FragmentSafe.TAG);
            }

        }
    }

    @Override
    public void initView() {
        mRootView.findViewById(R.id.btn_back).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_send).setOnClickListener(this);
        mRootView.findViewById(R.id.btn_true).setOnClickListener(this);
    }

    @Override
    public void initData() {
        if (getArguments() != null) {
            tel = getArguments().getString("tel");
            Log.i(TAG,tel);
        }

        if (TextUtils.isEmpty(tel)) {
            mTvBindTel.setVisibility(View.GONE);
            state = NEW_PHONE;
        } else {
            //tel = getArguments().getString("tel");
            tel1 = tel.substring(0, 3) + "****" + tel.substring(7, tel.length());
            mTvBindTel.setVisibility(View.VISIBLE);
            mTvBindTel.setText("当前绑定手机号 " + tel1 + "请先解除绑定");
            mEtPhone.setHint("请输入当前手机号");
            state = OLD_PHONE;
        }
    }


    class IdentifyCodeHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_SEND_IDENTIFY_CODE:
                    if (mIdentityCodeTime > 0) {
                        mBtnSend.setText(String.valueOf(mIdentityCodeTime) + "s");
                        mIdentityCodeTime--;
                    } else {
                        mBtnSend.setClickable(true);
                        if (state == NEW_PHONE) {
                            mBtnSend.setText(mContext.getResources().getString(R.string.get_code));
                        } else {
                            mBtnSend.setText("重新获取");
                        }

                        mBtnSend.setAlpha(1f);
                    }
                    break;
            }
        }
    }


    private IdentifyCodeHandler mIdentifyCodeHandler = new IdentifyCodeHandler();
    private Timer mIdentityCodeTimer = new Timer();
    private int mIdentityCodeTime;

    private void changeSendSMSButtonState() {
        if (mBtnSend.isClickable()) {
            mIdentityCodeTime = 60;
            mBtnSend.setClickable(false);
            mBtnSend.setAlpha(0.5f);

            TimerTask task = new TimerTask() {

                @Override
                public void run() {
                    mIdentifyCodeHandler.sendEmptyMessage(HANDLER_SEND_IDENTIFY_CODE);

                    if (mIdentityCodeTime == 0) {
                        this.cancel();
                    }
                }
            };
            mIdentityCodeTimer.schedule(task, 0, 1000);
        }
    }

    private void getCode() {
        String url = state == OLD_PHONE ? HttpTaskValues.API_POST_SAFE_GETCODE : HttpTaskValues.API_POST_SAFE_GETNEWCODE;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        //params.addFormDPart("tel", tel);

        if (state == NEW_PHONE){
            params.addFormDataPart("tel", newPhone);
        }
        Log.i(TAG, params + ""+state);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
                    Log.i(TAG, rawJsonData);
                    if (jsonObject.getInteger("ret").intValue() == 0) {
                        mDilogTvMessage.setText("验证码已发送");
                        myUniversalDialog.show();
                    }
                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }

    private void update() {
        String url = state == OLD_PHONE ? HttpTaskValues.API_POST_SAFE_VERIFYCODE : HttpTaskValues.API_POST_SAFE_SETTEL;
        RequestParams params = new RequestParams();
        //params.addFormDataPart("tel", newPhone);
        params.addFormDataPart("code", code);
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        if (state == NEW_PHONE){
            params.addFormDataPart("tel", newPhone);
        }
        Log.i(TAG, params + ""+state);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
                    Log.i(TAG, rawJsonData);

                    if (state == OLD_PHONE) {
                        if (jsonObject.getInteger("ret")==0) {
                         mDilogTvMessage.setText("手机号码解绑成功");
                        myUniversalDialog.show();
                            state = NEW_PHONE;
                            mTvBindTel.setVisibility(View.VISIBLE);
                            mTvBindTel.setText("当前绑定手机号 " + tel1 + "解除绑定成功");
                            mEtPhone.setText("");
                            mEtPhone.setHint("请输入新手机号");
                            mEtPhone.requestFocus();
                            mEtCode.setText("");
                            mIdentityCodeTime = 0;
                            mBtnSend.setClickable(true);
                            //mBtnSend.setText(mContext.getResources().getString(R.string.get_code));
                            //mBtnSend.setAlpha(1f);
                        }
                    } else {
                        //保存新绑定手机号码
                        UserInfo.instance.setPhone(newPhone);
                        mDilogTvMessage.setText("手机号码绑定成功");
                        myUniversalDialog.show();
                        myUniversalDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override public void onCancel(DialogInterface dialogInterface) {
                                mContext.startActivity(new Intent(mContext, ActivityLogin.class));

                                AppManager.getAppManager().finishAllActivity();

                                SharedPreferencesUtil.remove(MyApplication.USER_INFO);
                            }
                        });

                    }

                }
                return super.onSuccess(jsonObject, msg);
            }

        });
    }
}
