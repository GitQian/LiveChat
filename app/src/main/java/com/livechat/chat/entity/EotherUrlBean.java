package com.livechat.chat.entity;

/**
 * 其他地址显示的url
 * Created by Administrator on 2016/5/4.
 */
public class EotherUrlBean {

    private int iEId;
    private int iMenuOrder;
    private int iMenuStatus;
    private String sMenuIcon;
    private String sMenuUrl;
    private String sMenuId;
    private String sMenuName;
    private int iMenuType;

    public EotherUrlBean() {

    }

    public EotherUrlBean(int iEId, int iMenuOrder, int iMenuStatus, String sMenuIcon, String sMenuUrl, String sMenuId, String sMenuName, int iMenuType) {
        this.iEId = iEId;
        this.iMenuOrder = iMenuOrder;
        this.iMenuStatus = iMenuStatus;
        this.sMenuIcon = sMenuIcon;
        this.sMenuUrl = sMenuUrl;
        this.sMenuId = sMenuId;
        this.sMenuName = sMenuName;
        this.iMenuType = iMenuType;
    }

    public int getiEId() {
        return iEId;
    }

    public void setiEId(int iEId) {
        this.iEId = iEId;
    }

    public int getiMenuOrder() {
        return iMenuOrder;
    }

    public void setiMenuOrder(int iMenuOrder) {
        this.iMenuOrder = iMenuOrder;
    }

    public int getiMenuStatus() {
        return iMenuStatus;
    }

    public void setiMenuStatus(int iMenuStatus) {
        this.iMenuStatus = iMenuStatus;
    }

    public String getsMenuIcon() {
        return sMenuIcon;
    }

    public void setsMenuIcon(String sMenuIcon) {
        this.sMenuIcon = sMenuIcon;
    }

    public String getsMenuUrl() {
        return sMenuUrl;
    }

    public void setsMenuUrl(String sMenuUrl) {
        this.sMenuUrl = sMenuUrl;
    }

    public String getsMenuId() {
        return sMenuId;
    }

    public void setsMenuId(String sMenuId) {
        this.sMenuId = sMenuId;
    }

    public String getsMenuName() {
        return sMenuName;
    }

    public void setsMenuName(String sMenuName) {
        this.sMenuName = sMenuName;
    }

    public int getiMenuType() {
        return iMenuType;
    }

    public void setiMenuType(int iMenuType) {
        this.iMenuType = iMenuType;
    }

}
