package com.ad.taoyou.swk.home;

/**
 * @author CK on 2017/12/1
 */

public class OrderBean {

  public static OrderBean instance;

  public static OrderBean getInstance() {
    if (instance == null) {
      synchronized (OrderBean.class) {
        if (instance == null) {
          instance = new OrderBean();
        }
      }
    }
    return instance;
  }

  private String mId;
  private String uId;

  public String getuId() {
    return uId;
  }

  public void setuId(String uId) {
    this.uId = uId;
  }

  public String getPayMoney() {
    return payMoney;
  }

  public void setPayMoney(String payMoney) {
    this.payMoney = payMoney;
  }

  public String getOutOrderId() {
    return outOrderId;
  }

  public void setOutOrderId(String outOrderId) {
    this.outOrderId = outOrderId;
  }

  public String getGameId() {
    return gameId;
  }

  public void setGameId(String gameId) {
    this.gameId = gameId;
  }

  public String getGoodId() {
    return goodId;
  }

  public void setGoodId(String goodId) {
    this.goodId = goodId;
  }

  public String getGoodsName() {
    return goodsName;
  }

  public void setGoodsName(String goodsName) {
    this.goodsName = goodsName;
  }

  public String getGameName() {
    return gameName;
  }

  public void setGameName(String gameName) {
    this.gameName = gameName;
  }

  public String getSign() {
    return sign;
  }

  public void setSign(String sign) {
    this.sign = sign;
  }

  public String getParam() {
    return param;
  }

  public void setParam(String param) {
    this.param = param;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  private String payMoney;
  private String outOrderId;
  private String gameId;
  private String goodId;
  private String goodsName;
  private String gameName;
  private String sign;
  private String param;
  private String time;

  public String getmId() {
    return mId;
  }

  public void setmId(String mId) {
    this.mId = mId;
  }
}
