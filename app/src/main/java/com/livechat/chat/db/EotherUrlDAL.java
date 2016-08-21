package com.livechat.chat.db;

import android.content.Context;
import android.database.Cursor;

import com.livechat.chat.entity.EotherUrlBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理其他一栏中的Url操作
 * Created by Administrator on 2016/6/10.
 */
public class EotherUrlDAL {

    private LiveChatDefaultDBHelper dbHelper;

    public EotherUrlDAL(Context mContext) {
        this.dbHelper = new LiveChatDefaultDBHelper(mContext);
    }

    /**
     * 添加"其他"数据
     *
     * @param eotherUrlBean "其他"对象
     * @return
     */
    public int insertEotherUrlInfo(final EotherUrlBean eotherUrlBean) {
        String sSql = "insert into EotherUrlDAL(iMenuOrder, iMenuStatus, sMenuIcon, sMenuUrl, sMenuId, sMenuName, iMenuType, sSign) values(?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] object = new Object[]{eotherUrlBean.getiMenuOrder(), eotherUrlBean.getiMenuStatus(), eotherUrlBean.getsMenuIcon(), eotherUrlBean.getsMenuUrl(), eotherUrlBean.getsMenuId(), eotherUrlBean.getsMenuName(), eotherUrlBean.getiMenuType()};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 删除"其他"数据
     *
     * @param sMenuId "其他"ID
     * @return
     */
    public int deleteEotherUrlInfo(final String sMenuId) {
        String sSql = "delete from EotherUrlDAL where sMenuId=?";
        Object[] object = new Object[]{sMenuId};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 修改"其他"的数据
     *
     * @param eotherUrlBean "其他"对象
     * @return
     */
    public int updateEotherUrlInfo(final EotherUrlBean eotherUrlBean) {
        String sSql = "update EotherUrlDAL set (iMenuOrder=?, iMenuStatus=?, sMenuIcon=?, sMenuUrl=?, sMenuName=?, iMenuType=?, sSign=? where sMenuId=?";
        Object[] object = new Object[]{eotherUrlBean.getiMenuOrder(), eotherUrlBean.getiMenuStatus(), eotherUrlBean.getsMenuIcon(), eotherUrlBean.getsMenuUrl(), eotherUrlBean.getsMenuName(), eotherUrlBean.getiMenuType(), eotherUrlBean.getsMenuId()};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 根据"其他"的唯一ID去本地查询是否存在此记录
     *
     * @param sMenuId "其他"ID
     * @return 0成功;-1失败
     */
    public int isExists(final String sMenuId) {
        int iRet = 0;
        String sSql = "select count(*) as existsTable from EotherUrlDAL where sMenuId=?";
        Cursor cursor = dbHelper.rawQuery(sSql, new String[]{sMenuId});
        if (cursor != null && cursor.moveToNext()) {
            iRet = cursor.getInt(cursor.getColumnIndex("existsTable"));
        }
        return iRet;
    }

    /**
     * 查询全部的"其他"数据
     *
     * @return "其他"的集合
     */
    public List<EotherUrlBean> getListEotherUrlInfo() {
        List<EotherUrlBean> eotherUrlBeanList = new ArrayList<>();
        String sSql = "select * from EotherUrlDAL";// 在线的排在前面
        Cursor cursor = dbHelper.rawQuery(sSql, new String[]{});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                EotherUrlBean eotherUrlBean = new EotherUrlBean();
                eotherUrlBean.setiMenuOrder(cursor.getInt(cursor.getColumnIndex("iMenuOrder")));
                eotherUrlBean.setiMenuStatus(cursor.getInt(cursor.getColumnIndex("iMenuStatus")));
                eotherUrlBean.setsMenuIcon(cursor.getString(cursor.getColumnIndex("sMenuIcon")));
                eotherUrlBean.setsMenuUrl(cursor.getString(cursor.getColumnIndex("sMenuUrl")));
                eotherUrlBean.setsMenuId(cursor.getString(cursor.getColumnIndex("sMenuId")));
                eotherUrlBean.setsMenuName(cursor.getString(cursor.getColumnIndex("sMenuName")));
                eotherUrlBean.setiMenuType(cursor.getInt(cursor.getColumnIndex("iMenuType")));
                eotherUrlBeanList.add(eotherUrlBean);
            }
        }
        return eotherUrlBeanList;
    }

}
