package com.livechat.chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.alibaba.fastjson.JSON;
import com.livechat.chat.adapter.FeedBackImageAdapter;
import com.livechat.chat.utils.BitmapCache;
import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;
import com.livechat.chat.widget.LoadingDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.ta.util.http.AsyncHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONObject;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * 意见反馈(填写)
 */
public class FeedBackThreeActivity extends BaseActivity {

    private GridView gvUploadPic;
    private EditText etFeedBackContent;

    private String sPhone = "", sEmail = "", moreImages = "", pathImage = ""; // 选择图片路径
    private Bitmap bmp; // 导入临时图片
    private ArrayList<HashMap<String, String>> imageItem = new ArrayList<>();
    //    private SimpleAdapter simpleAdapter; // 适配器
    private FeedBackImageAdapter feedBackImageAdapter;
    private LoadingDialog loadingDialog;
    private BitmapCache mCache = new BitmapCache();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_three);
        x.view().inject(this);
        initUI();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        etFeedBackContent = (EditText) this.findViewById(R.id.etFeedBackContent);
        gvUploadPic = (GridView) this.findViewById(R.id.gvUploadPic);

        Intent intent = this.getIntent();
        sPhone = intent.getStringExtra("Phone");
        sEmail = intent.getStringExtra("Email");

        feedBackImageAdapter = new FeedBackImageAdapter(this, mCache);
