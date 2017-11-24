package com.ad.taoyou.swk.login;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.ad.taoyou.HomeActivity;
import com.ad.taoyou.MyApplication;
import com.ad.taoyou.R;
import com.ad.taoyou.common.alipay.AlipayUtils;
import com.ad.taoyou.common.base.BaseActivity;
import com.ad.taoyou.common.helper.TYListener;
import com.ad.taoyou.common.utils.AppManager;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.utils.LoadingDialogUtil;
import com.ad.taoyou.common.utils.SharedPreferencesUtil;
import com.ad.taoyou.common.utils.Utils;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

/**
 * Created by sunweike on 2017/8/26.
 */
public class ActivityLogin extends BaseActivity implements View.OnClickListener {
    public static final String TAG = "ActivityLogin";
    private static final int MY_PERMISSION_REQUEST_CODE = 10000;

    public EditText mEtPhone;
    public EditText mEtPass;
    SendAuth.Req req;
    private IWXAPI api;
    private String phone, pass;
    public static TYListener tyListener;

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        String appid = Utils.getChannel(mContext, "APP_ID");
//        String appid="wx09030271900f6745";
        api = WXAPIFactory.createWXAPI(mContext, appid);
        api.registerApp(appid);
        initView();
        registerBoradcastReceiver();

        /**
         * 第 1 步: 检查是否有相应的权限
         */
        boolean isAllGranted = checkPermissionAllGranted(
                new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE
                }
        );
//        MyApplication.showToast(isAllGranted + "");
        // 如果这3个权限全都拥有, 则直接执行备份代码
        if (isAllGranted) {
            return;
        }

        /**
         * 第 2 步: 请求权限
         */
        // 一次请求多个权限, 如果其他有权限是已经授予的将会自动忽略掉
        ActivityCompat.requestPermissions(
                this,
                new String[]{
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                },
                MY_PERMISSION_REQUEST_CODE
        );


    }

    /**
     * 检查是否拥有指定的所有权限
     */
    private boolean checkPermissionAllGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                // 只要有一个权限没有被授予, 则直接返回 false
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            boolean isAllGranted = true;

            // 判断是否所有的权限都已经授予了
            for (int grant : grantResults) {
                if (grant != PackageManager.PERMISSION_GRANTED) {
                    isAllGranted = false;
                    break;
                }
            }

            if (isAllGranted) {
                // 如果所有的权限都授予了, 则执行备份代码

            } else {
                // 弹出对话框告诉用户需要权限的原因, 并引导用户去应用权限管理中手动打开权限按钮
//                openAppDetails();
            }
        }
    }

    @Override
    public void initView() {
        mEtPhone = (EditText) findViewById(R.id.et_phone);
        mEtPass = (EditText) findViewById(R.id.et_password);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.btn_forget_pass).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);
        findViewById(R.id.btn_alipay).setOnClickListener(this);
        findViewById(R.id.btn_wx).setOnClickListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(TAG);
        // 注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }

    // 广播处理
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(TAG)) {
                AppManager.getAppManager().finishActivity();
                startActivity(new Intent(mContext, HomeActivity.class));
            }
        }

    };

    @Override
    public void initData() {

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent;
        phone = mEtPhone.getText().toString();
        pass = mEtPass.getText().toString();
        int i = view.getId();
        if (i == R.id.btn_register) {//注册
            intent = new Intent(mContext, ActivityRegister.class);
            intent.putExtra("type", ActivityRegister.REGISTER);
            startActivity(intent);

        } else if (i == R.id.btn_forget_pass) {//忘记密码
            intent = new Intent(mContext, ActivityRegister.class);
            intent.putExtra("type", ActivityRegister.FORGET);
            startActivity(intent);

        } else if (i == R.id.btn_login) {//登录
            if (TextUtils.isEmpty(phone)) {
                //检测账号
                MyApplication.showToast(getResources().getString(R.string.judge_phone2));
                return;
            }
            if (TextUtils.isEmpty(pass)) {
                //检测密码
                MyApplication.showToast(getResources().getString(R.string.null_pass));
                return;
            }

            login();

        } else if (i == R.id.btn_alipay) {//支付宝
            AlipayUtils alipayUtils = new AlipayUtils(this);
            alipayUtils.authV2();
        } else if (i == R.id.btn_wx) {//微信
            req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wxchat";
            boolean result = api.sendReq(req);
            if (!result) {
                MyApplication.showToast("请先安装微信");
            }
        }
    }

    private void login() {
        String url = HttpTaskValues.API_POST_LOGIN;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ac", phone);
        params.addFormDataPart("pw", pass);
        params.addFormDataPart("gc", MyApplication.gc);
        params.addFormDataPart("tpfCode", MyApplication.tpfCode);
        params.addFormDataPart("device", Utils.getModel());
        params.addFormDataPart("system", Utils.getSystem());
        params.addFormDataPart("IMEI", Utils.getIdenty(mContext));
        params.addFormDataPart("mac", Utils.getMacAddress());

        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    try {
                        String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                        Log.i(TAG, "1.1.3手机/账户 密码登录" + rawJsonData);
                        MyApplication.showToast(getResources().getString(R.string.login_success));

                        //获取用户信息
                        getUser(new org.json.JSONObject(jsonObject.get("data").toString()).getString("ty_ctoken"),
                                new org.json.JSONObject(jsonObject.get("data").toString()).getString("ty_cid"),
                                new org.json.JSONObject(jsonObject.get("data").toString()).getString("gopenid"),
                                new org.json.JSONObject(jsonObject.get("data").toString()).getString("heartbeatInterval"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return super.onSuccess(jsonObject, msg);
            }
//
//            @Override
//            public void onFailure(int errorCode, String msg) {
//                super.onFailure(errorCode, msg);
//                if (errorCode == 100101001) {
//                    MyApplication.showToast("账户或密码错误");
//                } else if (errorCode == Integer.valueOf(ErrorValues.CODE_LOGIN_ERROR)) {
//                    MyApplication.showToast("登录失败");
//                }
//            }
        });
    }

    private void getUser(final String ty_ctoken, final String ty_cid, final String gopenid, final String heart) {
        String url = HttpTaskValues.API_POST_USERINFO;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", ty_ctoken);
        params.addFormDataPart("ty_cid", ty_cid);
        params.addFormDataPart("gopenid", gopenid);
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.5.1 我的用户信息接口" + rawJsonData);
                    //登录成功后调用户信息接口，拿到用户信息存储本地跳转首页
                    UserInfo.instance = new Gson().fromJson(jsonObject.get("data").toString(), UserInfo.class);
                    UserInfo.getInstance().setToken(ty_ctoken);
                    UserInfo.getInstance().setCid(ty_cid);
                    UserInfo.getInstance().setGopenid(gopenid);
                    UserInfo.getInstance().setHeartbeatInterval(heart);
                    SharedPreferencesUtil.saveObj(mContext, MyApplication.USER_INFO, UserInfo.getInstance());
                    LoadingDialogUtil.dismissLoadingDialog();
                    AppManager.getAppManager().finishActivity();
//                    startActivity(new Intent(mContext, HomeActivity.class));
                    tyListener.onSuccess(UserInfo.getInstance());
                }
                return super.onSuccess(jsonObject, msg);
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
                tyListener.onFaild("login failed");
            }
        });
    }
}
