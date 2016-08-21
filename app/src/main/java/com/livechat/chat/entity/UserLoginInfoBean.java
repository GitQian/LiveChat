package com.livechat.chat.entity;

/**
 * 用户登陆的信息
 * Created by Administrator on 2016/4/23.
 */
public class UserLoginInfoBean {

    private long lid;// 表ID
    private String sUserId;// 用户ID
    private String sAccount;// 账号
    private String sPassword;// 登陆密码
    private String sNickname;// 昵称
    private String sBirthday;// 出生日期
    private int iGender;// 性别[0男;1女;]
    private String sAge;// 年龄
    private String sHeaderImage;// 用户头像
    private String sEmail;// 电子邮件
    private String sPhone;// 电话号码
    private String sAddress;// 地址
    private String sSignature;// 个性签名
    private String sRongToken;// 融云Token
    private int iStatus;// 用户状态
    private String sIp;// IP地址
    private String sCreateDay;// 创建的时间
    private int iAppType;// 应用类型[5.web;4.iOS;3.Android;]
    private int iOnlineStatus;// 在线状态
    private String sLastLogin;// 最后登陆的时间

    public UserLoginInfoBean() {

    }

    public UserLoginInfoBean(long lid, String sUserId, String sAccount, String sPassword, String sNickname, String sBirthday, int iGender, String sAge, String sHeaderImage, String sEmail, String sPhone, String sAddress, String sSignature, String sRongToken, int iStatus, String sIp, String sCreateDay, int iAppType, int iOnlineStatus, String sLastLogin) {
        this.lid = lid;
        this.sUserId = sUserId;
        this.sAccount = sAccount;
        this.sPassword = sPassword;
        this.sNickname = sNickname;
        this.sBirthday = sBirthday;
        this.iGender = iGender;
        this.sAge = sAge;
        this.sHeaderImage = sHeaderImage;
        this.sEmail = sEmail;
        this.sPhone = sPhone;
        this.sAddress = sAddress;
        this.sSignature = sSignature;
        this.sRongToken = sRongToken;
        this.iStatus = iStatus;
        this.sIp = sIp;
        this.sCreateDay = sCreateDay;
        this.iAppType = iAppType;
        this.iOnlineStatus = iOnlineStatus;
        this.sLastLogin = sLastLogin;
    }

    public long getLid() {
        return lid;
    }

    public void setLid(long lid) {
        this.lid = lid;
    }

    public String getsUserId() {
        return sUserId;
    }

    public void setsUserId(String sUserId) {
        this.sUserId = sUserId;
    }

    public String getsAccount() {
        return sAccount;
    }

    public void setsAccount(String sAccount) {
        this.sAccount = sAccount;
    }

    public String getsPassword() {
        return sPassword;
    }

    public void setsPassword(String sPassword) {
        this.sPassword = sPassword;
    }

    public String getsNickname() {
        return sNickname;
    }

    public void setsNickname(String sNickname) {
        this.sNickname = sNickname;
    }

    public String getsBirthday() {
        return sBirthday;
    }

    public void setsBirthday(String sBirthday) {
        this.sBirthday = sBirthday;
    }

    public int getiGender() {
        return iGender;
    }

    public void setiGender(int iGender) {
        this.iGender = iGender;
    }

    public String getsAge() {
        return sAge;
    }

    public void setsAge(String sAge) {
        this.sAge = sAge;
    }

    public String getsHeaderImage() {
        return sHeaderImage;
    }

    public void setsHeaderImage(String sHeaderImage) {
        this.sHeaderImage = sHeaderImage;
    }

    public String getsEmail() {
        return sEmail;
    }

    public void setsEmail(String sEmail) {
        this.sEmail = sEmail;
    }

    public String getsPhone() {
        return sPhone;
    }

    public void setsPhone(String sPhone) {
        this.sPhone = sPhone;
    }

    public String getsAddress() {
        return sAddress;
    }

    public void setsAddress(String sAddress) {
        this.sAddress = sAddress;
    }

    public String getsSignature() {
        return sSignature;
    }

    public void setsSignature(String sSignature) {
        this.sSignature = sSignature;
    }

    public String getsRongToken() {
        return sRongToken;
    }

    public void setsRongToken(String sRongToken) {
        this.sRongToken = sRongToken;
    }

    public int getiStatus() {
        return iStatus;
    }

    public void setiStatus(int iStatus) {
        this.iStatus = iStatus;
    }

    public String getsIp() {
        return sIp;
    }

    public void setsIp(String sIp) {
        this.sIp = sIp;
    }

    public String getsCreateDay() {
        return sCreateDay;
    }

    public void setsCreateDay(String sCreateDay) {
        this.sCreateDay = sCreateDay;
    }

    public int getiAppType() {
        return iAppType;
    }

    public void setiAppType(int iAppType) {
        this.iAppType = iAppType;
    }

    public int getiOnlineStatus() {
        return iOnlineStatus;
    }

    public void setiOnlineStatus(int iOnlineStatus) {
        this.iOnlineStatus = iOnlineStatus;
    }

    public String getsLastLogin() {
        return sLastLogin;
    }

    public void setsLastLogin(String sLastLogin) {
        this.sLastLogin = sLastLogin;
    }

}
