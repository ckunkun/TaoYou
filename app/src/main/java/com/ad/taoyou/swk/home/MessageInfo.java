package com.ad.taoyou.swk.home;

/**
 * Created by sunweike on 2017/8/30.
 */

public class MessageInfo {

    private String tidingsId;
    private String read;
    private String senderName;
    private String title;
    private String createTime;
    private String info;

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }


    public String getTidingsId() {
        return tidingsId;
    }

    public void setTidingsId(String tidingsId) {
        this.tidingsId = tidingsId;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }


}
