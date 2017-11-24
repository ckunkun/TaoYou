package com.ad.taoyou.swk.gift;

/**
 * Created by sunweike on 2017/8/30.
 */

public class GiftInfo {

    private String giftId;
    private String deline;//是否过期(boolean)
    private String gameName;//游戏名称
    private String logoPath;//游戏logo
    private String name;//礼包名称
    private String gameCode;//游戏code
    private String info;//礼包详情
    private String recId;//领取id
    private String receive;//是否领取(boolean)
    private String total;//总量
    private String count;//已领
    private String runPf;
    private String startTime;
    private String endTime;
    private String useInfo;

    public String getUseInfo() {
        return useInfo;
    }

    public void setUseInfo(String useInfo) {
        this.useInfo = useInfo;
    }

    public String getRunPf() {
        return runPf;
    }

    public void setRunPf(String runPf) {
        this.runPf = runPf;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public String getRecId() {
        return recId;
    }

    public void setRecId(String recId) {
        this.recId = recId;
    }


    public String getGiftId() {
        return giftId;
    }

    public void setGiftId(String giftId) {
        this.giftId = giftId;
    }

    public String getDeline() {
        return deline;
    }

    public void setDeline(String deline) {
        this.deline = deline;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public String getLogoPath() {
        return logoPath;
    }

    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String gameCode) {
        this.gameCode = gameCode;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


}
