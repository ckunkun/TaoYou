package com.ad.taoyou.common.utils;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ad.taoyou.MyApplication;
import com.ad.taoyou.R;
import com.ad.taoyou.common.values.ErrorValues;
import com.ad.taoyou.common.view.MyCustomDialog;
import com.ad.taoyou.swk.login.ActivityLogin;
import com.alibaba.fastjson.JSONObject;

import cn.finalteam.okhttpfinal.JsonHttpRequestCallback;

/**
 * Created by sunweike on 2017/8/28.
 */

public class HttpRequestCallBack extends JsonHttpRequestCallback {
    private static final String ERROR = "errorCode";
    protected MyCustomDialog myUniversalDialog;
    protected TextView mDilogTvMessage, mDialogTitle;
    protected Button btn_2;
    private Context mContext;

    public HttpRequestCallBack(Context context) {
        mContext = context;
        initDialog();
    }


    @Override
    protected void onSuccess(JSONObject jsonObject) {
        super.onSuccess(jsonObject);
        onSuccess(jsonObject, "");
    }


    protected boolean onSuccess(JSONObject jsonObject, String msg) {
        if (!ErrorValues.CODE_SUCCESS.equals(jsonObject.get("ret").toString())) {
            onFailure(Integer.valueOf(jsonObject.get("ret").toString()), "");
            return false;
        }
        return true;
    }

    @Override
    public void onFailure(int errorCode, String msg) {
        super.onFailure(errorCode, msg);
//        Log.e(ERROR, errorCode + "");
        LoadingDialogUtil.dismissLoadingDialog();
        if (ErrorValues.CODE_ERROR.equals(errorCode + "") || ErrorValues.CODE_SEND_OVERDUE.equals(errorCode + "")) {
            //验证码错误
            mDilogTvMessage.setText(mContext.getResources().getString(R.string.code_error));
            myUniversalDialog.show();
        } else if (ErrorValues.CODE_PHONE_ERROR.equals(errorCode + "")) {
            //手机号错误
            mDilogTvMessage.setText(mContext.getResources().getString(R.string.phone_error));
            myUniversalDialog.show();
        } else if (ErrorValues.CODE_USER_EXISTS.equals(errorCode + "")) {
            //账号已存在
            mDilogTvMessage.setText(mContext.getResources().getString(R.string.user_exit));
            myUniversalDialog.show();
        } else if (ErrorValues.CODE_PARAMETER_INVALID_TOKEN.equals(errorCode + "")) {
            //token失效
            mContext.startActivity(new Intent(mContext, ActivityLogin.class));
            AppManager.getAppManager().finishAllActivity();
            SharedPreferencesUtil.remove(MyApplication.USER_INFO);
        } else if (ErrorValues.CODE_USER_REGIST_ERROR.equals(errorCode + "") ||
                ErrorValues.CODE_TYUSER_REGIST_ERROR.equals(errorCode + "") ||
                ErrorValues.CODE_YJUSER_REGIST_ERROR.equals(errorCode + "")) {
            //注册失败
            mDilogTvMessage.setText(mContext.getResources().getString(R.string.register_fail));
            myUniversalDialog.show();
        } else if (ErrorValues.CODE_SEND_CODE_ERROR.equals(errorCode + "")) {
            //验证码发送失败
            mDilogTvMessage.setText(mContext.getResources().getString(R.string.code_fail));
            myUniversalDialog.show();
        } else if (ErrorValues.CODE_NO_USER.equals(errorCode + "")) {
            //账户不存在
            mDilogTvMessage.setText(mContext.getResources().getString(R.string.user_no_exit));
            myUniversalDialog.show();
        } else if (ErrorValues.CODE_YJUSER_LOGIN_ERROR.equals(errorCode + "")
                || ErrorValues.CODE_LOGIN_ERROR.equals(errorCode + "")) {
            //第三方平台登录失败
            mDilogTvMessage.setText("密码错误");
            myUniversalDialog.show();
        } else if (ErrorValues.CODE_PASSWORD_ERROR.equals(errorCode + "")) {
            //密码验证失败
            mDilogTvMessage.setText("密码验证失败");
            myUniversalDialog.show();
        } else if (ErrorValues.CODE_UPDATE_PASSWORD_ERROR.equals(errorCode + "")) {
            //修改密码失败
            mDilogTvMessage.setText("修改密码失败");
            myUniversalDialog.show();
        } else if (ErrorValues.CODE_NOSET_PHONE.equals(errorCode + "")) {
            //未设置手机号
            mDilogTvMessage.setText("未设置手机号");
            myUniversalDialog.show();
        } else if (ErrorValues.CODE_NOSET_PASSWORD.equals(errorCode + "")) {
            //未设置密码
            mDilogTvMessage.setText("未设置密码");
            myUniversalDialog.show();
        } else if (ErrorValues.CODE_TICKET_ERROR.equals(errorCode + "")) {
            //重置凭据错误
            mDilogTvMessage.setText("凭据错误");
            myUniversalDialog.show();
        } else {
            //其他错误
//            MyApplication.showToast(errorCode + "");
        }
    }


    protected void initDialog() {
        myUniversalDialog = new MyCustomDialog(mContext);
        View v = LayoutInflater.from(mContext)
                .inflate(R.layout.view_dialog, null);
        myUniversalDialog.setLayoutView(v);
        mDialogTitle = (TextView) v.findViewById(R.id.tv_title);
        mDilogTvMessage = (TextView) v.findViewById(R.id.tv_message);
        btn_2 = (Button) v.findViewById(R.id.btn_2);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dip2px(mContext, 80));
        params.gravity = Gravity.CENTER;
        mDilogTvMessage.setLayoutParams(params);
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myUniversalDialog.dismiss();
            }
        });
    }


}
