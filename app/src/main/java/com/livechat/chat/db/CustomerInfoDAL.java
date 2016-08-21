package com.livechat.chat.db;

import android.content.Context;
import android.database.Cursor;

import com.livechat.chat.entity.CustomerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 对客服数据的操作
 * Created by Administrator on 2016/5/7.
 */
public class CustomerInfoDAL {

    private LiveChatDefaultDBHelper dbHelper;

    public CustomerInfoDAL(Context mContext) {
        this.dbHelper = new LiveChatDefaultDBHelper(mContext);
    }

    /**
     * 添加客服数据
     *
     * @param customerBean 客服对象
     * @return 0成功;-1失败
     */
    public int insertCustomerInfo(final CustomerBean customerBean) {
        String sSql = "insert into CustomerInfoDAL(sAccount, sNickname, sHeaderImage, sRongToken, iOnlineStatus) values(?, ?, ?, ?, ?)";
        Object[] object = new Object[]{customerBean.getsAccount(), customerBean.getsNickname(), customerBean.getsHeaderImage(), customerBean.getsRongToken(), customerBean.getiOnlineStatus()};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 根据客服ID进行删除客服
     *
     * @param sAccount 客服账号
     * @return 0成功;-1失败
     */
    public int deleteCustomerInfo(final String sAccount) {
        String sSql = "delete from CustomerInfoDAL where sAccount=?";
        Object[] object = new Object[]{sAccount};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 修改客服的数据
     *
     * @param customerBean 客服对象
     * @return 0成功;-1失败
     */
    public int updateCustomerInfo(final CustomerBean customerBean) {
        String sSql = "update CustomerInfoDAL set sNickname=?, sHeaderImage=?, sRongToken=?, iOnlineStatus=? where sAccount=?";
        Object[] object = new Object[]{customerBean.getsNickname(), customerBean.getsHeaderImage(), customerBean.getsRongToken(), customerBean.getiOnlineStatus(), customerBean.getsAccount()};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 根据客服的唯一ID去本地查询是否存在此记录
     *
     * @param sAccount 客服账号
     * @return 0成功;-1失败
     */
    public int isExists(final String sAccount) {
        int iRet = 0;
        String sSql = "select count(*) as existsTable from CustomerInfoDAL where sAccount=?";
        Cursor cursor = dbHelper.rawQuery(sSql, new String[]{sAccount});
        if (cursor != null && cursor.moveToNext()) {
            iRet = cursor.getInt(cursor.getColumnIndex("existsTable"));
        }
        return iRet;
    }

    /**
     * 查询客服数据(单条)
     *
     * @param sAccount 客服账号
     * @return 客服对象
     */
    public CustomerBean getSimpleCustomerInfo(final String sAccount) {
        CustomerBean customerBean = null;
        String sSql = "select * from CustomerInfoDAL where sAccount=?";
        Cursor cursor = dbHelper.rawQuery(sSql, new String[]{sAccount});
        if (cursor != null && cursor.moveToNext()) {
            customerBean = new CustomerBean();
            customerBean.setlId(cursor.getLong(cursor.getColumnIndex("lId")));
            customerBean.setsAccount(cursor.getString(cursor.getColumnIndex("sAccount")));
            customerBean.setsNickname(cursor.getString(cursor.getColumnIndex("sNickname")));
            customerBean.setsHeaderImage(cursor.getString(cursor.getColumnIndex("sHeaderImage")));
            customerBean.setsRongToken(cursor.getString(cursor.getColumnIndex("sRongToken")));
            customerBean.setiOnlineStatus(cursor.getInt(cursor.getColumnIndex("iOnlineStatus")));
        }
        return customerBean;
    }

    /**
     * 查询全部的客服数据
     *
     * @return 客服的集合
     */
    public List<CustomerBean> getListCustomerInfo() {
        List<CustomerBean> customerBeanList = new ArrayList<>();
        String sSql = "select * from CustomerInfoDAL order by iOnlineStatus asc";// 在线的排在前面
        Cursor cursor = dbHelper.rawQuery(sSql, new String[]{});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                CustomerBean customerBean = new CustomerBean();
                customerBean.setlId(cursor.getLong(cursor.getColumnIndex("lId")));
                customerBean.setsAccount(cursor.getString(cursor.getColumnIndex("sAccount")));
                customerBean.setsNickname(cursor.getString(cursor.getColumnIndex("sNickname")));
                customerBean.setsHeaderImage(cursor.getString(cursor.getColumnIndex("sHeaderImage")));
                customerBean.setsRongToken(cursor.getString(cursor.getColumnIndex("sRongToken")));
                customerBean.setiOnlineStatus(cursor.getInt(cursor.getColumnIndex("iOnlineStatus")));
                customerBeanList.add(customerBean);
            }
        }
        return customerBeanList;
    }

    /**
     * 根据客服昵称模糊查询客服数据
     *
     * @param name 客服昵称
     * @return
     */
    public List<CustomerBean> getListCustomerInfo(String name) {
        List<CustomerBean> customerBeanList = new ArrayList<>();
        String sSql = "select * from CustomerInfoDAL where sNickname like '%" + name + "%' order by iOnlineStatus DESC";// 在线的排在前面
        Cursor cursor = dbHelper.rawQuery(sSql, new String[]{});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                CustomerBean customerBean = new CustomerBean();
                customerBean.setlId(cursor.getLong(cursor.getColumnIndex("lId")));
                customerBean.setsAccount(cursor.getString(cursor.getColumnIndex("sAccount")));
                customerBean.setsNickname(cursor.getString(cursor.getColumnIndex("sNickname")));
                customerBean.setsHeaderImage(cursor.getString(cursor.getColumnIndex("sHeaderImage")));
                customerBean.setsRongToken(cursor.getString(cursor.getColumnIndex("sRongToken")));
                customerBean.setiOnlineStatus(cursor.getInt(cursor.getColumnIndex("iOnlineStatus")));
                customerBeanList.add(customerBean);
            }
        }
        return customerBeanList;
    }

}
