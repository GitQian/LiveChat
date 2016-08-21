package com.livechat.chat.db;

import android.content.Context;
import android.database.Cursor;

import com.livechat.chat.entity.UserLoginInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 对用户数据的操作
 * Created by Administrator on 2016/4/23.
 */
public class UserLoginInfoDAL {

    private LiveChatDefaultDBHelper dbHelper;

    public UserLoginInfoDAL(final Context mContext) {
        this.dbHelper = new LiveChatDefaultDBHelper(mContext);
    }

    /**
     * 添加用户数据
     *
     * @param userLoginInfoBean 用户对象
     * @return 0成功;-1失败
     */
    public int insertUserInfo(final UserLoginInfoBean userLoginInfoBean) {
        String sSql = "insert into UserLoginInfoDAL(sUserId, sAccount, sPassword, sNickname, sBirthday, iGender, sAge, sHeaderImage, sEmail, sPhone, sAddress, sSignature, sRongToken, iStatus, sIp, sCreateDay, iAppType, iOnlineStatus, sLastLogin) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] object = new Object[]{userLoginInfoBean.getsUserId(), userLoginInfoBean.getsAccount(), userLoginInfoBean.getsPassword(),
                userLoginInfoBean.getsNickname(), userLoginInfoBean.getsBirthday(), userLoginInfoBean.getiGender(),
                userLoginInfoBean.getsAge(), userLoginInfoBean.getsHeaderImage(), userLoginInfoBean.getsEmail(),
                userLoginInfoBean.getsPhone(), userLoginInfoBean.getsAddress(), userLoginInfoBean.getsSignature(),
                userLoginInfoBean.getsRongToken(), userLoginInfoBean.getiStatus(), userLoginInfoBean.getsIp(),
                userLoginInfoBean.getsCreateDay(), userLoginInfoBean.getiAppType(), userLoginInfoBean.getiOnlineStatus(), userLoginInfoBean.getsLastLogin()
        };
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 删除用户数据(根据用户账号)
     *
     * @param sAccount 账号唯一
     * @return 0成功;-1失败
     */
    public int deleteUserInfo(final String sAccount) {
        String sSql = "delete from UserLoginInfoDAL where sAccount=?";
        Object[] object = new Object[]{sAccount};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 修改用户数据(根据用户Id)
     *
     * @param userLoginInfoBean 用户对象
     * @return 0成功;-1失败
     */
    public int updateUserInfo(final UserLoginInfoBean userLoginInfoBean) {
        String sSql = "update UserLoginInfoDAL set sAccount=?, sPassword=?, sNickname=?, sBirthday=?, iGender=?, sAge=?, sHeaderImage=?, sEmail=?, sPhone=?, sAddress=?, sSignature=?, sRongToken=?, iStatus=?, sIp=?, sCreateDay=?, iAppType=?, iOnlineStatus=?, sLastLogin=? where sUserId=?";
        Object[] object = new Object[]{userLoginInfoBean.getsAccount(), userLoginInfoBean.getsPassword(), userLoginInfoBean.getsNickname(),
                userLoginInfoBean.getsBirthday(), userLoginInfoBean.getiGender(), userLoginInfoBean.getsAge(),
                userLoginInfoBean.getsHeaderImage(), userLoginInfoBean.getsEmail(), userLoginInfoBean.getsPhone(),
                userLoginInfoBean.getsAddress(), userLoginInfoBean.getsSignature(), userLoginInfoBean.getsRongToken(),
                userLoginInfoBean.getiStatus(), userLoginInfoBean.getsIp(), userLoginInfoBean.getsCreateDay(),
                userLoginInfoBean.getiAppType(), userLoginInfoBean.getiOnlineStatus(), userLoginInfoBean.getsLastLogin(),
                userLoginInfoBean.getsUserId()
        };
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 修改用户的昵称
     *
     * @param sNickname 修改的昵称
     * @param sAccount  用户唯一的账号
     * @return 0成功;-1失败
     */
    public int updateUsersNickname(final String sNickname, final String sAccount) {
        String sSql = "update UserLoginInfoDAL set sNickname=? where sAccount=?";
        Object[] object = new Object[]{sNickname, sAccount};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 修改用户的性别
     *
     * @param iGender  修改的性别
     * @param sAccount 用户唯一的账号
     * @return 0成功;-1失败
     */
    public int updateUseriGender(final int iGender, final String sAccount) {
        String sSql = "update UserLoginInfoDAL set iGender=? where sAccount=?";
        Object[] object = new Object[]{iGender, sAccount};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 修改用户的个性签名
     *
     * @param sSignature 修改的个性签名
     * @param sAccount   用户唯一的账号
     * @return 0成功;-1失败
     */
    public int updateUsersSignature(final String sSignature, final String sAccount) {
        String sSql = "update UserLoginInfoDAL set sSignature=? where sAccount=?";
        Object[] object = new Object[]{sSignature, sAccount};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 修改用户的头像
     *
     * @param sHeaderImage 修改的头像路径
     * @param sAccount     用户唯一的账号
     * @return 0成功;-1失败
     */
    public int updateUsersHeaderImage(final String sHeaderImage, final String sAccount) {
        String sSql = "update UserLoginInfoDAL set sHeaderImage=? where sAccount=?";
        Object[] object = new Object[]{sHeaderImage, sAccount};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 修改用户的邮箱地址
     *
     * @param sEmail   修改的邮箱地址
     * @param sAccount 用户唯一的账号
     * @return 0成功;-1失败
     */
    public int updateUsersEmail(final String sEmail, final String sAccount) {
        String sSql = "update UserLoginInfoDAL set sEmail=? where sAccount=?";
        Object[] object = new Object[]{sEmail, sAccount};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 修改用户的登陆状态
     *
     * @param iOnlineStatus 是否已登陆过
     * @param sAccount      用户唯一的账号
     * @return 0成功;-1失败
     */
    public int updateUseriLoginState(final int iOnlineStatus, final String sAccount) {
        String sSql = "update UserLoginInfoDAL set iOnlineStatus=? where sAccount=?";
        Object[] object = new Object[]{iOnlineStatus, sAccount};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 修改用户的地址
     *
     * @param sAddress 修改的地址
     * @param sAccount 用户唯一的账号
     * @return 0成功;-1失败
     */
    public int updateUsersAddresses(final String sAddress, final String sAccount) {
        String sSql = "update UserLoginInfoDAL set sAddress=? where sAccount=?";
        Object[] object = new Object[]{sAddress, sAccount};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 根据用户的唯一ID去本地查询是否存在此记录
     *
     * @param sUserId 用户ID
     * @return 0成功;-1失败
     */
    public int isExists(final String sUserId) {
        int iRet = 0;
        String sSql = "select count(*) as existsTable from UserLoginInfoDAL where sUserId=?";
        Cursor cursor = dbHelper.rawQuery(sSql, new String[]{sUserId});
        if (cursor != null && cursor.moveToNext()) {
            iRet = cursor.getInt(cursor.getColumnIndex("existsTable"));
        }
        return iRet;
    }

    /**
     * 查询用户数据(单条)
     *
     * @param sPhone 用户手机号码
     * @return 用户对象数据
     */
    public UserLoginInfoBean getSimpleUserLoginInfo(final String sPhone) {
        UserLoginInfoBean userLoginInfoBean = null;
        String sSql = "select * from UserLoginInfoDAL where sPhone=?";
        Cursor cursor = dbHelper.rawQuery(sSql, new String[]{sPhone});
        if (cursor != null && cursor.moveToNext()) {
            userLoginInfoBean = new UserLoginInfoBean();
            userLoginInfoBean.setsUserId(cursor.getString(cursor.getColumnIndex("sUserId")));
            userLoginInfoBean.setsAccount(cursor.getString(cursor.getColumnIndex("sAccount")));
            userLoginInfoBean.setsPassword(cursor.getString(cursor.getColumnIndex("sPassword")));
            userLoginInfoBean.setsNickname(cursor.getString(cursor.getColumnIndex("sNickname")));
            userLoginInfoBean.setsBirthday(cursor.getString(cursor.getColumnIndex("sBirthday")));
            userLoginInfoBean.setiGender(cursor.getInt(cursor.getColumnIndex("iGender")));
            userLoginInfoBean.setsAge(cursor.getString(cursor.getColumnIndex("sAge")));
            userLoginInfoBean.setsHeaderImage(cursor.getString(cursor.getColumnIndex("sHeaderImage")));
            userLoginInfoBean.setsEmail(cursor.getString(cursor.getColumnIndex("sEmail")));
            userLoginInfoBean.setsPhone(cursor.getString(cursor.getColumnIndex("sPhone")));
            userLoginInfoBean.setsAddress(cursor.getString(cursor.getColumnIndex("sAddress")));
            userLoginInfoBean.setsSignature(cursor.getString(cursor.getColumnIndex("sSignature")));
            userLoginInfoBean.setsRongToken(cursor.getString(cursor.getColumnIndex("sRongToken")));
            userLoginInfoBean.setiStatus(cursor.getInt(cursor.getColumnIndex("iStatus")));
            userLoginInfoBean.setsIp(cursor.getString(cursor.getColumnIndex("sIp")));
            userLoginInfoBean.setsCreateDay(cursor.getString(cursor.getColumnIndex("sCreateDay")));
            userLoginInfoBean.setiAppType(cursor.getInt(cursor.getColumnIndex("iAppType")));
            userLoginInfoBean.setiOnlineStatus(cursor.getInt(cursor.getColumnIndex("iOnlineStatus")));
            userLoginInfoBean.setsLastLogin(cursor.getString(cursor.getColumnIndex("sLastLogin")));
        }
        return userLoginInfoBean;
    }

    /**
     * 查询用户数据(单条)
     *
     * @param sAccount 用户账号
     * @return 用户对象数据
     */
    public UserLoginInfoBean getSimpleUserLoginInfoBysAccount(final String sAccount) {
        UserLoginInfoBean userLoginInfoBean = null;
        String sSql = "select * from UserLoginInfoDAL where sAccount=?";
        Cursor cursor = dbHelper.rawQuery(sSql, new String[]{sAccount});
        if (cursor != null && cursor.moveToNext()) {
            userLoginInfoBean = new UserLoginInfoBean();
            userLoginInfoBean.setsUserId(cursor.getString(cursor.getColumnIndex("sUserId")));
            userLoginInfoBean.setsAccount(cursor.getString(cursor.getColumnIndex("sAccount")));
            userLoginInfoBean.setsPassword(cursor.getString(cursor.getColumnIndex("sPassword")));
            userLoginInfoBean.setsNickname(cursor.getString(cursor.getColumnIndex("sNickname")));
            userLoginInfoBean.setsBirthday(cursor.getString(cursor.getColumnIndex("sBirthday")));
            userLoginInfoBean.setiGender(cursor.getInt(cursor.getColumnIndex("iGender")));
            userLoginInfoBean.setsAge(cursor.getString(cursor.getColumnIndex("sAge")));
            userLoginInfoBean.setsHeaderImage(cursor.getString(cursor.getColumnIndex("sHeaderImage")));
            userLoginInfoBean.setsEmail(cursor.getString(cursor.getColumnIndex("sEmail")));
            userLoginInfoBean.setsPhone(cursor.getString(cursor.getColumnIndex("sPhone")));
            userLoginInfoBean.setsAddress(cursor.getString(cursor.getColumnIndex("sAddress")));
            userLoginInfoBean.setsSignature(cursor.getString(cursor.getColumnIndex("sSignature")));
            userLoginInfoBean.setsRongToken(cursor.getString(cursor.getColumnIndex("sRongToken")));
            userLoginInfoBean.setiStatus(cursor.getInt(cursor.getColumnIndex("iStatus")));
            userLoginInfoBean.setsIp(cursor.getString(cursor.getColumnIndex("sIp")));
            userLoginInfoBean.setsCreateDay(cursor.getString(cursor.getColumnIndex("sCreateDay")));
            userLoginInfoBean.setiAppType(cursor.getInt(cursor.getColumnIndex("iAppType")));
            userLoginInfoBean.setiOnlineStatus(cursor.getInt(cursor.getColumnIndex("iOnlineStatus")));
            userLoginInfoBean.setsLastLogin(cursor.getString(cursor.getColumnIndex("sLastLogin")));
        }
        return userLoginInfoBean;
    }

    /**
     * 查询用户数据(全部)
     *
     * @return 对象的集合
     */
    public List<UserLoginInfoBean> getListUserLoginInfo() {
        List<UserLoginInfoBean> loginInfoList = new ArrayList<>();
        String sSql = "select * from UserLoginInfoDAL";
        Cursor cursor = dbHelper.rawQuery(sSql, new String[]{});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                UserLoginInfoBean userLoginInfoBean = new UserLoginInfoBean();
                userLoginInfoBean.setsUserId(cursor.getString(cursor.getColumnIndex("sUserId")));
                userLoginInfoBean.setsAccount(cursor.getString(cursor.getColumnIndex("sAccount")));
                userLoginInfoBean.setsPassword(cursor.getString(cursor.getColumnIndex("sPassword")));
                userLoginInfoBean.setsNickname(cursor.getString(cursor.getColumnIndex("sNickname")));
                userLoginInfoBean.setsBirthday(cursor.getString(cursor.getColumnIndex("sBirthday")));
                userLoginInfoBean.setiGender(cursor.getInt(cursor.getColumnIndex("iGender")));
                userLoginInfoBean.setsAge(cursor.getString(cursor.getColumnIndex("sAge")));
                userLoginInfoBean.setsHeaderImage(cursor.getString(cursor.getColumnIndex("sHeaderImage")));
                userLoginInfoBean.setsEmail(cursor.getString(cursor.getColumnIndex("sEmail")));
                userLoginInfoBean.setsPhone(cursor.getString(cursor.getColumnIndex("sPhone")));
                userLoginInfoBean.setsAddress(cursor.getString(cursor.getColumnIndex("sAddress")));
                userLoginInfoBean.setsSignature(cursor.getString(cursor.getColumnIndex("sSignature")));
                userLoginInfoBean.setsRongToken(cursor.getString(cursor.getColumnIndex("sRongToken")));
                userLoginInfoBean.setiStatus(cursor.getInt(cursor.getColumnIndex("iStatus")));
                userLoginInfoBean.setsIp(cursor.getString(cursor.getColumnIndex("sIp")));
                userLoginInfoBean.setsCreateDay(cursor.getString(cursor.getColumnIndex("sCreateDay")));
                userLoginInfoBean.setiAppType(cursor.getInt(cursor.getColumnIndex("iAppType")));
                userLoginInfoBean.setiOnlineStatus(cursor.getInt(cursor.getColumnIndex("iOnlineStatus")));
                userLoginInfoBean.setsLastLogin(cursor.getString(cursor.getColumnIndex("sLastLogin")));
                loginInfoList.add(userLoginInfoBean);
            }
        }
        return loginInfoList;
    }

}
