package com.livechat.chat.db;

import android.content.Context;
import android.database.Cursor;

import com.livechat.chat.entity.MaterialInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 素材的数据操作
 * Created by Administrator on 2016/5/23.
 */
public class MaterialInfoDAL {

    private LiveChatDefaultDBHelper dbHelper;

    public MaterialInfoDAL(Context mContext) {
        this.dbHelper = new LiveChatDefaultDBHelper(mContext);
    }

    /**
     * 添加素材数据
     *
     * @param materialInfoBean 素材对象
     * @return 0成功;-1失败
     */
    public int insertMaterialInfo(final MaterialInfoBean materialInfoBean) {
        String sSql = "insert into MaterialInfoDAL(lDate, sItemId, itemType, iState, sArticles) values(?, ?, ?, ?, ?)";
        Object[] object = new Object[]{materialInfoBean.getlDate(), materialInfoBean.getsItemId(), materialInfoBean.getItemType(), materialInfoBean.getiState(), materialInfoBean.getsArticles()};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 根据素材推送过来的日期进行删除客服
     *
     * @param lDate 推送日期
     * @return 0成功;-1失败
     */
    public int deleteMaterialInfo(final long lDate) {
        String sSql = "delete from MaterialInfoDAL where lDate=?";
        Object[] object = new Object[]{lDate};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 修改素材的数据
     *
     * @param materialInfoBean 素材对象
     * @return 0成功;-1失败
     */
    public int updateMaterialInfo(final MaterialInfoBean materialInfoBean) {
        String sSql = "update MaterialInfoDAL set sItemId=?, itemType=?, iState=?, sArticles=? where lDate=?";
        Object[] object = new Object[]{materialInfoBean.getsItemId(), materialInfoBean.getItemType(), materialInfoBean.getiState(), materialInfoBean.getsArticles(), materialInfoBean.getlDate()};
        return dbHelper.execSql(sSql, object);
    }

    /**
     * 根据素材的推送ID去本地查询是否存在此记录
     *
     * @param sItemId 推送ID
     * @return 0成功;-1失败
     */
    public int isExists(final String sItemId) {
        int iRet = 0;
        String sSql = "select count(*) as existsTable from MaterialInfoDAL where sItemId=?";
        Cursor cursor = dbHelper.rawQuery(sSql, new String[]{sItemId});
        if (cursor != null && cursor.moveToNext()) {
            iRet = cursor.getInt(cursor.getColumnIndex("existsTable"));
        }
        return iRet;
    }

    /**
     * 查询素材数据(单条)
     *
     * @param sItemId 推送ID
     * @return 素材对象
     */
    public MaterialInfoBean getSimpleCustomerInfo(final String sItemId) {
        MaterialInfoBean materialInfoBean = null;
        String sSql = "select * from MaterialInfoDAL where sItemId=?";
        Cursor cursor = dbHelper.rawQuery(sSql, new String[]{sItemId});
        if (cursor != null && cursor.moveToNext()) {
            materialInfoBean.setlMiId(cursor.getLong(cursor.getColumnIndex("lMiId")));
            materialInfoBean.setlDate(cursor.getLong(cursor.getColumnIndex("lDate")));
            materialInfoBean.setsItemId(cursor.getString(cursor.getColumnIndex("sItemId")));
            materialInfoBean.setItemType(cursor.getInt(cursor.getColumnIndex("itemType")));
            materialInfoBean.setiState(cursor.getInt(cursor.getColumnIndex("iState")));
            materialInfoBean.setsArticles(cursor.getString(cursor.getColumnIndex("sArticles")));
        }
        return materialInfoBean;
    }

    /**
     * 查询全部的素材数据
     *
     * @return 素材集合
     */
    public List<MaterialInfoBean> getListCustomerInfo() {
        List<MaterialInfoBean> materialInfoBeanList = new ArrayList<>();
        String sSql = "select * from MaterialInfoDAL order by lDate";
        Cursor cursor = dbHelper.rawQuery(sSql, new String[]{});
        if (cursor != null ) {
            while (cursor.moveToNext()){

                MaterialInfoBean materialInfoBean = new MaterialInfoBean();
                materialInfoBean.setlMiId(cursor.getLong(cursor.getColumnIndex("lMiId")));
                materialInfoBean.setlDate(cursor.getLong(cursor.getColumnIndex("lDate")));
                materialInfoBean.setsItemId(cursor.getString(cursor.getColumnIndex("sItemId")));
                materialInfoBean.setItemType(cursor.getInt(cursor.getColumnIndex("itemType")));
                materialInfoBean.setiState(cursor.getInt(cursor.getColumnIndex("iState")));
                materialInfoBean.setsArticles(cursor.getString(cursor.getColumnIndex("sArticles")));
                materialInfoBeanList.add(materialInfoBean);
            }
        }
        return materialInfoBeanList;
    }

}
