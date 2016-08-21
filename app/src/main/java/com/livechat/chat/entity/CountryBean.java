package com.livechat.chat.entity;

/**
 * 国家
 * Created by Administrator on 2016/3/30.
 */
public class CountryBean {

    private String country;
    private String code;

    public CountryBean() {

    }

    public CountryBean(String country, String code) {
        this.country = country;
        this.code = code;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

}
