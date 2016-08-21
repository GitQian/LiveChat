package com.livechat.chat.service;

import android.content.Context;

import com.livechat.chat.db.EotherUrlDAL;
import com.livechat.chat.entity.EotherUrlBean;

import java.util.List;

/**
 * Created by Administrator on 2016/6/10.
 */
public class EotherUrlService {

    private EotherUrlDAL eotherUrlDAL;

    public EotherUrlService(Context mContext) {
        this.eotherUrlDAL = new EotherUrlDAL(mContext);
    }

    /**
     * 添加"其他"数据
     *
     * @param eotherUrlBean "其他"对象
     * @return
     */
    public int insertEotherUrlInfo(final EotherUrlBean eotherUrlBean) {
        return eotherUrlDAL.insertEotherUrlInfo(eotherUrlBean);
    }

    /**
     * 删除"其他"数据
     *
     * @param sMenuId "其他"ID
     * @return
     */
    public int deleteEotherUrlInfo(final String sMenuId) {
        return eotherUrlDAL.deleteEotherUrlInfo(sMenuId);
    }

    /**
     * 修改"其他"的数据
     *
     * @param eotherUrlBean "其他"对象
     * @return
     */
    public int updateEotherUrlInfo(final EotherUrlBean eotherUrlBean) {
        return eotherUrlDAL.updateEotherUrlInfo(eotherUrlBean);
    }

    /**
     * 根据"其他"的唯一ID去本地查询是否存在此记录
     *
     * @param sMenuId "其他"ID
     * @return 0成功;-1失败
     */
    public int isExists(final String sMenuId) {
        return eotherUrlDAL.isExists(sMenuId);
    }

    /**
     * 查询全部的"其他"数据
     *
     * @return "其他"的集合
     */
    public List<EotherUrlBean> getListEotherUrlInfo() {
        return eotherUrlDAL.getListEotherUrlInfo();
    }

}
