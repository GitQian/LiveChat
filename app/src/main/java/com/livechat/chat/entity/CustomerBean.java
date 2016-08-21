package com.livechat.chat.entity;

import io.rong.imlib.model.MessageContent;

/**
 * 客服实体类
 * Created by Administrator on 2016/5/7.
 */
public class CustomerBean {

    private long lId;// 表id
    private String sAccount;// 账号
    private String sNickname;// 客服昵称
    private String sHeaderImage;// 客服头像
    private String sRongToken;// 融云Token
    private int iOnlineStatus;// 是否是在线状态
    private long sentTime;// 聊天时间
    private MessageContent content;// 聊天内容

    public CustomerBean() {

    }

    public CustomerBean(long lId, String sAccount, String sNickname, String sHeaderImage, String sRongToken, int iOnlineStatus) {
        this.lId = lId;
        this.sAccount = sAccount;
        this.sNickname = sNickname;
        this.sHeaderImage = sHeaderImage;
        this.sRongToken = sRongToken;
        this.iOnlineStatus = iOnlineStatus;
    }

    public long getSentTime() {
        return sentTime;
    }

    public void setSentTime(long sentTime) {
        this.sentTime = sentTime;
    }

    public MessageContent getContent() {
        return content;
    }

    public void setContent(MessageContent content) {
        this.content = content;
    }

    public long getlId() {
        return lId;
    }

    public void setlId(long lId) {
        this.lId = lId;
    }

    public String getsAccount() {
        return sAccount;
    }

    public void setsAccount(String sAccount) {
        this.sAccount = sAccount;
    }

    public String getsNickname() {
        return sNickname;
    }

    public void setsNickname(String sNickname) {
        this.sNickname = sNickname;
    }

    public String getsHeaderImage() {
        return sHeaderImage;
    }

    public void setsHeaderImage(String sHeaderImage) {
        this.sHeaderImage = sHeaderImage;
    }

    public String getsRongToken() {
        return sRongToken;
    }

    public void setsRongToken(String sRongToken) {
        this.sRongToken = sRongToken;
    }

    public int getiOnlineStatus() {
        return iOnlineStatus;
    }

    public void setiOnlineStatus(int iOnlineStatus) {
        this.iOnlineStatus = iOnlineStatus;
    }

}
