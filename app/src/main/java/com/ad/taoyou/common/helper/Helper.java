package com.ad.taoyou.common.helper;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.ad.taoyou.ActivityWeb;
import com.ad.taoyou.HomeActivity;
import com.ad.taoyou.MyApplication;
import com.ad.taoyou.common.utils.HttpRequestCallBack;
import com.ad.taoyou.common.utils.SignUtils;
import com.ad.taoyou.common.utils.Utils;
import com.ad.taoyou.common.values.HttpTaskValues;
import com.ad.taoyou.swk.login.ActivityLogin;
import com.ad.taoyou.swk.login.UserInfo;
import com.alibaba.fastjson.JSONObject;
import com.snowfish.cn.ganga.helper.SFOnlineLoginListener;
import com.snowfish.cn.ganga.helper.SFOnlinePayResultListener;
import com.snowfish.cn.ganga.helper.SFOnlineUser;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.JsonFormatUtils;

public class Helper {
    final String TAG = "Helper";
    private Activity mContext;
    private String order;
    public RoleInfo info;
    public int money;
    public String goodsName;
    public SFOnlineLoginListener sfOnlineLoginListener;
    public SFLoginListener sfLoginListener;
    public SFPayResultListener sfPayResultListener;
    public SFOnlinePayResultListener sfOnlinePayResultListener;

    public Helper(Activity context) {
        mContext = context;

        //易接登录回调
        sfOnlineLoginListener = new SFOnlineLoginListener() {
            @Override
            public void onLogout(Object o) {
                sfLoginListener.onLogout(o);
            }

            @Override
            public void onLoginSuccess(SFOnlineUser sfOnlineUser, Object o) {
                //淘游易接快捷登录
                quickLogin(sfOnlineUser, o);
            }

            @Override
            public void onLoginFailed(String s, Object o) {
                sfLoginListener.onLoginFailed(s, o);
            }
        };

        //易接支付回调
        sfOnlinePayResultListener = new SFOnlinePayResultListener() {
            @Override
            public void onFailed(String s) {
                sfPayResultListener.onFailed(s);
            }

            @Override
            public void onSuccess(String s) {
                quickPay(goodsName, order, money);
                sfPayResultListener.onSuccess(s);
            }

            @Override
            public void onOderNo(String s) {
                order = s;
                sfPayResultListener.onOderNo(s);
            }
        };
    }

    // 这里单独拿出这个登陆成功回调 : 是为了  token 变动 或在别处登录 ,  点击主页 心跳接口检测 token失效后会 跳转到登陆页登陆成功后 没有注册回调 导致空指针问题
    public void tyTokenLossLogin(TYListener tyListener) {
        ActivityLogin.tyListener = tyListener;
    }

    public void tyLogin(TYListener tyListener) {
        ActivityLogin.tyListener = tyListener;
        mContext.startActivity(new Intent(mContext, HomeActivity.class));
    }

    // 这个是针对淘币充值
    public void setPayCallBack(PayCallBack callBack) {
        ActivityWeb.payCallBack = callBack;
    }




    // 这个是针对游戏内充值
    public void tPay(String money, String name, int count, String gameOrder, GamePayCallBack callBack) {
        if (TextUtils.isEmpty(money) || TextUtils.isEmpty(name))
            return;
        ActivityWeb.gamePayCallBack = callBack;
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.putExtra("money", money);
        intent.putExtra("name", name);
        intent.putExtra("count", count);
        intent.putExtra("gameOrder", gameOrder);
        mContext.startActivity(intent);
    }

