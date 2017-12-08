package com.ad.taoyou.swk.home;

/**
 * Created by sunweike on 2017/9/13.
 */

public class OrderInfo {
    private String payUrl;
    private String preUrl;
    private String payurl;
    private String preurl;
    private String mweb_url;
    private String orderno;
    private String payOrderNo;
    private String order;
    private String payFormType;

    public String getPayurl() {
        return payurl;
    }

    public void setPayurl(String payurl) {
        this.payurl = payurl;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getMweb_url() {
        return mweb_url;
    }

    public void setMweb_url(String mweb_url) {
        this.mweb_url = mweb_url;
    }

    /**
     * order : KiytBTKVVGjmYoqhMMFmnwMNWGrBnIPY
     * preurl : https%3A%2F%2Fmapi.alipay.com%2Fgateway.do%3F_input_charset%3DUTF-8%26notify_url%3Dhttp%3A%2F%2Fapi.xjtmjoy.com%3A8062%2Fapp%2Fpay%2Falipay%2Fnotify.shtml%26out_trade_no%3DKiytBTKVVGjmYoqhMMFmnwMNWGrBnIPY%26partner%3D2088421499757105%26payment_type%3D1%26seller_id%3D2088421499757105%26service%3Dalipay.wap.create.direct.pay.by.user%26show_url%3Dhttp%3A%2F%2Fm.xjtmjoy.com%2Fsdk%2FcloseIframe.html%26subject%3D2450%E5%85%83%E5%AE%9D%26total_fee%3D0.01%26sign%3D8a8c17af2543d51f3733783d1ec2dcbd%26sign_type%3DMD5
     */



    public String getPreUrl() {
        return preUrl;
    }

    public void setPreUrl(String preUrl) {
        this.preUrl = preUrl;
    }


    public String getPayOrderNo() {
        return payOrderNo;
    }

    public void setPayOrderNo(String payOrderNo) {
        this.payOrderNo = payOrderNo;
    }

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getPreurl() {
        return preurl;
    }

    public void setPreurl(String preurl) {
        this.preurl = preurl;
    }
}
