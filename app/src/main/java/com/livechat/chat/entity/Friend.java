package com.livechat.chat.entity;

/**
 * 好友实体
 * Created by Administrator on 2016/4/15.
 */
public class Friend {

    private String sUserId;
    private String sUserName;
    private String sPortraitUri;

    public Friend() {

    }

    public Friend(String sUserId, String sUserName, String sPortraitUri) {
        this.sUserId = sUserId;
        this.sUserName = sUserName;
        this.sPortraitUri = sPortraitUri;
    }

    public String getsUserId() {
        return sUserId;
    }

    public void setsUserId(String sUserId) {
        this.sUserId = sUserId;
    }

    public String getsUserName() {
        return sUserName;
    }

    public void setsUserName(String sUserName) {
        this.sUserName = sUserName;
    }

    public String getsPortraitUri() {
        return sPortraitUri;
    }

    public void setsPortraitUri(String sPortraitUri) {
        this.sPortraitUri = sPortraitUri;
    }

}
