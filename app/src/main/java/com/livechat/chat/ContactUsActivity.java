package com.livechat.chat;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 联系我们
 */
public class ContactUsActivity extends BaseActivity {

    private EditText etContent;
    private GridView gvUploadPic;

    private String pathImage = ""; // 选择图片路径
    private Bitmap bmp; // 导入临时图片
    private ArrayList<HashMap<String, Object>> imageItem;
    private SimpleAdapter simpleAdapter; // 适配器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        etContent = (EditText) this.findViewById(R.id.etContent);
        gvUploadPic = (GridView) this.findViewById(R.id.gvUploadPic);
        // 载入默认图片添加图片加号 通过适配器实现 SimpleAdapter参数imageItem为数据源
        bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.upload_pic2); // 加号
        imageItem = new ArrayList<>();
        HashMap<String, Object> map = new HashMap<>();
        map.put("itemImage", bmp);
        imageItem.add(map);
        simpleAdapter = new SimpleAdapter(this, imageItem, R.layout.griditem_add_pic_layout, new String[]{"itemImage"},
                new int[]{R.id.imageView1});
        /**
         * HashMap载入bmp图片在GridView中不显示,但是如果载入资源ID能显示 如 map.put("itemImage", R.drawable.img);
         * 解决方法: 1.自定义继承BaseAdapter实现;
         * 2.ViewBinder()接口实现.
         */
        simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Object data, String textRepresentation) {
                if (view instanceof ImageView && data instanceof Bitmap) {
                    ImageView i = (ImageView) view;
                    i.setImageBitmap((Bitmap) data);
                    return true;
                }
                return false;
            }
        });
        gvUploadPic.setAdapter(simpleAdapter);
        gvUploadPic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                if (imageItem.size() == 4) { // 第一张为默认图片
                    CommonUtil.showTips(getApplicationContext(), R.mipmap.warning, "图片数3张已满！");
                } else if (position == 0) { // 点击图片位置为+ 0对应0张图片
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
     * 发送
     *
     * @param view
     */
    public void doBtnSend(View view) {
        String sContent = etContent.getText().toString();
        if (TextUtils.isEmpty(sContent)) {
            Toast.makeText(this, "请输入您的问题", Toast.LENGTH_SHORT).show();
            return;
        }
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
            if (!TextUtils.isEmpty(uri.getAuthority())) {
                // 查询选择图片
                Cursor cursor = getContentResolver().query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                // 返回 没找到选择图片
                if (cursor == null) {
                    return;
                }
                // 光标移动至开头 获取图片路径
                cursor.moveToFirst();
                pathImage = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                System.out.print(pathImage);
            }
        }
    }

    /**
     * 刷新图片
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(pathImage)) {
            Bitmap addBmp = BitmapFactory.decodeFile(pathImage);
            HashMap<String, Object> map = new HashMap<>();
            map.put("itemImage", addBmp);
            imageItem.add(map);
            simpleAdapter = new SimpleAdapter(this, imageItem, R.layout.griditem_add_pic_layout, new String[]{"itemImage"},
                    new int[]{R.id.imageView1});
            simpleAdapter.setViewBinder(new SimpleAdapter.ViewBinder() {
                @Override
                public boolean setViewValue(View view, Object data, String textRepresentation) {
                    if (view instanceof ImageView && data instanceof Bitmap) {
                        ImageView i = (ImageView) view;
                        i.setImageBitmap((Bitmap) data);
                        return true;
                    }
                    return false;
                }
            });
            gvUploadPic.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
            // 刷新后释放防止手机休眠后自动添加
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
                simpleAdapter.notifyDataSetChanged();
            }
        }).setNegativeButton("保留", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

}
