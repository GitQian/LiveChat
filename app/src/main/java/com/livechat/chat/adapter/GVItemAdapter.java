package com.livechat.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.livechat.chat.R;
import com.livechat.chat.entity.EotherUrlBean;
import com.livechat.chat.utils.CommonUtil;

import java.util.List;

/**
 * 自定义每一项的适配器
 * Created by Administrator on 2016/3/22.
 */
public class GVItemAdapter extends BaseAdapter {

    private Context mContext;
    private List<EotherUrlBean> mlist;
    private LayoutInflater mInflater;

    public GVItemAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void addItem(List<EotherUrlBean> list) {
        this.mlist = list;
    }

    @Override
    public int getCount() {
        return mlist == null ? 0 : mlist.size();
    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.gv_item_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // 设置图标和标题
        EotherUrlBean eotherUrlBean = mlist.get(position);
        if (eotherUrlBean != null) {
            CommonUtil.showImageLoader(eotherUrlBean.getsMenuIcon().equals("") ? "" : eotherUrlBean.getsMenuIcon(), viewHolder.ivItemIcon, 0);
            viewHolder.tvDeposit.setText(eotherUrlBean.getsMenuName());
        }
        return convertView;
    }

    private class ViewHolder {

        public ImageView ivItemIcon;// 图标
        public TextView tvDeposit;// 图标名称

        private ViewHolder(View view) {
            ivItemIcon = (ImageView) view.findViewById(R.id.ivItemIcon);
            tvDeposit = (TextView) view.findViewById(R.id.tvDeposit);
        }
    }

}
