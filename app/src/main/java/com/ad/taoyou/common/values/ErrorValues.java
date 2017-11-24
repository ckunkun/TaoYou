package com.ad.taoyou.common.values;

/**
 * Created by sunweike on 2017/8/28.
 */

public class ErrorValues {
    /************************99 通用错误码*******************************/
    public static final String CODE_SUCCESS = "0";//成功
    public static final String CODE_SERVICE_ERROE = "990000001";//服务错误
    public static final String CODE_PARAMETER_ERROE = "990000002";//参数错误
    public static final String CODE_TPFINFO_ERROE = "990000003";//tpfinfo错误
    public static final String CODE_INTERFACE_ERROE = "990000004";//接口错误
    public static final String CODE_MESSAGE_ERROE = "990000005";//未找到信息
    public static final String CODE_CONFIGURE_ERROE = "990000006";//配置错误
    public static final String CODE_YJAUTOGRAPH_ERROE = "990000007";//易接签名错误
    public static final String CODE_PARAMETER_NULL_ERROR = "990000010";//参数为空
    public static final String CODE_PARAMETER_AUTOGRAPH_GENERATE_ERROR = "990000011";//签名生成失败
    public static final String CODE_PARAMETER_AUTOGRAPH_VERIFICATION_ERROR = "990000012";//签名验证失败
    public static final String CODE_PARAMETER_LOG_ERROR = "990000013";//记录日志失败
    public static final String CODE_PARAMETER_INVALID_TOKEN = "990000020";//token失效

    /************************100101 登录/注册 错误码*******************************/
    public static final String CODE_LOGIN_ERROR = "100101001";//登录失败
    public static final String CODE_USER_EXISTS = "100101002";//账户已存在
    public static final String CODE_NO_USER = "100101003";//账户不存在
    public static final String CODE_SEND_OVERDUE = "100101004";//未发送验证码或已过期
    public static final String CODE_ERROR = "100101005";//验证码错误
    public static final String CODE_USER_REGIST_ERROR = "100101006";//注册失败:  核心用户注册失败
    public static final String CODE_TYUSER_REGIST_ERROR = "100101007";//注册失败:平台用户注册失败
    public static final String CODE_YJUSER_REGIST_ERROR = "100101008";//注册失败:第三方平台用户注册失败
    public static final String CODE_YJUSER_LOGIN_ERROR = "100101009";//第三方平台登录失败
    public static final String CODE_SEND_CODE_ERROR = "100101011";//发送手机验证码失败
    public static final String CODE_PHONE_ERROR = "100101012";//手机号错误
    public static final String CODE_PASSWORD_ERROR = "100101013";//密码验证失败
    public static final String CODE_UPDATE_PASSWORD_ERROR = "100101014";//修改密码失败
    public static final String CODE_NOSET_PHONE = "100101015";//未设置手机号
    public static final String CODE_NOSET_PASSWORD = "100101016";//未设置密码
    public static final String CODE_TICKET_ERROR = "100101017";//重置凭据错误


    /************************100102 支付/账户 错误码*******************************/
    public static final String CODE_NOTFOUND_TBUSER = "100102001";//未找到淘币账户
    public static final String CODE_NOTFOUND_JFUSER = "100102002";//未找到积分账户
    public static final String CODE_YJORDER_FOUND_ERROR = "100102003";//第三方平台创建预订单失败
    public static final String CODE_ORDER_STATUS_ERROR = "100102004";//订单状态错误
    public static final String CODE_YJORDER_ERROR = "100102005";//订单第三方平台错误
    public static final String CODE_UPDATE_ORDER_ERROR = "100102006";//修改订单失败
    public static final String CODE_INIT_ORDER_ERROR = "100102007";//初始化订单失败
    public static final String CODE_CREAT_ORDER_ERROR = "100102008";//创建支付宝订单失败
    public static final String CODE_NOTFOUND_ORDER_ERROR = "100102009";//未找到订单

    /************************支付订单状态说明*******************************/
    public static final String INIT = "INIT";//初始化
    public static final String PRE = "PRE";//预创建
    public static final String ING = "ING";//支付中
    public static final String P_FAIL = "P_FAIL";//支付失败
    public static final String P_SUCC = "P_SUCC";//支付成功
    public static final String DONE = "DONE";//订单完成
}
