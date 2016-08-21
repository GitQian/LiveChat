package com.livechat.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.livechat.chat.service.UserLoginInfoService;
import com.livechat.chat.utils.BitmapCache;
import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;
import com.livechat.chat.widget.LoadingDialog;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;
import com.ta.util.http.RequestParams;

import java.io.File;

/**
 * 设置头像
 */
public class SetHeadPicActivity extends BaseActivity {

    private ImageView ivHeadPic;

    private String imageName = "", sPath = Environment.getExternalStorageDirectory() + "/livechat/head/";
    private LoadingDialog loadingDialog;
    private UserLoginInfoService loginInfoService;
    private PopupWindow popupWindow = null;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_head_pic);
        mQueue = Volley.newRequestQueue(this);
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());

        initUI();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        loginInfoService = new UserLoginInfoService(this);
        ivHeadPic = (ImageView) this.findViewById(R.id.ivHeadPic);

        String sHeadPicUrl = this.getIntent().getStringExtra("sHeadPicUrl");
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(ivHeadPic, 0, R.mipmap.head);
        mImageLoader.get(sHeadPicUrl, listener);
    }

    /**
     * 设置头像
     *
     * @param view
     */
    public void doSetHeadPic(View view) {
        ShowPopupWindow(view);
    }

    private void ShowPopupWindow(View parent) {
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.set_head_pic_layout, null);
            popupWindow = new PopupWindow(view, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

            final RelativeLayout rlMainPopu = (RelativeLayout) view.findViewById(R.id.rlMainPopu);
            final Button btnCamera = (Button) view.findViewById(R.id.btnCamera);
            final Button btnLocalhost = (Button) view.findViewById(R.id.btnLocalhost);
            final Button btnSavePic = (Button) view.findViewById(R.id.btnSavePic);
            final Button btnCancle = (Button) view.findViewById(R.id.btnCancle);

            // 点击外面消失
            rlMainPopu.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            // 拍照
            btnCamera.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    imageName = CommonUtil.getNowTime() + ".png";
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    // 指定调用相机拍照后照片的储存路径
                    File file = new File(sPath, imageName);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, Constant.PHOTO_CAMERA_CODE);
                }
            });
            // 从本地选择
            btnLocalhost.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    imageName = CommonUtil.getNowTime() + ".png";
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, Constant.PHOTO_LOCALHOST_CODE);
                }
            });
            // 保存图片
            btnSavePic.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                    CommonUtil.downLoadPicFromServer(SetHeadPicActivity.this, "");
                }
            });
            // 取消
            btnCancle.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setAnimationStyle(R.style.customAnimStyle);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2 - popupWindow.getWidth() / 2;
        popupWindow.showAsDropDown(parent, xPos, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Constant.PHOTO_CAMERA_CODE:
                    if (CommonUtil.isExistsSdCard()) {
                        File file = new File(sPath, imageName);
                        startPhotoZoom(Uri.fromFile(file), 480);
                    } else {
                        CommonUtil.showTips(this, R.mipmap.warning, "未找到存储卡，无法保存图片");
                    }
                    break;
                case Constant.PHOTO_LOCALHOST_CODE:
                    if (data != null) {
                        startPhotoZoom(data.getData(), 480);
                    }
                    break;
                case Constant.PHOTO_CROP_PICTURE:
                    // path = /storage/emulated/0/livechat/head/050614373846.png
                    final String path = sPath + imageName;
                    // 设置显示图片
                    Bitmap bitmap = BitmapFactory.decodeFile(sPath + imageName);
                    PostHeadImage(path);
                    ivHeadPic.setImageBitmap(bitmap);
                    break;
            }
        }
    }

    /**
     * 头像-----图片上传
     *
     * @param pathImage 图片本地路径
     */
    public void PostHeadImage(String pathImage) {
        loadingDialog = new LoadingDialog(this, Constant.THEME_HOLO_LIGHT, "正在上传图片，请稍后...");
        loadingDialog.show();
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        try {
            params.put("image", new File(pathImage));
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpClient.post(Constant.sUploadImageUrl, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                try {
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
                    String msg = jsonObject.getString("msg");
                    int code = jsonObject.getInteger("code");
                    if (code == 0) {
                        // 更新本地数据
                        jsonObject = JSON.parseObject(jsonObject.getString("data"));
                        doHeadImagePostServer(jsonObject.getString("url"));
                    } else {
                        CommonUtil.showTips(SetHeadPicActivity.this, R.mipmap.warning, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                CommonUtil.showTips(SetHeadPicActivity.this, R.mipmap.warning, "请求失败");
            }
        });
    }

    /**
     * 提交数据到服务器
     *
     * @param imageName 提交的数据
     */
    private void doHeadImagePostServer(final String imageName) {
        if (TextUtils.isEmpty(imageName)) {
            CommonUtil.showTips(this, R.mipmap.warning, "图片错误");
            return;
        }
        loadingDialog = new LoadingDialog(this, Constant.THEME_HOLO_LIGHT, "正在更换，请稍后...");
        loadingDialog.show();
        if (!CommonUtil.isNetworkAvailable(this)) {
            CommonUtil.showTips(this, R.mipmap.warning, "网络不可用");
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            return;
        }
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        final String account = App.getInstance().getsUserLoginId();
        params.put("account", account);
        params.put("sign", CommonUtil.md5Encryption(account + Constant.Token));// 签名(md5(account + token))
        params.put("headerImage", imageName);
        httpClient.post(Constant.sUpdateUserInfoUrl, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                try {
                    String sresponse = String.valueOf(response);
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(sresponse);
                    String msg = jsonObject.getString("message");
                    int code = jsonObject.getInteger("code");
                    if (code == 0) {
                        // 更新本地数据
                        loginInfoService.updateUsersHeaderImage(imageName, account);
                        CommonUtil.showTips(SetHeadPicActivity.this, R.mipmap.smile, msg);
                    } else {
                        CommonUtil.showTips(SetHeadPicActivity.this, R.mipmap.warning, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                CommonUtil.showTips(SetHeadPicActivity.this, R.mipmap.warning, "请求失败");
            }
        });
    }

    /**
     * 开始截图
     *
     * @param uri1
     * @param size
     */
    @SuppressLint("SdCardPath")
    private void startPhotoZoom(Uri uri1, int size) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri1, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", false);
        // 保存路径
        File file = new File(sPath, imageName);
        // 创建父目录
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, Constant.PHOTO_CROP_PICTURE);
    }

}
