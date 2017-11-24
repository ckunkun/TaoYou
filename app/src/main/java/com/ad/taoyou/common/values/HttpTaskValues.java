package com.ad.taoyou.common.values;

/**
 * Created by sunweike on 2017/8/25.
 */

public class HttpTaskValues {

    //    private static final String HTTP = "http://api.itoyo.cn/app/";//域名
    private static final String HTTP = "http://api.itoyo.cn:8081/app/";//测试域名

    /*******************************用户信息********************************/
    public static final String API_POST_GETREGTELCODE = HTTP + "passport/pf/getRegTelCode.shtml";//1.1.1获取手机注册验证码
    public static final String API_POST_REGBYTEL = HTTP + "passport/pf/regByTel.shtml";//1.1.2手机注册
    public static final String API_POST_LOGIN = HTTP + "passport/pf/loginPf.shtml";//1.1.3手机/账户 密码登录
    public static final String API_POST_GETRESETTELCODE = HTTP + "passport/pf/getResetTelCode.shtml";//1.1.4找回密码->获取手机验证码
    public static final String API_POST_RESETBYTEL = HTTP + "passport/pf/resetByTel.shtml";//1.1.5找回密码->手机
    public static final String API_POST_USERINFO = HTTP + "myuser/info.shtml";//1.5.1 我的用户信息接口


    /*******************************支付宝********************************/
    public static final String API_POST_ALIPAY_GETAUTHPARAMS = HTTP + "passport/alipay/getAuthParams.shtml";//1.4.1 获取授权参数信息接口
    public static final String API_POST_ALIPAY_LOGIN = HTTP + "passport/alipay/login.shtml";//1.4.2 支付宝登陆接口
    public static final String API_POST_ALIPAY_PREWAPCOINORDER = HTTP + "pay/alipay/preWapCoinOrder.shtml";//1.4.3 创建手机网站淘币预订单
    public static final String API_POST_ALIPAY_PREWAPGAMEORDER = HTTP + "pay/alipay/preWapGameOrder.shtml";//1.4.4 创建游戏预支付订单
    public static final String API_POST_ALIPAY_CALLBACK = HTTP + "pay/alipay/callback.shtml";//1.4.5 支付宝回调
    public static final String API_POST_GAME_PREORDER = HTTP + "pay/yijie/handleOrder.shtml";//游戏充值订单

    /*******************************日志********************************/
    public static final String API_POST_ACTIVATES = HTTP + "log/activate.shtml";
    public static final String API_POST_HEARTBEAT = HTTP + "log/heartbeat.shtml";
    public static final String API_POST_LOGINGAME = HTTP + "log/loginGame.shtml";

    /*******************************易接登录********************************/
    public static final String API_POST_YJLOGIN = HTTP + "passport/yijie/loginYijie.shtml";


    /*******************************安全中心********************************/
    public static final String API_POST_SAFE_CENTER = HTTP + "passport/security/center.shtml";
    public static final String API_POST_SAFE_SETPWD = HTTP + "passport/security/setPwd.shtml";
    public static final String API_POST_SAFE_GETCODE = HTTP + "passport/security/getResetTelCode.shtml";
    public static final String API_POST_SAFE_VERIFYCODE = HTTP + "passport/security/verifyResetTelCode.shtml";
    public static final String API_POST_SAFE_GETNEWCODE = HTTP + "passport/security/getNewTelCode.shtml";
    public static final String API_POST_SAFE_SETTEL = HTTP + "passport/security/setTel.shtml";

    /*******************************微信登录********************************/
    public static final String API_POST_LOGIN_WX = HTTP + "passport/wechat/login.shtml";

    /*******************************礼包********************************/
    public static final String API_POST_GIFT_RECOMMEND = HTTP + "gift/recommend.shtml";
    public static final String API_POST_GIFT_ALL = HTTP + "gift/allList.shtml";
    public static final String API_POST_GIFT_MY = HTTP + "gift/myGift.shtml";
    public static final String API_POST_GIFT_RECEIVE = HTTP + "gift/receive.shtml";
    public static final String API_POST_GIFT_INFO = HTTP + "gift/info.shtml";
    public static final String API_POST_GIFT_CODE = HTTP + "gift/getGiftCode.shtml";

    /*******************************活动********************************/
    public static final String API_POST_ACTIVITY_LIST = HTTP + "activity/list.shtml";
    public static final String API_POST_ACTIVITY_DETAIL = HTTP + "activity/detail.shtml";

    /*******************************余额********************************/
    public static final String API_POST_COIN = HTTP + "myuser/coin.shtml";
    public static final String API_POST_SCORE = HTTP + "myuser/score.shtml";

    /*******************************消息********************************/
    public static final String API_POST_MESSAGE_LIST = HTTP + "tidings/systemList.shtml";
    public static final String API_POST_CONSUME_LIST = HTTP + "tidings/consumeList.shtml";
    public static final String API_POST_RECHARGE_LIST = HTTP + "tidings/rechargeList.shtml";
    public static final String API_POST_MESSAGE_DETAILS = HTTP + "tidings/systemDetail.shtml";
    public static final String API_POST_MESSAGE_COUNT = HTTP + "tidings/countUnreadBySystem.shtml";

    /*******************************帮助********************************/
    public static final String API_POST_HELP_TYPE_LIST = HTTP + "qa/typeList.shtml";
    public static final String API_POST_HELP_LIST= HTTP + "qa/list.shtml";
    public static final String API_POST_HELP_DETAIL= HTTP + "qa/detail.shtml";

}
