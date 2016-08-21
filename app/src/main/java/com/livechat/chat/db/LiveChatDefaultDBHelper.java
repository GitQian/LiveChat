package com.livechat.chat.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.livechat.chat.App;

/**
 * LiveChat数据库
 * Created by Administrator on 2016/3/25.
 */
public class LiveChatDefaultDBHelper extends SQLiteOpenHelper {

    private static SQLiteDatabase mSQLiteDatabase = null;// 定义操作数据库对像
    private static final int DATABASE_VERSION = 1;// 数据库版本号
    private static LiveChatDefaultDBHelper instance;// 定义实例
    // 创建数据表[用户信息表/客服信息表/素材信息表]
    private String sSqlUserLoginInfoDAL = "create table if not exists UserLoginInfoDAL(lid integer primary key autoincrement, sUserId varchar, sAccount varchar, sPassword varchar, sNickname varchar, sBirthday varchar, iGender integer, sAge varchar, sHeaderImage varchar, sEmail varchar, sPhone varchar, sAddress varchar, sSignature varchar, sRongToken varchar, iStatus integer, sIp varchar, sCreateDay varchar, iAppType integer, iOnlineStatus integer, sLastLogin varchar)";
    private String sSqlCustomerInfoDAL = "create table if not exists CustomerInfoDAL(lId integer primary key autoincrement, sAccount varchar, sNickname varchar, sHeaderImage varchar, sRongToken varchar, iOnlineStatus integer)";
    private String sSqlMaterialInfoDAL = "create table if not exists MaterialInfoDAL(lMiId integer primary key autoincrement, lDate integer, sItemId varchar, itemType integer, iState integer, sArticles varchar)";

    /**
     * 实例化数据库
     *
     * @param mContext
     * @return
     */
    public static LiveChatDefaultDBHelper getInstance(Context mContext) {
        if (instance == null) {
            instance = new LiveChatDefaultDBHelper(mContext);
        }
        return instance;
    }

    /**
     * 以用户登录的ID来命名数据库的名称
     *
     * @return LiveChat[1005].db
     */
    private static String getUserDatabaseName() {
        return "LiveChat[" + App.getInstance().getsUserLoginId() + "].db";
    }

    /**
     * 区分每个用户的数据库
     *
     * @param context
     */
    public LiveChatDefaultDBHelper(Context context) {
        super(context, getUserDatabaseName(), null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sSqlUserLoginInfoDAL);
        db.execSQL(sSqlCustomerInfoDAL);
        db.execSQL(sSqlMaterialInfoDAL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 连接数据库.
     *
     * @return 0成功;-1失败
     */
    public int connectDB() {
        if (mSQLiteDatabase != null) {
            mSQLiteDatabase.close();
            mSQLiteDatabase = null;
        }
        int iRet = -1;
        try {
            mSQLiteDatabase = this.getWritableDatabase();
            if (mSQLiteDatabase.isOpen()) {
                iRet = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iRet;
    }

    /**
     * 关闭数据库.
     */
    public void closeDB() {
        if (mSQLiteDatabase != null) {
            mSQLiteDatabase.close();
            mSQLiteDatabase = null;
        }
    }

    /**
     * 执行SQL语句.
     *
     * @param sql   SQL语句
     * @param oArgs 参数Object数组
     * @return 0成功;-1失败
     */
    public int execSql(String sql, Object[] oArgs) {
        int iRet = -1;
        try {
            if (mSQLiteDatabase == null) {
                mSQLiteDatabase = this.getWritableDatabase();
            }
            if (mSQLiteDatabase.isOpen()) {
                mSQLiteDatabase.execSQL(sql, oArgs);
                iRet = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return iRet;
    }

    /**
     * 查询返回结果Cursor.
     *
     * @param sql   SQL语句
     * @param sArgs 参数String数组
     * @return 非null成功;null失败
     */
    public Cursor rawQuery(String sql, String[] sArgs) {
        Cursor cursor = null;
        try {
            if (mSQLiteDatabase == null) {
                mSQLiteDatabase = this.getReadableDatabase();
            }
            if (mSQLiteDatabase.isOpen()) {
                cursor = mSQLiteDatabase.rawQuery(sql, sArgs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cursor;
    }

}
