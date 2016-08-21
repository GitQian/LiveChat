package com.livechat.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.livechat.chat.R;
import com.livechat.chat.entity.CustomerBean;
import com.livechat.chat.entity.InformationBean;
import com.livechat.chat.entity.MaterialInfoBean;
import com.livechat.chat.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 谈话列表的适配器
 * Created by Administrator on 2016/4/14.
 */
public class InfoActListAdapter extends BaseAdapter {

    private Context mContext;
    private List<MaterialInfoBean> mList;
    private LayoutInflater mInflater;
    private List<InformationBean> informationBeans;

    public InfoActListAdapter(Context mContext) {
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);
    }

    public void add(List<MaterialInfoBean> mList) {
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.infoact_list_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        informationBeans=new ArrayList<>();
        informationBeans= JSON.parseArray(mList.get(position).getsArticles(),InformationBean.class);
        // 头像
        CommonUtil.showImageLoader(informationBeans.get(0).getCoverPic().equals("") ? "" : informationBeans.get(0).getCoverPic(), viewHolder.coverPic, R.mipmap.head);
        viewHolder.title.setText(informationBeans.get(0).getTitle());
        return convertView;
    }

    private class ViewHolder {

        private ImageView coverPic;
        private TextView title;

        private ViewHolder(View view) {
            coverPic = (ImageView) view.findViewById(R.id.coverPic);
            title = (TextView) view.findViewById(R.id.title);
        }

    }

}
