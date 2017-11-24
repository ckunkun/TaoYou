package com.ad.taoyou.swk.login;

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
import com.ad.taoyou.common.base.BaseActivity;
import com.ad.taoyou.common.utils.AppManager;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.utils.LoadingDialogUtil;
import com.ad.taoyou.common.utils.Utils;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.alibaba.fastjson.JSONObject;

import org.json.JSONException;

import java.util.Timer;
import java.util.TimerTask;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

/**
 * Created by sunweike on 2017/8/25.
 * 注册页面和修改密码页面相同。intent传值为REGISTER时为注册页面，FORGET时为忘记密码页面
 */

public class ActivityRegister extends BaseActivity {
    public static final String TAG = "ActivityRegister";
    private static final int HANDLER_SEND_IDENTIFY_CODE = 1;
    public static final int REGISTER = 10;//注册页面
    public static final int FORGET = 11;//忘记密码页面

    public EditText mEtPhone;
    public EditText mEtCode;
    public EditText mEtPass;
    public EditText mEtPass2;
    public TextView mBtnSend;

    private String phone, code, pass, pass2, token = "";
    private int type;

    @Override
    public int getContentViewId() {
        return R.layout.activity_register;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initView();
        initDialog();
    }

    @Override
    public void initView() {
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtCode = (EditText) findViewById(R.id.et_code);
        mEtPass = (EditText) findViewById(R.id.et_password);
        mEtPass2 = (EditText) findViewById(R.id.et_password2);
        mBtnSend = (TextView) findViewById(R.id.btn_send);
        type = getIntent().getIntExtra("type", 0);
        findViewById(R.id.btn_send).setOnClickListener(this);
        findViewById(R.id.btn_true).setOnClickListener(this);
        findViewById(R.id.btn_back).setOnClickListener(this);
    }

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        phone = mEtPhone.getText().toString();
        code = mEtCode.getText().toString();
        pass = mEtPass.getText().toString();
        pass2 = mEtPass2.getText().toString();
        int i = view.getId();
        if (i == R.id.btn_send) {//发送验证码
            if (TextUtils.isEmpty(phone) || phone.length() < 11) {
                //手机号判空和位数判断
                MyApplication.showToast(getResources().getString(R.string.judge_phone));
                return;
            }


            getCode();
            //60秒倒计时
            changeSendSMSButtonState();

        } else if (i == R.id.btn_true) {//注册
            if (TextUtils.isEmpty(phone) || phone.length() < 11) {
                //检测手机号
                MyApplication.showToast(getResources().getString(R.string.judge_phone));
                return;
            }
            if (TextUtils.isEmpty(code)) {
                //检测验证码
                MyApplication.showToast(getResources().getString(R.string.null_code));
                return;
            }

            if (TextUtils.isEmpty(pass)) {
                //检测密码
                MyApplication.showToast(getResources().getString(R.string.null_pass));
                return;
            }

            if (TextUtils.isEmpty(pass2)) {
                //确认密码
                MyApplication.showToast(getResources().getString(R.string.null_pass2));
                return;
            }

            if (!pass.equals(pass2)) {
                //检查两次输入的密码
                MyApplication.showToast(getResources().getString(R.string.pass_error));
                return;
            }
            if (type == REGISTER)
                //注册
                register();
            else
                //修改密码
                update();


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
                        mBtnSend.setText("重新获取");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mIdentityCodeTimer.cancel();
    }

    private void getCode() {
        String url = type == REGISTER ? HttpTaskValues.API_POST_GETREGTELCODE : HttpTaskValues.API_POST_GETRESETTELCODE;
        RequestParams params = new RequestParams();
        params.addFormDataPart("tel", phone);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "手机验证码获取" + rawJsonData);
                    //弹窗提示用户验证码已发送
                    mDialogTitle.setText(getResources().getString(R.string.Reminder));
                    mDilogTvMessage.setText(getResources().getString(R.string.send_code_success));
                    myUniversalDialog.show();
                    try {
                        token = new org.json.JSONObject(jsonObject.get("data").toString()).getString("ty_ctoken");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return super.onSuccess(jsonObject, msg);
            }

        });
    }

    private void register() {
        LoadingDialogUtil.showLoadingDialog(this);
        String url = HttpTaskValues.API_POST_REGBYTEL;
        RequestParams params = new RequestParams();
        params.addFormDataPart("tel", phone);
        params.addFormDataPart("code", code);
        params.addFormDataPart("pw", pass);
        params.addFormDataPart("ty_ctoken", token);
        params.addFormDataPart("gc", MyApplication.gc);
        params.addFormDataPart("device", Utils.getModel());
        params.addFormDataPart("system", Utils.getSystem());
        params.addFormDataPart("IMEI", Utils.getIdenty(mContext));
        params.addFormDataPart("mac", Utils.getMacAddress());
        params.addFormDataPart("tpfCode", MyApplication.tpfCode);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.1.2手机注册" + rawJsonData);
                    LoadingDialogUtil.dismissLoadingDialog();
//                    mDialogTitle.setText(getResources().getString(R.string.Reminder));
//                    mDilogTvMessage.setText(getResources().getString(R.string.register_success));
//                    myUniversalDialog.show();
                    MyApplication.showToast(getResources().getString(R.string.register_success));
                    AppManager.getAppManager().finishActivity();
                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }

    private void update() {
        LoadingDialogUtil.showLoadingDialog(this);
        String url = HttpTaskValues.API_POST_RESETBYTEL;
        RequestParams params = new RequestParams();
        params.addFormDataPart("tel", phone);
        params.addFormDataPart("code", code);
        params.addFormDataPart("pw", pass);
        params.addFormDataPart("ty_ctoken", token);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.1.4找回密码->手机" + rawJsonData);
//                    mDialogTitle.setText(getResources().getString(R.string.Reminder));
//                    mDilogTvMessage.setText();
//                    myUniversalDialog.show();
                    MyApplication.showToast(getResources().getString(R.string.update_success));
                    AppManager.getAppManager().finishActivity();
                }

                return super.onSuccess(jsonObject, msg);
            }
        });
    }
}
