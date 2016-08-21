package com.livechat.chat.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.livechat.chat.PublicWebViewActivity;
import com.livechat.chat.R;
import com.livechat.chat.entity.Result;
import com.livechat.chat.widget.CircleImageView;
import com.livechat.chat.widget.LoadingDialog;
import com.livechat.chat.widget.RoundedRectImageView;
import com.livechat.chat.widget.TipsToast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.apache.http.Header;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 常用工具类
 * Created by Administrator on 2016/3/30.
 */
public class CommonUtil {

    private static Object mLock = new Object(); // 安全锁
    private static TipsToast tipsToast;
    private static final String CHARSET = "utf-8"; // 设置编码

    /**
     * 自定义吐司
     *
     * @param mContext  上下文
     * @param iconResId 资源对象
     * @param tips      提示信息
     */
    public static void showTips(final Context mContext, final int iconResId, final String tips) {
        synchronized (mLock) {
            if (tipsToast != null) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    tipsToast.cancel();
                }
            } else {
                tipsToast = TipsToast.makeText(mContext, tips, TipsToast.LENGTH_SHORT);
            }
            tipsToast.show();
            tipsToast.setIcon(iconResId);
            tipsToast.setText(tips);
        }
    }

    /**
     * 说明：验证 手机号,密码 是否正确
     *
     * @param value 要验证的值
     * @param name  验证类型 手机号：phone,密码：password
     * @return 是否匹配正确
     */
    public static boolean isVerification(final String value, final String name) {
        synchronized (mLock) {// 手机号
            if (name == "phone") {
                String phone = "^[1][3,4,5,7,8][0-9]{9}$";
                if (value.matches(phone)) {
                    return true;
                }
            }
            if (name == "password") {// 密码
                String password = "^[a-zA-Z_0-9@!#$%^&*.;:/{/}<>(),\" ]{6,16}$";
                if (value.matches(password)) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 说明：检查当前网络是否可用
     *
     * @param mContext 上下文
     * @return true为可用 false为不可用
     */
    public static boolean isNetworkAvailable(final Context mContext) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {// 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            int len = networkInfo.length;
            if (networkInfo != null && len > 0) {
                for (int i = 0; i < len; i++) {
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 使用MD5加密
     *
     * @param pwd 参入的加密参数
     * @return 加密的结果
     */
    public static String md5Encryption(final String pwd) {
        synchronized (mLock) {
            byte[] hash;
            try {
                hash = MessageDigest.getInstance("MD5").digest(pwd.getBytes("UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                if ((b & 0xFF) < 0x10) {
                    hex.append("0");
                }
                hex.append(Integer.toHexString(b & 0xFF));
            }
            return hex.toString().toUpperCase();
        }
    }

    /**
     * 当前时间
     *
     * @return 当前时间
     */
    @SuppressLint("SimpleDateFormat")
    public static String getNowTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmssSS");
        return dateFormat.format(date);
    }

    /**
     * 当前时间
     *
     * @return 当前时间
     */
    @SuppressLint("SimpleDateFormat")
    public static String getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd mm:ss");
        return dateFormat.format(date);
    }

    /**
     * 判断是否存在SD卡
     *
     * @return true;false;
     */
    public static boolean isExistsSdCard() {
        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 异步加载图片显示(显示图片的配置)
     *
     * @param sPicUrl   图片的路径
     * @param imageView 显示图片的控件名
     */
    public static void showImageLoader(final String sPicUrl, final ImageView imageView, final int resId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(resId)
                .showImageOnFail(resId)
                .showImageForEmptyUri(resId)
                .displayer(new RoundedBitmapDisplayer(5))
                .displayer(new FadeInBitmapDisplayer(100))
                .cacheInMemory(false)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
        ImageLoader.getInstance().displayImage(sPicUrl, imageView, options);
    }

    /**
     * 异步加载图片显示(显示图片的配置)
     *
     * @param sPicUrl   图片的路径
     * @param imageView 显示图片的控件名
     */
    public static void showImageLoader(final String sPicUrl, final CircleImageView imageView, final int resId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(resId)
                .showImageOnFail(resId)
                .showImageForEmptyUri(resId)
                .displayer(new RoundedBitmapDisplayer(5))
                .displayer(new FadeInBitmapDisplayer(100))
                .cacheInMemory(false)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
        ImageLoader.getInstance().displayImage(sPicUrl, imageView, options);
    }

    /**
     * 异步加载图片显示
     *
     * @param sPicUrl   图片的路径
     * @param imageView 显示图片的控件名
     * @param resId     显示默认的图片
     */
    public static void showImageLoader(final String sPicUrl, final RoundedRectImageView imageView, final int resId) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(resId)
                .showImageOnFail(resId)
                .showImageForEmptyUri(resId)
                .displayer(new RoundedBitmapDisplayer(5))
                .displayer(new FadeInBitmapDisplayer(100))
                .cacheInMemory(false)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)
                .build();
        ImageLoader.getInstance().displayImage(sPicUrl, imageView, options);
    }

    /**
     * 设置下拉的背景色
     *
     * @param view 控件
     */
    public static void setSwipeRefreshBg(final SwipeRefreshLayout view) {
        view.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    /**
     * 获取Assets目录下的文件内容
     *
     * @param mContext 上下文
     * @param fileName 文件名
     * @return 返回字符串值
     */
    public static String getAssetsResourceList(final Context mContext, final String fileName) {
        try {
            InputStream is = mContext.getResources().getAssets().open(fileName);
            InputStreamReader reader = new InputStreamReader(is);
            BufferedReader bufferedReader = new BufferedReader(reader);
            StringBuffer buffer = new StringBuffer("");
            String str;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
                buffer.append("\n");
            }
            return buffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 保存用户的账号到本地
     *
     * @param mContext    上下文
     * @param account     登录账号
     * @param password    登录密码
     * @param iLoginState 登录状态码
     */
    public static void saveUserLoginInfo(final Context mContext, final String account, final String password, final String iLoginState, final String ways) {
        SharedPreferences preferences = mContext.getSharedPreferences("com.livechat.chat", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("account", account);
        editor.putString("password", password);
        editor.putString("iLoginStateCode", iLoginState);
        editor.putString("OnWays", ways);
        editor.commit();
    }

    /**
     * 保存百度推送的ID
     *
     * @param mContext  上下文
     * @param channelId 百度推送的ID
     */
    public static void savePushChannelId(final Context mContext, final String channelId) {
        SharedPreferences preferences = mContext.getSharedPreferences("com.livechat.chat", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("channelId", channelId);
        editor.commit();
    }

    /**
     * 保存后台推送的itemId
     *
     * @param mContext 上下文
     * @param itemId   后台推送的ID, 用于获取素材时使用
     */
    public static void savePushItemId(final Context mContext, final String itemId) {
        SharedPreferences preferences = mContext.getSharedPreferences("com.livechat.chat", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("itemId", itemId);
        editor.commit();
    }

    /**
     * 获得用户登录的信息
     *
     * @param mContext 上下文
     * @param sign     标识获取那个值
     * @return 返回对应的值
     */
    public static String getUserLoginInfo(final Context mContext, final int sign) {
        SharedPreferences preferences = mContext.getSharedPreferences("com.livechat.chat", Context.MODE_PRIVATE);
        if (sign == Constant.ACCOUNT_CODE) {
            return preferences.getString("account", "");
        } else if (sign == Constant.PASSWORD_CODE) {
            return preferences.getString("password", "");
        } else if (sign == Constant.iLoginState_CODE) {// 0:未登录;1:已登陆
            return preferences.getString("iLoginStateCode", "");
        } else if (sign == Constant.OnWays_Code) {
            return preferences.getString("OnWays", "");// 使用的是那种登陆方式
        } else if (sign == Constant.ChannelId_Code) {
            return preferences.getString("channelId", "");// 得到保存的百度推送的ID
        } else if (sign == Constant.ItemId_Code) {
            return preferences.getString("itemId", "");
        }
        return null;
    }

    /**
     * 跳转到公共的WebView页面去显示Url地址
     *
     * @param mContext             上下文
     * @param whetherShowActionBar 是否显示ActionBar:true:显示;false:隐藏;
     * @param subTitle             副标题
     * @param masterTitle          主标题
     * @param otherTitle           其他标题
     * @param webViewUrl           Url地址
     */
    public static void doJumpToPublicWebView(final Context mContext, final boolean whetherShowActionBar, final String subTitle, final String masterTitle, final String otherTitle, final String webViewUrl) {
        Intent intent = new Intent(mContext, PublicWebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean("whetherShowActionBar", whetherShowActionBar);
        bundle.putString("subTitle", subTitle);
        bundle.putString("masterTitle", masterTitle);
        bundle.putString("otherTitle", otherTitle);
        bundle.putString("webViewUrl", webViewUrl);
        intent.putExtras(bundle);
        mContext.startActivity(intent);
    }

    /**
     * 上传图片到服务器
     *
     * @param mContext 上下文
     * @param path     要上传的文件路径
     * @param url      服务端接收URL
     */
    public static void uploadPicToServer(final Context mContext, final String path, final String url) {
        final LoadingDialog loadingDialog = new LoadingDialog(mContext, Constant.THEME_HOLO_LIGHT, "正在上传，请稍后...");
        loadingDialog.show();
        if (!CommonUtil.isNetworkAvailable(mContext)) {
            showTips(mContext, R.mipmap.warning, "网络不可用");
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            return;
        }
        try {
            final File file = new File(path);

            if (file.exists() && file.length() > 0) {


                new AsyncTask<String, Integer, String>() {

                    @Override
                    protected String doInBackground(String... params) {
                        Result result = uploadPicToServer(file, 120000);
                        return null;
                    }
                }.execute();


//                AsyncHttpClient httpClient = new AsyncHttpClient();
//                httpClient.setTimeout(Constant.DEFAULT_TIMEOUT);
//                RequestParams params = new RequestParams();
//                params.put("name", "image");
//                params.put("filename", file.getName());
////                params.put("charset", "utf-8");
//                // 上传文件
//                httpClient.post(url, params, new AsyncHttpResponseHandler() {
//
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                        try {
//                            if (statusCode == 200) {
//                                if (loadingDialog != null) {
//                                    loadingDialog.dismiss();
//                                }
//                                String str = new String(responseBody);
//                                System.out.print(str);
//                                showTips(mContext, R.mipmap.smile, "上传成功");
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable throwable) {
//                        if (loadingDialog != null) {
//                            loadingDialog.dismiss();
//                        } // 上传失败，请稍后再试
//                        showTips(mContext, R.mipmap.warning, "上传失败" + throwable.getStackTrace());
//                    }
//
//                    /**
//                     * 上传的进度变化
//                     * @param bytesWritten
//                     * @param totalSize
//                     */
//                    @Override
//                    public void onProgress(long bytesWritten, long totalSize) {
//                        super.onProgress(bytesWritten, totalSize);
//                    }
//
//                    /**
//                     * 返回重试次数
//                     * @param retryNo
//                     */
//                    @Override
//                    public void onRetry(int retryNo) {
//                        super.onRetry(retryNo);
//                    }
//
//                });
            } else {
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                showTips(mContext, R.mipmap.warning, "文件不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件
     *
     * @param mContext 上下文
     * @param url      要下载的文件URL
     */
    public static void downLoadPicFromServer(final Context mContext, final String url) {
        final LoadingDialog loadingDialog = new LoadingDialog(mContext, Constant.THEME_HOLO_LIGHT, "正在保存，请稍后...");
        loadingDialog.show();
        if (!CommonUtil.isNetworkAvailable(mContext)) {
            showTips(mContext, R.mipmap.warning, "网络不可用");
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            return;
        }
        try {
            AsyncHttpClient httpClient = new AsyncHttpClient();
            // 指定文件类型
            String[] allowedContentTypes = new String[]{"image/png", "image/jpeg"};
            // 获取二进制数据如图片和其他文件
            httpClient.get(url, new BinaryHttpResponseHandler(allowedContentTypes) {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                    if (statusCode == 200) {
                        String tempPath = Environment.getExternalStorageDirectory() + "/" + System.currentTimeMillis() + ".jpg";
                        Bitmap bmp = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                        File file = new File(tempPath);
                        // 压缩格式
                        Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
                        // 压缩比例
                        int quality = 100;
                        try {
                            // 若存在则删除
                            if (file.exists()) {
                                file.delete();
                            }
                            // 创建文件
                            file.createNewFile();
                            OutputStream stream = new FileOutputStream(file);
                            // 压缩输出
                            bmp.compress(format, quality, stream);
                            // 关闭
                            stream.close();
                            if (loadingDialog != null) {
                                loadingDialog.dismiss();
                            }
                            showTips(mContext, R.mipmap.smile, "保存成功");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable throwable) {
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }
                    showTips(mContext, R.mipmap.error, "保存失败，请稍后再试");
                }

                /**
                 * 下载的进度变化
                 * @param bytesWritten
                 * @param totalSize
                 */
                @Override
                public void onProgress(long bytesWritten, long totalSize) {
                    super.onProgress(bytesWritten, totalSize);
                }

                /**
                 * 返回重试次数
                 * @param retryNo
                 */
                @Override
                public void onRetry(int retryNo) {
                    super.onRetry(retryNo);
                }

            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证电子邮箱
     *
     * @param email 电子邮箱地址
     * @return
     */
    public static boolean isEmail(final String email) {
        return Pattern.matches(Constant.REGEX_EMAIL, email);
    }

    /**
     * 日期转换成字符串
     *
     * @param time
     * @return
     */
    public static String dateConversionString(final long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date = new Date(time);
        return sdf.format(date);
    }

    /**
     * 获取消息发送的时间，传入毫秒数
     *
     * @param dateTimeMills 时间
     * @return
     * @author Administrator 2015-4-13 下午3:30:16 <br>
     */
    public static String getHistorySendTime(long dateTimeMills) {
        String returnTime;
        long currentSecond = System.currentTimeMillis() / 1000;
        long passSecond = dateTimeMills / 1000;
        long delta = currentSecond - passSecond;
        if (delta < 60) {
            returnTime = "刚刚";
        } else if (delta < 60 * 60) {
            returnTime = delta / 60 + "分钟之前";
        } else if (delta < 60 * 60 * 24) {
            returnTime = delta / 60 / 60 + "小时之前";
        } else {
            returnTime = convertLongTimeToString_ForFragment(dateTimeMills);
        }
        return returnTime;
    }

    /**
     * 长整型日期转字符类型 ：66668516348-> 2009-02-14
     *
     * @param timeInMillis 时间
     * @return
     * @author Administrator 2015-4-13 下午3:29:33 <br>
     */
    @SuppressLint("SimpleDateFormat")
    public static String convertLongTimeToString_ForFragment(long timeInMillis) {
        String newTypeDate;
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);
        Date date = calendar.getTime();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        newTypeDate = f.format(date);
        return newTypeDate;
    }

    /**
     * 日期转换成字符串
     *
     * @param time
     * @return
     */
    public static String dateConversionStringTwo(final long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(time);
        return sdf.format(date);
    }

    /**
     * 获取本地（SD卡）图片
     *
     * @param pathString 图片路径（例如：sdcard0/1.jpg）
     * @return Bitmap
     * @author Administrator 2015-4-13 下午3:25:17 <br>
     */
    @SuppressWarnings("deprecation")
    public Bitmap getDiskBitmap(Context context, String pathString) {
        // Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inPreferredConfig = Bitmap.Config.RGB_565;
                opt.inInputShareable = true;
                opt.inPurgeable = true;
                return BitmapFactory.decodeFile(pathString, opt);
//                Bitmap image=BitmapFactory.decodeFile(pathString, null);
//                ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                image.compress(Bitmap.CompressFormat.JPEG, 50, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//                int options = 100;
////      Log.i(test,原始大小 + baos.toByteArray().length);
//                while (baos.toByteArray().length / 1024 > 80) { // 循环判断如果压缩后图片是否大于(maxkb)50kb,大于继续压缩
////          Log.i(test,压缩一次!);
//                    baos.reset();// 重置baos即清空baos
//                    options -= 20;// 每次都减少10
//                    image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
//                }
//                image.recycle();
////      Log.i(test,压缩后大小 + baos.toByteArray().length);
//                ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
//                Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, opt);// 把ByteArrayInputStream数据生成图片
//                return bitmap;
            } else {
                return readBitMap(context, R.mipmap.upload_pic);
            }
            // if (file != null) {
            // file = null;
            // }
        } catch (Exception e) {
            e.printStackTrace();
            return readBitMap(context, R.mipmap.upload_pic);
        }
        // return bitmap;
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     *
     * @param context
     * @param resId
     * @return Bitmap
     */
    @SuppressWarnings("deprecation")
    public Bitmap readBitMap(Context context, int resId) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        //opt.inPreferredConfig = Bitmap.Config.ALPHA_8; //RGB_565
        opt.inPreferredConfig = Bitmap.Config.ARGB_8888;//太低会图片变黑
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        // 获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is, null, opt);
    }

    /**
     * use to lessen pic 50%
     *
     * @param path sd card path
     * @return bitmap
     */
    public final Bitmap lessenUriImage(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_4444;
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); //此时返回 bm 为空
        options.inJustDecodeBounds = false; //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = (int) (options.outHeight / (float) 320);
        if (be <= 0)
            be = 1;
        options.inSampleSize = be; //重新读入图片，注意此时已经把 options.inJustDecodeBounds 设回 false 了
        bitmap = BitmapFactory.decodeFile(path, options);
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        System.out.println(w + " " + h); //after zoom
        return bitmap;
    }

    /**
     * 通过压缩图片的尺寸来压缩图片大小，仅仅做了缩小，如果图片本身小于目标大小，不做放大操作
     *
     * @param pathName     图片的完整路径
     * @param targetWidth  缩放的目标宽度
     * @param targetHeight 缩放的目标高度
     * @return 缩放后的图片
     */
    public Bitmap compressBySize(String pathName, int targetWidth, int targetHeight) {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = false;// 不去真的解析图片，只是获取图片的头部信息，包含宽高等；
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
        opts.inInputShareable = true;
        opts.inPurgeable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(pathName, opts);
        // 得到图片的宽度、高度；
        int imgWidth = opts.outWidth;
        int imgHeight = opts.outHeight;
        // 分别计算图片宽度、高度与目标宽度、高度的比例；取大于等于该比例的最小整数；
        int widthRatio = (int) Math.ceil(imgWidth / (float) targetWidth);
        int heightRatio = (int) Math.ceil(imgHeight / (float) targetHeight);
        if (widthRatio > 1 || heightRatio > 1) {
            if (widthRatio > heightRatio) {
                opts.inSampleSize = widthRatio;
            } else {
                opts.inSampleSize = heightRatio;
            }
        }
        // 设置好缩放比例后，加载图片进内容；
        opts.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(pathName, opts);
        return bitmap;
    }


    /**
     * 上传图片
     *
     * @param path
     */
    public static void uploadPic(final String path) {
        try {
            String end = "\r\n";
            String twoHyphens = "--";
            String boundary = "******";
            URL url = new URL(Constant.sUploadImageUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setChunkedStreamingMode(128 * 1024);// 128K
            // 允许输入输出流
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setUseCaches(false);
            // 使用POST方法
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            DataOutputStream dos = new DataOutputStream(httpURLConnection.getOutputStream());
            dos.writeBytes(twoHyphens + boundary + end);
            dos.writeBytes("Content-Disposition: form-data; name=\"uploadfile\"; filename=\"" + path.substring(path.lastIndexOf("/") + 1) + "\"" + end);
            dos.writeBytes(end);

            FileInputStream fis = new FileInputStream(path);
            byte[] buffer = new byte[8192]; // 8k
            int count;
            // 读取
            while ((count = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, count);
            }
            fis.close();
            dos.writeBytes(end);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + end);
            dos.flush();
            InputStream is = httpURLConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
//            String result = br.readLine();
            dos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取ApiKey
     *
     * @param context
     * @param metaKey
     * @return
     */
    public static String getMetaValue(Context context, String metaKey) {
        Bundle metaData = null;
        String apiKey = null;
        if (context == null || metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.getString(metaKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiKey;
    }

    /**
     * 上传图片到服务器
     *
     * @param file    文件的路径
     * @param timeOut 超时时间
     * @return
     */
    public static Result uploadPicToServer(File file, int timeOut) {
        String BOUNDARY = "----BoundarysvqkPIDyHpQroB0I";
        String PREFIX = "--", LINE_END = "\r\n";
        String CONTENT_TYPE = "multipart/form-data";
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(Constant.sUploadImageUrl).openConnection();
            conn.setReadTimeout(timeOut);
            conn.setConnectTimeout(timeOut);
            // 允许输入流跟输出流并且不能缓存
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", CHARSET);
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
            if (file.exists() && file.length() > 0) {
                // 获取流
                OutputStream outputSteam = conn.getOutputStream();
                DataOutputStream dos = new DataOutputStream(outputSteam);
                StringBuffer sb = new StringBuffer();
                sb.append(PREFIX);
                sb.append(BOUNDARY);
                sb.append(LINE_END);
                // name是我SpringMVC服务器里面的参数名,我设置成images了.
                // filename其实这个破玩意无所谓.可以随便写1个.因为服务器没用到这个字段
                // 但是拼接头文件还是得添加的.或者直接按照文件的名称上传也可以
                sb.append("Content-Disposition: form-data; name=\"image\"; filename=\"" + file.getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                dos.write(sb.toString().getBytes());

                InputStream is = new FileInputStream(file);
                byte[] bytes = new byte[1024];
                int len = 0;
                while ((len = is.read(bytes)) != -1) {
                    dos.write(bytes, 0, len);
                }
                is.close();
                dos.write(LINE_END.getBytes());
                byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
                dos.write(end_data);
                dos.flush();

                // 在这里读取HTTP返回的结果
                InputStream inputStream = conn.getInputStream();
                ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int l;
                while ((l = inputStream.read(buffer)) != -1) {
                    outStream.write(buffer, 0, l);
                }
                inputStream.close();
                // 因为我返回的是一个JSON.所以呢... 我封装了一下
                String json = new String(outStream.toByteArray());
                // 构建JSON数组对象
                Result result = JSON.parseObject(json, Result.class);
                return result;
            } else {
                throw new Exception("文件不能为空");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 是否第一次
     *
     * @param mContext
     * @param isOpen
     */
    public static void isOpen(final Context mContext, final boolean isOpen) {
        SharedPreferences preferences = mContext.getSharedPreferences("com.livechat.chat_open", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isOpen", isOpen);
        editor.commit();
    }

    /**
     * 取值
     *
     * @param mContext
     * @return
     */
    public static boolean getIsOpen(final Context mContext) {
        SharedPreferences preferences = mContext.getSharedPreferences("com.livechat.chat_open", Context.MODE_PRIVATE);
        return preferences.getBoolean("isOpen", false);
    }
    /**
     * 说明：检查当前网络是否可用 --> true为可用 false为不可用
     *
     * @param context
     * @return boolean
     */
    public boolean isNetworkUsable(Context context) {
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            int len = networkInfo.length;
            if (networkInfo != null && len > 0) {
                for (int i = 0; i < len; i++) {
                    // System.out.println(i + "===网络状态===" +
                    // networkInfo[i].getState());
                    // System.out.println(i + "===网络类型===" +
                    // networkInfo[i].getTypeName());
                    // 判断当前网络状态是否为连接状态
                    if (networkInfo[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
