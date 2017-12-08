package com.ad.taoyou.swk.login;

/**
 * Created by sunweike on 2017/9/4.
 */

public class UserInfo {

  public static UserInfo instance;

  public static UserInfo getInstance() {
    if (instance == null) {
      synchronized (UserInfo.class) {
        if (instance == null) {
          instance = new UserInfo();
        }
      }
    }
    return instance;
  }

  private String cid;
  private String token;
  private String gopenid;
  private String sex;
  private String nickname;
  private String exp;
  private String level;
  private String no;
  private String vip;
  private String heartbeatInterval;
  private String balanceCoin;//淘币
  private String balanceScore;//淘豆
  private String phone;
  private String gameCode;
  public String getGameCode() {
    return gameCode;
  }

  public void setGameCode(String gameCode) {
    this.gameCode = gameCode;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getBalanceCoin() {

    if (balanceCoin == null || balanceCoin.equals("")) {
      return balanceCoin;
    } else {
      return Integer.parseInt(balanceCoin) / 100 + "";
    }
  }

  public void setBalanceCoin(String balanceCoin) {
    this.balanceCoin = balanceCoin;
  }

  public String getBalanceScore() {
    if (balanceScore == null || balanceScore.equals("")) {
      return balanceScore;
    } else {
      return Integer.parseInt(balanceScore) / 100 + "";
    }
  }

  public void setBalanceScore(String balanceScore) {
    this.balanceScore = balanceScore;
  }

  @Override public String toString() {
    return "UserInfo{"
        + "cid='"
        + cid
        + '\''
        + ", token='"
        + token
        + '\''
        + ", gopenid='"
        + gopenid
        + '\''
        + ", sex='"
        + sex
        + '\''
        + ", nickname='"
        + nickname
        + '\''
        + ", exp='"
        + exp
        + '\''
        + ", level='"
        + level
        + '\''
        + ", no='"
        + no
        + '\''
        + ", vip='"
        + vip
        + '\''
        + ", headImgUrl='"
        + headImgUrl
        + '\''
        + '}';
  }

  public String getHeartbeatInterval() {
    return heartbeatInterval;
  }

  public void setHeartbeatInterval(String heartbeatInterval) {
    this.heartbeatInterval = heartbeatInterval;
  }

  public String getExp() {
    return exp;
  }

  public void setExp(String exp) {
    this.exp = exp;
  }

  public String getLevel() {
    return level;
  }

  public void setLevel(String level) {
    this.level = level;
  }

  public String getNo() {
    return no;
  }

  public void setNo(String no) {
    this.no = no;
  }

  public String getVip() {
    return vip;
  }

  public void setVip(String vip) {
    this.vip = vip;
  }

  private String headImgUrl;

  public String getCid() {
    return cid;
  }

  public void setCid(String cid) {
    this.cid = cid;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getGopenid() {
    return gopenid;
  }

  public void setGopenid(String gopenid) {
    this.gopenid = gopenid;
  }

  public String getSex() {
    return sex;
  }

  public void setSex(String sex) {
    this.sex = sex;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  public String getHeadImgUrl() {
    return headImgUrl;
  }

  public void setHeadImgUrl(String headImgUrl) {
    this.headImgUrl = headImgUrl;
  }
}
