package com.livechat.chat.service;

import android.content.Context;

import com.livechat.chat.db.CustomerInfoDAL;
import com.livechat.chat.entity.CustomerBean;

import java.util.List;

/**
 * 客服的业务服务
 * Created by Administrator on 2016/5/7.
 */
public class CustomerInfoService {

    /**
     * 声明客服数据对象
     */
    private CustomerInfoDAL customerInfoDAL;

    public CustomerInfoService(final Context mContext) {
        this.customerInfoDAL = new CustomerInfoDAL(mContext);
    }

    /**
     * 添加客服数据
     *
     * @param customerBean 客服对象
     * @return 0添加成功;-1添加失败
     */
    public int insertCustomerInfo(final CustomerBean customerBean) {
        return customerInfoDAL.insertCustomerInfo(customerBean);
    }

    /**
     * 根据客服ID进行删除客服
     *
     * @param sAccount 客服账号
     * @return 0删除成功;-1删除失败
     */
    public int deleteCustomerInfo(final String sAccount) {
        return customerInfoDAL.deleteCustomerInfo(sAccount);
    }

    /**
     * 修改客服的数据
     *
     * @param customerBean 客服对象
     * @return 0修改成功;-1修改失败
     */
    public int updateCustomerInfo(final CustomerBean customerBean) {
        return customerInfoDAL.updateCustomerInfo(customerBean);
    }

    /**
     * 根据客服的唯一ID去本地查询是否存在此记录
     *
     * @param sAccount 客服账号
     * @return 0存在记录;-1不存在记录
     */
    public int isExists(final String sAccount) {
        return customerInfoDAL.isExists(sAccount);
    }

    /**
     * 查询客服数据(单条)
     *
     * @param sAccount 客服账号
     * @return 客服对象
     */
    public CustomerBean getSimpleCustomerInfo(final String sAccount) {
        return customerInfoDAL.getSimpleCustomerInfo(sAccount);
    }

    /**
     *
     *
     * @return 客服的集合
     */
    /**
     * 查询全部的客服数据
     *
     * @param name 客服昵称
     * @return 客服的集合
     */
    public List<CustomerBean> getListCustomerInfo(String name) {
        return customerInfoDAL.getListCustomerInfo(name);
    }

    /**
     * 查询全部的客服数据
     *
     * @return 客服的集合
     */
    public List<CustomerBean> getListCustomerInfo() {
        return customerInfoDAL.getListCustomerInfo();
    }

}
