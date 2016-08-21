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
 * Created by Administrator on 2016/6/10.
 */
public class InfoActPullDownAdapter extends BaseAdapter {

    private Context mContext;
    private List<EotherUrlBean> mUrlBeanList;
    private LayoutInflater mInflater;

    public InfoActPullDownAdapter(Context mContext) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void addInfoActItem(List<EotherUrlBean> mUrlBeanList) {
        this.mUrlBeanList = mUrlBeanList;
    }

    @Override
    public int getCount() {
        return mUrlBeanList == null ? 0 : mUrlBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mUrlBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.info_act_pulldown_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        EotherUrlBean eotherUrlBean = mUrlBeanList.get(position);
        if (eotherUrlBean != null) {
            CommonUtil.showImageLoader(eotherUrlBean.getsMenuIcon().equals("") ? "" : eotherUrlBean.getsMenuIcon(), viewHolder.ivInfoActIcon, R.mipmap.logo_icon);
            viewHolder.tvInfoAct.setText(eotherUrlBean.getsMenuName());
        }

        return convertView;
    }

    private class ViewHolder {

        private ImageView ivInfoActIcon;
        private TextView tvInfoAct;

        private ViewHolder(View convertView) {
            ivInfoActIcon = (ImageView) convertView.findViewById(R.id.ivInfoActIcon);
            tvInfoAct = (TextView) convertView.findViewById(R.id.tvInfoAct);
        }
    }

}
