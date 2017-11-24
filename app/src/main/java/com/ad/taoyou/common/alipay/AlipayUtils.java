package com.ad.taoyou.common.alipay;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ad.taoyou.MyApplication;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.utils.Utils;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.alipay.sdk.app.AuthTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;

public class AlipayUtils {
    private Activity mContext;
    private AuthResult authResult;

    public AlipayUtils(Activity context) {
        mContext = context;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            authResult = new AuthResult((Map<String, String>) msg.obj, true);
            String resultStatus = authResult.getResultStatus();
            // 判断resultStatus 为“9000”且result_code
            // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
            if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                // 获取alipay_open_id，调支付时作为参数extern_token 的value
                // 传入，则支付账户为该授权账户

                //上传code到服务器
//                Toast.makeText(mContext,
//                        "授权成功" + String.format("authCode:%s", authResult.getAlipayOpenId()), Toast.LENGTH_SHORT).show();
                postId(authResult.getAlipayOpenId());
            } else {
                // 其他状态值则为授权失败
                Toast.makeText(mContext,
                        "授权失败" + String.format("authCode:%s", authResult.getAuthCode()), Toast.LENGTH_SHORT).show();

            }
        }

        ;
    };

    /**
     * 支付宝账户授权业务
     */
    public void authV2() {

        String url = HttpTaskValues.API_POST_ALIPAY_GETAUTHPARAMS;
        HttpRequest.post(url, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(com.alibaba.fastjson.JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {

                    try {
                        String info = new JSONObject(jsonObject.get("data").toString()).getString("info");
                        String sign = new JSONObject(jsonObject.get("data").toString()).getString("sign");
                        //        利用签名来调起支付宝授权
                        final String authInfo;
                        authInfo = new java.net.URLDecoder().decode(info + "&sign=" + sign, "UTF-8");
                        Log.e("authInfo", authInfo);
                        Runnable authRunnable = new Runnable() {

                            @Override
                            public void run() {
                                // 构造AuthTask 对象
                                AuthTask authTask = new AuthTask(mContext);
                                // 调用授权接口，获取授权结果
                                Map<String, String> result = authTask.authV2(authInfo, true);

                                Message msg = new Message();
                                msg.obj = result;
                                mHandler.sendMessage(msg);
                            }
                        };

                        // 必须异步调用
                        Thread authThread = new Thread(authRunnable);
                        authThread.start();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                return super.onSuccess(jsonObject, msg);
            }
        });

    }

    public void postId(String auth_code) {
        String url = HttpTaskValues.API_POST_ALIPAY_LOGIN;
        RequestParams params = new RequestParams();
        params.addFormDataPart("auth_code", auth_code);
        params.addFormDataPart("gc", MyApplication.gc);
        params.addFormDataPart("device", Utils.getModel());
        params.addFormDataPart("system", Utils.getSystem());
        params.addFormDataPart("IMEI", Utils.getIdenty(mContext));
        params.addFormDataPart("mac", Utils.getMacAddress());
        HttpRequest.post(url, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(com.alibaba.fastjson.JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {

                    try {
                        String ty_ctoken = new JSONObject(jsonObject.get("data").toString()).getString("ty_ctoken");
                        String ty_cid = new JSONObject(jsonObject.get("data").toString()).getString("ty_cid");
                        String gopenid = new JSONObject(jsonObject.get("data").toString()).getString("gopenid");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }


}
