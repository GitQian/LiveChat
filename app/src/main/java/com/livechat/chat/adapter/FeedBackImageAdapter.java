package com.livechat.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.livechat.chat.R;
import com.livechat.chat.entity.CountriesBean;
import com.livechat.chat.utils.BitmapCache;
import com.livechat.chat.utils.CommonUtil;

import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 意见反馈图片适配器
 * Created by Administrator on 2016/5/7.
 */
public class FeedBackImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<HashMap<String, String>> mList=new ArrayList<>();
    private LayoutInflater mInflater;
    private CommonUtil commonUtil=new CommonUtil();
    private BitmapCache cache;

    public FeedBackImageAdapter(Context context,BitmapCache cache) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.cache=cache;
    }

    public void addCity(List<HashMap<String, String>> list) {
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 1 : mList.size()+1;
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.griditem_add_pic_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if((mList.size())==position||mList.size()==0){
            viewHolder.imageView1.setImageBitmap(commonUtil.readBitMap(mContext,R.mipmap.upload_pic));
        }else{
            if(cache.getBitmap(mList.get(position).get("itemImage"))!=null){
                viewHolder.imageView1.setImageBitmap(cache.getBitmap(mList.get(position).get("itemImage")));
            }
//            viewHolder.imageView1.setImageBitmap(commonUtil.readBitMap(mContext, R.mipmap.upload_pic));
//            x.image().bind(viewHolder.imageView1, "file:////"+mList.get(position).get("itemImage"));
//            viewHolder.imageView1.setImageBitmap(commonUtil.getDiskBitmap(mContext,mList.get(position).get("itemImage")));
        }
        return convertView;
    }

    private class ViewHolder {

        private ImageView imageView1;

        private ViewHolder(View view) {
            imageView1 = (ImageView) view.findViewById(R.id.imageView1);
        }
    }

}
