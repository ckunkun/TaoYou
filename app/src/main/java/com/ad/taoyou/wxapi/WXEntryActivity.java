//package com.ad.taoyou.wxapi;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.ad.taoyou.MyApplication;
//import com.ad.taoyou.common.utils.HttpRequestCallBack;
//import com.ad.taoyou.common.utils.SharedPreferencesUtil;
//import com.ad.taoyou.common.utils.Utils;
//import com.ad.taoyou.common.values.HttpTaskValues;
//import com.ad.taoyou.swk.login.ActivityLogin;
//import com.ad.taoyou.swk.login.UserInfo;
//import com.alibaba.fastjson.JSONObject;
//import com.google.gson.Gson;
//import com.tencent.mm.opensdk.constants.ConstantsAPI;
//import com.tencent.mm.opensdk.modelbase.BaseReq;
//import com.tencent.mm.opensdk.modelbase.BaseResp;
//import com.tencent.mm.opensdk.modelmsg.SendAuth;
//import com.tencent.mm.opensdk.openapi.IWXAPI;
//import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
//import com.tencent.mm.opensdk.openapi.WXAPIFactory;
//
//import org.json.JSONException;
//
//import cn.finalteam.okhttpfinal.HttpRequest;
//import cn.finalteam.okhttpfinal.RequestParams;
//import cn.finalteam.toolsfinal.JsonFormatUtils;
//
//public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
//    public static final String TAG = "WXEntryActivity";
//
//    private IWXAPI api;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
////        setContentView(R.layout.entry);
//        // 通过WXAPIFactory工厂，获取IWXAPI的实例
////        api = WXAPIFactory.createWXAPI(this, Utils.getChannel(this, "APP_ID"), true);
//        api = WXAPIFactory.createWXAPI(this, "wx09030271900f6745", true);
//        api.handleIntent(getIntent(), this);
//        Log.i(TAG, "onCreate");
//    }
//
//    @Override
//    public void onReq(BaseReq baseReq) {
//        switch (baseReq.getType()) {
//            case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
//                break;
//            case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onResp(BaseResp resp) {
//        // TODO Auto-generated method stub
//        // ERR_OK = 0(用户同意)
//        // ERR_AUTH_DENIED = -4（用户拒绝授权）
//        // ERR_USER_CANCEL = -2（用户取消）
//        switch (resp.errCode) {
//            case BaseResp.ErrCode.ERR_OK:
//                if (resp instanceof SendAuth.Resp) {
//                    String code = ((SendAuth.Resp) resp).code;// 需要转换一下才可以
//                    //同意 授权
//                    Log.i(TAG, "微信回调");
//                    wxLogin(code);
//                }
//                WXEntryActivity.this.finish();
//                break;
//            case BaseResp.ErrCode.ERR_USER_CANCEL:
//                Toast.makeText(getApplication(), "用户取消", Toast.LENGTH_SHORT).show();
//                WXEntryActivity.this.finish();
//                break;
//            case BaseResp.ErrCode.ERR_AUTH_DENIED:
//                Toast.makeText(getApplication(), "用户拒绝授权", Toast.LENGTH_SHORT)
//                        .show();
//                WXEntryActivity.this.finish();
//                break;
//            default:
//                break;
//        }
//    }
//
//
//    private void wxLogin(String code) {
//        String url = HttpTaskValues.API_POST_LOGIN_WX;
//        RequestParams params = new RequestParams();
//        params.addFormDataPart("code", code);
//        params.addFormDataPart("gc", MyApplication.gc);
//        params.addFormDataPart("tpfCode", MyApplication.tpfCode);
//        params.addFormDataPart("device", Utils.getModel());
//        params.addFormDataPart("system", Utils.getSystem());
//        params.addFormDataPart("IMEI", Utils.getIdenty(WXEntryActivity.this));
//        params.addFormDataPart("mac", Utils.getMacAddress());
//        HttpRequest.post(url, params, new HttpRequestCallBack(WXEntryActivity.this) {
//            @Override
//            protected boolean onSuccess(JSONObject jsonObject, String msg) {
//                if (super.onSuccess(jsonObject, msg)) {
//                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "2.0.1 微信登录" + rawJsonData);
//                    try {
//                        //获取用户信息
//                        getUser(new org.json.JSONObject(jsonObject.get("data").toString()).getString("ty_ctoken"),
//                                new org.json.JSONObject(jsonObject.get("data").toString()).getString("ty_cid"),
//                                new org.json.JSONObject(jsonObject.get("data").toString()).getString("gopenid"),
//                                new org.json.JSONObject(jsonObject.get("data").toString()).getString("heartbeatInterval"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                return super.onSuccess(jsonObject, msg);
//            }
//        });
//    }
//
//    private void getUser(final String ty_ctoken, final String ty_cid, final String gopenid,final String time) {
//        String url = HttpTaskValues.API_POST_USERINFO;
//        RequestParams params = new RequestParams();
//        params.addFormDataPart("ty_ctoken", ty_ctoken);
//        params.addFormDataPart("ty_cid", ty_cid);
//        params.addFormDataPart("gopenid", gopenid);
//        HttpRequest.post(url, params, new HttpRequestCallBack(WXEntryActivity.this) {
//            @Override
//            protected boolean onSuccess(JSONObject jsonObject, String msg) {
//                if (super.onSuccess(jsonObject, msg)) {
//                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.5.1 我的用户信息接口" + rawJsonData);
////                    MyApplication.showToast(getResources().getString(R.string.login_success));
//                    //登录成功后调用户信息接口，拿到用户信息存储本地跳转首页
//                    UserInfo.instance = new Gson().fromJson(jsonObject.get("data").toString(), UserInfo.class);
//                    UserInfo.getInstance().setToken(ty_ctoken);
//                    UserInfo.getInstance().setCid(ty_cid);
//                    UserInfo.getInstance().setGopenid(gopenid);
//                    UserInfo.getInstance().setHeartbeatInterval(time);
//                    SharedPreferencesUtil.saveObj(WXEntryActivity.this, MyApplication.USER_INFO, UserInfo.getInstance());
//                    sendBroadcast(new Intent(ActivityLogin.TAG));
//
//                }
//                return super.onSuccess(jsonObject, msg);
//            }
//        });
//    }
//
//}