//        simpleAdapter = new SimpleAdapter(this, imageItem, R.layout.griditem_add_pic_layout, new String[]{"itemImage"},
//                new int[]{R.id.imageView1});
        gvUploadPic.setAdapter(feedBackImageAdapter);
        gvUploadPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (imageItem.size() == 4) { // 第一张为默认图片
                    CommonUtil.showTips(getApplicationContext(), R.mipmap.warning, "图片数4张已满！");
                } else if (position == imageItem.size()) { // 点击图片位置为+ 0对应0张图片
                    // 选择图片, 通过onResume()刷新数据
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, Constant.IMAGE_OPEN_CODE);
                } else {
                    dialog(position);
                }
            }
        });
    }

    /**
     * 获取图片路径 响应startActivityForResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 打开图片
        if (resultCode == RESULT_OK && requestCode == Constant.IMAGE_OPEN_CODE) {
            Uri uri = data.getData();
            if (!TextUtils.isEmpty(uri.getPath())) {
                // 查询选择图片
                Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                // 返回 没找到选择图片
                if (cursor == null) {
                    return;
                }
//                 光标移动至开头 获取图片路径
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
//                pathImage=uri.getPath();

//                moreImages += Constant.BASE_URL + pathImage + ",";

//                etFeedBackContent.setText(moreImages);
                mCache.putBitmap(pathImage, new CommonUtil().compressBySize(pathImage, 80, 80));
                updateAdapter();
            }
        }
    }

    /**
     * 刷新图片
     */
    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * 更新Adapter
     */
    public void updateAdapter() {
        if (!TextUtils.isEmpty(pathImage)) {
//            Bitmap addBmp = BitmapFactory.decodeFile(pathImage);
            HashMap<String, String> map = new HashMap<>();
            map.put("itemImage", pathImage);
//            map.put("itemImage", new CommonUtil().getDiskBitmap(pathImage));
            // map.put("itemImage", addBmp);
            imageItem.add(map);
            feedBackImageAdapter.addCity(imageItem);
            feedBackImageAdapter.notifyDataSetChanged();
            pathImage = null;
        }
    }

    /**
     * Dialog对话框提示用户删除操作 position为删除图片位置
     *
     * @param position
     */
    private void dialog(final int position) {
        new AlertDialog.Builder(this).setMessage("\n确认移除已添加图片吗？\n").setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                imageItem.remove(position);
//                simpleAdapter.notifyDataSetChanged();

                feedBackImageAdapter.addCity(imageItem);
                feedBackImageAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("保留", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 完成
     *
     * @param view
     */
    public void doBtnFinish(View view) {
        if (TextUtils.isEmpty(etFeedBackContent.getText().toString().trim())) {
            CommonUtil.showTips(this, R.mipmap.warning, "请输入您宝贵的问题和意见");
            return;
        }
        loadingDialog = new LoadingDialog(this, Constant.THEME_HOLO_LIGHT, "正在发送，请稍后...");
        loadingDialog.show();
        psot();
    }

    public void psot() {
        if (imageItem.size() > content) {
            PostHeadImage(imageItem.get(content).get("itemImage"));
        } else {
            postOpinion();
        }
    }

    /**
     * 意见提交
     */
    public void postOpinion() {
        String sFeedBackContent = etFeedBackContent.getText().toString().trim();
        if (TextUtils.isEmpty(sFeedBackContent)) {
            CommonUtil.showTips(this, R.mipmap.warning, "请输入您宝贵的问题和意见");
            return;
        }
        if (!CommonUtil.isNetworkAvailable(this)) {
            CommonUtil.showTips(this, R.mipmap.warning, "网络不可用");
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            return;
        }
        AsyncHttpClient httpClient = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("title", "意见反馈");
        params.put("content", sFeedBackContent);
        String from = "";// 手机号或者Email
        if (!"".equals(sEmail) && !"".equals(sPhone)) {
            from = sPhone;
        } else if (!"".equals(sPhone) && "".equals(sEmail)) {
            from = sPhone;
        } else if (!"".equals(sEmail) && "".equals(sPhone)) {
            from = sEmail;
        } else if ("".equals(sEmail) && "".equals(sPhone)) {
            return;
        }
        params.put("from", from);
        params.put("type", 1);
        params.put("appType", 3);
        params.put("images", moreImages);
        params.put("sign", CommonUtil.md5Encryption("0" + Constant.Token));
        httpClient.post(Constant.sCommitFeedbackUrl, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                try {
                    if (loadingDialog != null) {
                        loadingDialog.dismiss();
                    }

                    String sresponse = String.valueOf(response);
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(sresponse);
                    String msg = jsonObject.getString("message");
                    int code = jsonObject.getInteger("code");

                    if (statusCode == 200) {
                        if (code == 0) {
                            finish();
                            CommonUtil.showTips(FeedBackThreeActivity.this, R.mipmap.smile, msg);
                        } else {
                            CommonUtil.showTips(FeedBackThreeActivity.this, R.mipmap.warning, msg);
                        }
                    } else {
                        CommonUtil.showTips(FeedBackThreeActivity.this, R.mipmap.warning, "请求失败");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                CommonUtil.showTips(FeedBackThreeActivity.this, R.mipmap.warning, "请求失败");
            }
        });
    }

    int content = 0;

    /**
     * 图片上传
     *
     * @param pathImage 图片本地路径
     */
    public void PostHeadImage(String pathImage) {
        com.ta.util.http.AsyncHttpClient httpClient = new com.ta.util.http.AsyncHttpClient();
        com.ta.util.http.RequestParams params = new com.ta.util.http.RequestParams();
//        System.out.println(pathImage + "=======");
        try {
            params.put("image", new File(pathImage));
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpClient.post(Constant.sUploadImageUrl, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(String response) {
                super.onSuccess(response);
                try {
                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
                    String msg = jsonObject.getString("msg");
                    int code = jsonObject.getInteger("code");
                    if (code == 0) {
                        // 更新本地数据
                        jsonObject = JSON.parseObject(jsonObject.getString("data"));
                        if (moreImages.equals("")) {
                            moreImages = jsonObject.getString("url");
                        } else {
                            moreImages += "," + jsonObject.getString("url");
                        }
                        content += 1;
                        psot();
//                        doHeadImagePostServer(jsonObject.getString("url"));
                    } else {
                        CommonUtil.showTips(getApplicationContext(), R.mipmap.warning, msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                psot();
            }
        });
    }

}
