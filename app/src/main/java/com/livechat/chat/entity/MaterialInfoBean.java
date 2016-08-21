package com.livechat.chat.entity;

/**
 * 素材实体类
 * Created by Administrator on 2016/5/23.
 */
public class MaterialInfoBean {

    private long lMiId;// 素材表ID
    private long lDate;// 日期
    private String sItemId;// 后台推送的ID
    private int itemType;// 类型
    private int iState;// 状态
    private String sArticles;// 文章

    public MaterialInfoBean() {

    }

    public MaterialInfoBean(long lMiId, long lDate, String sItemId, int itemType, int iState, String sArticles) {
        this.lMiId = lMiId;
        this.lDate = lDate;
        this.sItemId = sItemId;
        this.itemType = itemType;
        this.iState = iState;
        this.sArticles = sArticles;
    }

    public long getlMiId() {
        return lMiId;
    }

    public void setlMiId(long lMiId) {
        this.lMiId = lMiId;
    }

    public long getlDate() {
        return lDate;
    }

    public void setlDate(long lDate) {
        this.lDate = lDate;
    }

    public String getsItemId() {
        return sItemId;
    }

    public void setsItemId(String sItemId) {
        this.sItemId = sItemId;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getiState() {
        return iState;
    }

    public void setiState(int iState) {
        this.iState = iState;
    }

    public String getsArticles() {
        return sArticles;
    }

    public void setsArticles(String sArticles) {
        this.sArticles = sArticles;
    }

}
