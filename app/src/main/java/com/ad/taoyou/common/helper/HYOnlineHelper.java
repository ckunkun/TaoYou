package com.ad.taoyou.common.helper;

import android.app.Activity;
import android.content.Context;

import com.ad.taoyou.common.base.BaseActivity;
import com.snowfish.cn.ganga.helper.SFOnlineExitListener;
import com.snowfish.cn.ganga.helper.SFOnlineHelper;
import com.snowfish.cn.ganga.helper.SFOnlineInitListener;
import com.snowfish.cn.ganga.helper.SFPayResultExtendListener;

import java.util.Map;

public class HYOnlineHelper {

    private static Helper helper;
    private static Activity mContext;

    public static void init(Activity context, int state) {
        mContext = context;
        helper = new Helper(context);
        BaseActivity.state = state;
    }

    public static void onDestroy() {
        helper.onDestroy();
    }

    public static void setRole(RoleInfo role) {
        helper.setRole(role);
    }

    public static void login(TYListener tyListener) {
        helper.tyLogin(tyListener);
    }
    public static void tyTokenLoseLogin(TYListener tyListener) {
        helper.tyTokenLossLogin(tyListener);
    }

    public static void tPay(String money, String name, int count, String gameOrder, GamePayCallBack callBack) {
        helper.tPay(money, name, count, gameOrder, callBack);
    }

    public static void setPayCallBack(PayCallBack callBack){
        helper.setPayCallBack(callBack);
    }


    /*********************************易接**********************************/

    public static void onCreate(Activity activity, final HYOnlineInitListener onlineInitListener) {
        SFOnlineHelper.onCreate(activity, new SFOnlineInitListener() {
            @Override
            public void onResponse(String s, String s1) {
                onlineInitListener.onResponse(s, s1);
            }
        });
    }

    public static void onStop(Activity activity) {
        SFOnlineHelper.onStop(activity);
    }

    public static void onResume(Activity activity) {
        SFOnlineHelper.onResume(activity);
    }

    public static void onPause(Activity activity) {
        SFOnlineHelper.onPause(activity);
    }

    public static void onRestart(Activity activity) {
        SFOnlineHelper.onRestart(activity);
    }

    public static void onDestroy(Activity activity) {
        SFOnlineHelper.onDestroy(activity);
    }

    public static void exit(Activity var0, SFOnlineExitListener var1) {
        SFOnlineHelper.exit(var0, var1);
    }

    public static void login(Activity context, Object customParams) {
        SFOnlineHelper.login(context, customParams);
    }

    public static void logout(Activity context, Object customParams) {
        SFOnlineHelper.logout(context, customParams);
    }

    public static void setLoginListener(Activity var0, SFLoginListener var1) {
        helper.sfLoginListener = var1;
        SFOnlineHelper.setLoginListener(var0, helper.sfOnlineLoginListener);
    }

    public static boolean isMusicEnabled(Activity var0) {
        return SFOnlineHelper.isMusicEnabled(var0);
    }

    public static void charge(Context var0, String var1, int var2, int var3, String var4, String var5, SFPayResultListener var6) {
        helper.sfPayResultListener = var6;
        helper.money = var2;
        helper.goodsName = var1;
        SFOnlineHelper.charge(var0, var1, var2, var3, var4, var5, helper.sfOnlinePayResultListener);
    }

    public static void pay(Context context, int unitPrice, String unitName, int count, String callBackInfo, String callBackUrl, SFPayResultListener payResultListener) {
        helper.sfPayResultListener = payResultListener;
        helper.money = unitPrice;
        helper.goodsName = unitName;
        SFOnlineHelper.pay(context, unitPrice, unitName, count, callBackInfo, callBackUrl, helper.sfOnlinePayResultListener);
    }

    public static void payExtend(Context var0, int var1, String var2, String var3, String var4, int var5, String var6, String var7, SFPayResultListener var8) {
        helper.sfPayResultListener = var8;
        helper.money = var1;
        helper.goodsName = var2;
        SFOnlineHelper.payExtend(var0, var1, var2, var3, var4, var5, var6, var7, helper.sfOnlinePayResultListener);
    }

    public static void setRoleData(Context context, String roleId, String roleName, String roleLevel, String zoneId, String zoneName) {
        SFOnlineHelper.setRoleData(context, roleId, roleName, roleLevel, zoneId, zoneName);
        //易接登录传递游戏角色参数同时传递到淘游服务器
        helper.info = new RoleInfo();
        helper.info.setRoleId(roleId);//游戏角色id
        helper.info.setRoleName(roleName);//角色名称
        helper.info.setRoleLevel(roleLevel);//角色等级
        helper.info.setZoneId(zoneId);//区服id
        helper.info.setZoneName(zoneName);//区服名称
        helper.info.setFightPower("0");//战斗力
        helper.info.setChargeMoney("0");//充值金额
    }

    public static void setData(Context var0, String var1, Object var2) {
        SFOnlineHelper.setData(var0, var1, var2);
    }

    public static void onCreate(Activity var0, SFOnlineInitListener var1) {
        SFOnlineHelper.onCreate(var0, var1);
    }

    public static String extend(Activity var0, String var1, Map var2) {
        return SFOnlineHelper.extend(var0, var1, var2);
    }

    public static void setPayResultExtendListener(Activity var0, SFPayResultExtendListener var1) {
        SFOnlineHelper.setPayResultExtendListener(var0, var1);
    }


}
