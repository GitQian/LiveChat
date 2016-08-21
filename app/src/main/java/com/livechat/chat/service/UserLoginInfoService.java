package com.livechat.chat.service;

import android.content.Context;

import com.livechat.chat.db.UserLoginInfoDAL;
import com.livechat.chat.entity.UserLoginInfoBean;

import java.util.List;

/**
 * 用户的业务服务
 * Created by Administrator on 2016/5/6.
 */
public class UserLoginInfoService {

    /**
     * 声明用户数据对象
     */
    private UserLoginInfoDAL userLoginInfoDAL;

    public UserLoginInfoService(final Context mContext) {
        this.userLoginInfoDAL = new UserLoginInfoDAL(mContext);
    }

    /**
     * 根据用户的唯一ID去本地查询是否存在此记录
     *
     * @param sUserId 用户ID
     * @return 0存在;-1不存在
     */
    public int isExists(final String sUserId) {
        return userLoginInfoDAL.isExists(sUserId);
    }

    /**
     * 添加用户数据
     *
     * @param userLoginInfoBean 用户对象
     * @return 0添加成功;-1添加失败
     */
    public int insertUserInfo(final UserLoginInfoBean userLoginInfoBean) {
        return userLoginInfoDAL.insertUserInfo(userLoginInfoBean);
    }

    /**
     * 更新用户数据
     *
     * @param userLoginInfoBean 用户对象
     * @return 0更新成功;-1更新失败
     */
    public int updateUserInfo(final UserLoginInfoBean userLoginInfoBean) {
        return userLoginInfoDAL.updateUserInfo(userLoginInfoBean);
    }

    /**
     * 修改用户的昵称
     *
     * @param sNickname 修改的昵称
     * @param sAccount  用户唯一的账号
     * @return 0修改成功;-1修改失败
     */
    public int updateUsersNickname(final String sNickname, final String sAccount) {
        return userLoginInfoDAL.updateUsersNickname(sNickname, sAccount);
    }

    /**
     * 修改用户的性别
     *
     * @param iGender  修改的性别
     * @param sAccount 用户唯一的账号
     * @return 0修改成功;-1修改失败
     */
    public int updateUseriGender(final int iGender, final String sAccount) {
        return userLoginInfoDAL.updateUseriGender(iGender, sAccount);
    }

    /**
     * 修改用户的个性签名
     *
     * @param sSignature 修改的个性签名
     * @param sAccount   用户唯一的账号
     * @return 0修改成功;-1修改失败
     */
    public int updateUsersSignature(final String sSignature, final String sAccount) {
        return userLoginInfoDAL.updateUsersSignature(sSignature, sAccount);
    }

    /**
     * 修改用户的头像
     *
     * @param sHeaderImage 修改的头像路径
     * @param sAccount     用户唯一的账号
     * @return 0修改成功;-1修改失败
     */
    public int updateUsersHeaderImage(final String sHeaderImage, final String sAccount) {
        return userLoginInfoDAL.updateUsersHeaderImage(sHeaderImage, sAccount);
    }

    /**
     * 修改用户的邮箱地址
     *
     * @param sEmail   修改的邮箱地址
     * @param sAccount 用户唯一的账号
     * @return 0修改成功;-1修改失败
     */
    public int updateUsersEmail(final String sEmail, final String sAccount) {
        return userLoginInfoDAL.updateUsersEmail(sEmail, sAccount);
    }

    /**
     * 修改用户的登陆状态
     *
     * @param iOnlineStatus 是否已登陆过
     * @param sAccount      用户唯一的账号
     * @return 0修改成功;-1修改失败
     */
    public int updateUseriLoginState(final int iOnlineStatus, final String sAccount) {
        return userLoginInfoDAL.updateUseriLoginState(iOnlineStatus, sAccount);
    }

    /**
     * 修改用户的地址
     *
     * @param sAddress 修改的地址
     * @param sAccount 用户唯一的账号
     * @return 0修改成功;-1修改失败
     */
    public int updateUsersAddresses(final String sAddress, final String sAccount) {
        return userLoginInfoDAL.updateUsersAddresses(sAddress, sAccount);
    }

    /**
     * 删除用户数据(根据用户账号)
     *
     * @param sAccount 账号唯一
     * @return 0成功;-1失败
     */
    public int deleteUserInfo(final String sAccount) {
        return userLoginInfoDAL.deleteUserInfo(sAccount);
    }

    /**
     * 查询用户数据(单条)
     *
     * @param sPhone 用户手机号码
     * @return 对象数据
     */
    public UserLoginInfoBean getSimpleUserLoginInfo(final String sPhone) {
        return userLoginInfoDAL.getSimpleUserLoginInfo(sPhone);
    }

    /**
     * 查询用户数据(单条)
     *
     * @param sAccount 用户账号
     * @return 用户对象数据
     */
    public UserLoginInfoBean getSimpleUserLoginInfoBysAccount(final String sAccount) {
        return userLoginInfoDAL.getSimpleUserLoginInfoBysAccount(sAccount);
    }

    /**
     * 查询用户数据(全部)
     *
     * @return 对象的集合
     */
    public List<UserLoginInfoBean> getListUserLoginInfo() {
        return userLoginInfoDAL.getListUserLoginInfo();
    }

}
