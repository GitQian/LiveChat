package com.livechat.chat.entity;

/**
 * 国别
 * Created by Administrator on 2016/5/12.
 */
public class CountriesBean {

    private String sCode;
    private String sName;

    public CountriesBean() {

    }

    public CountriesBean(String sCode, String sName) {
        this.sCode = sCode;
        this.sName = sName;
    }

    public String getsCode() {
        return sCode;
    }

    public void setsCode(String sCode) {
        this.sCode = sCode;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

}