    /**
     * 外部用户快捷登陆
     *
     * @param sfOnlineUser 外部登陆用户信息类
     */
    public void quickLogin(final SFOnlineUser sfOnlineUser, final Object o) {
        String url = HttpTaskValues.API_POST_YJLOGIN;
        RequestParams params = new RequestParams();
        params.addFormDataPart("sdk", sfOnlineUser.getChannelId());
        params.addFormDataPart("app", sfOnlineUser.getProductCode());
        params.addFormDataPart("uin", sfOnlineUser.getChannelUserId());
        params.addFormDataPart("sess", sfOnlineUser.getToken());
        params.addFormDataPart("gc", MyApplication.gc);
        params.addFormDataPart("device", Utils.getModel());
        params.addFormDataPart("system", Utils.getSystem());
        params.addFormDataPart("IMEI", Utils.getIdenty(mContext));
        params.addFormDataPart("mac", Utils.getMacAddress());
        Log.i("TYMicroEnd", params.toString());
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    try {
                        String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
                        Log.i(TAG, "1.2.1 易接登陆" + rawJsonData);
                        UserInfo.getInstance().setToken(new org.json.JSONObject(jsonObject.get("data").toString()).getString("ty_ctoken"));
                        UserInfo.getInstance().setCid(new org.json.JSONObject(jsonObject.get("data").toString()).getString("ty_cid"));
                        UserInfo.getInstance().setGopenid(new org.json.JSONObject(jsonObject.get("data").toString()).getString("gopenid"));
                        sfLoginListener.onLoginSuccess(sfOnlineUser, o, UserInfo.instance);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }

    /**
     * 外部用户快捷支付
     *
     * @param goodsName 商品名字
     * @param order     订单号
     * @param money     订单金额
     */
    public void quickPay(String goodsName, String order, int money) {
        long time = System.currentTimeMillis() / 1000;
        Map<String, String> map = new HashMap<>();
        map.put("ty_ctoken", UserInfo.getInstance().getToken());
        map.put("ty_cid", UserInfo.getInstance().getCid());
        map.put("gopenid", UserInfo.getInstance().getGopenid());
        map.put("signTime", time + "");
        map.put("orderNo", order);
        map.put("payMoney", money + "");
        map.put("productName", goodsName);
        map.put("gameArea", info.getZoneName());
        map.put("roleName", info.getRoleName());
        map.put("roleLevel", info.getRoleLevel());
        String sign = SignUtils.getSign(map, MyApplication.secretKey);
        Log.e(TAG, sign);

        String url = HttpTaskValues.API_POST_YJLOGIN;
        RequestParams params = new RequestParams();
        params.addFormDataPart("orderNo", order);
        params.addFormDataPart("payMoney", money);
        params.addFormDataPart("productName", goodsName);
        params.addFormDataPart("gameArea", info.getZoneName());
        params.addFormDataPart("roleName", info.getRoleName());
        params.addFormDataPart("roleLevel", info.getRoleLevel());
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
            @Override
            protected boolean onSuccess(JSONObject jsonObject, String msg) {
                if (super.onSuccess(jsonObject, msg)) {
                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
                    Log.i(TAG, "1.2.2 上报易接支付订单" + rawJsonData);
                }
                return super.onSuccess(jsonObject, msg);
            }
        });
    }

//    private void getUser(final SFOnlineUser sfOnlineUser, final Object o, final String ty_ctoken, final String ty_cid, final String gopenid) {
//        String url = HttpTaskValues.API_POST_USERINFO;
//        RequestParams params = new RequestParams();
//        params.addFormDataPart("ty_ctoken", ty_ctoken);
//        params.addFormDataPart("ty_cid", ty_cid);
//        params.addFormDataPart("gopenid", gopenid);
//        HttpRequest.post(url, params, new HttpRequestCallBack(mContext) {
//            @Override
//            protected boolean onSuccess(JSONObject jsonObject, String msg) {
//                if (super.onSuccess(jsonObject, msg)) {
//                    String rawJsonData = JsonFormatUtils.formatJson(jsonObject.toJSONString());
//                    Log.i(TAG, "1.5.1 我的用户信息接口(易接登录获取用户信息)" + rawJsonData);
//                    UserInfo.instance = new Gson().fromJson(jsonObject.get("data").toString(), UserInfo.class);
//                    UserInfo.getInstance().setToken(ty_ctoken);
//                    UserInfo.getInstance().setCid(ty_cid);
//                    UserInfo.getInstance().setGopenid(gopenid);
//                    SharedPreferencesUtil.saveObj(mContext, MyApplication.USER_INFO, UserInfo.getInstance());
//                    //回调用户信息
//
//                }
//                return super.onSuccess(jsonObject, msg);
//            }
//        });
//    }

    /**
     * 上传游戏角色信息
     *
     * @param roleInfo 游戏角色信息类
     */
    public void setRole(RoleInfo roleInfo) {
        String url = HttpTaskValues.API_POST_LOGINGAME;
        RequestParams params = new RequestParams();
        params.addFormDataPart("ty_ctoken", UserInfo.getInstance().getToken());
        params.addFormDataPart("ty_cid", UserInfo.getInstance().getCid());
        params.addFormDataPart("gopenid", UserInfo.getInstance().getGopenid());
        params.addFormDataPart("area", roleInfo.getZoneName());
        params.addFormDataPart("roleName", roleInfo.getRoleName());
        params.addFormDataPart("level", roleInfo.getRoleLevel());
        HttpRequest.post(url, params, new HttpRequestCallBack(mContext));
    }

    public void onDestroy() {
        //游戏退出时通知到服务器
        MyApplication.showToast("游戏退出了");
    }
}
