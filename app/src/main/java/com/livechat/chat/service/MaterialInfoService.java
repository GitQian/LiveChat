package com.livechat.chat.service;

import android.content.Context;

import com.alibaba.fastjson.JSON;
import com.livechat.chat.db.MaterialInfoDAL;
import com.livechat.chat.entity.InformationBean;
import com.livechat.chat.entity.MaterialInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 素材的业务服务
 * Created by Administrator on 2016/5/23.
 */
public class MaterialInfoService {

    /**
     * 声明素材数据对象
     */
    private MaterialInfoDAL materialInfoDAL;

    public MaterialInfoService(final Context mContext) {
        this.materialInfoDAL = new MaterialInfoDAL(mContext);
    }

    /**
     * 添加素材数据
     *
     * @param materialInfoBean 素材对象
     * @return 0添加成功;-1添加失败
     */
    public int insertMaterialInfo(final MaterialInfoBean materialInfoBean) {
        return materialInfoDAL.insertMaterialInfo(materialInfoBean);
    }

    /**
     * 根据素材推送过来的日期进行删除客服
     *
     * @param lDate 推送日期
     * @return 0删除成功;-1删除失败
     */
    public int deleteMaterialInfo(final long lDate) {
        return materialInfoDAL.deleteMaterialInfo(lDate);
    }

    /**
     * 修改素材的数据
     *
     * @param materialInfoBean 素材对象
     * @return 0修改成功;-1修改失败
     */
    public int updateMaterialInfo(final MaterialInfoBean materialInfoBean) {
        return materialInfoDAL.updateMaterialInfo(materialInfoBean);
    }

    /**
     * 根据素材的推送ID去本地查询是否存在此记录
     *
     * @param sItemId 推送ID
     * @return 0存在;-1不存在
     */
    public int isExists(final String sItemId) {
        return materialInfoDAL.isExists(sItemId);
    }

    /**
     * 查询素材数据(单条)
     *
     * @param sItemId 推送ID
     * @return 素材对象
     */
    public MaterialInfoBean getSimpleCustomerInfo(final String sItemId) {
        return materialInfoDAL.getSimpleCustomerInfo(sItemId);
    }

    /**
     * 查询全部的素材数据
     *
     * @return 素材集合
     */
    public List<MaterialInfoBean> getListCustomerInfo(String name) {
        if(name.equals("")){
            return materialInfoDAL.getListCustomerInfo();
        }else{
            List<MaterialInfoBean> listArrays=new ArrayList<>();
            List<MaterialInfoBean> listArray=materialInfoDAL.getListCustomerInfo();
            for(int i=0;i<listArray.size();i++){
                final List<InformationBean> informationBeans= JSON.parseArray(listArray.get(i).getsArticles(), InformationBean.class);
                for(int j=0;j<informationBeans.size();j++){
                    if(informationBeans.get(j).getTitle().indexOf(name)>-1){
                        listArrays.add(listArray.get(i));
                        break;
                    }
                }
            }
            return listArrays;
        }
    }

}
