package com.livechat.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.livechat.chat.R;
import com.livechat.chat.entity.CustomerBean;
import com.livechat.chat.utils.CommonUtil;

import java.util.List;

/**
 * Created by Administrator on 2016/6/23.
 */
public class SearchCustomerScAdapter extends BaseAdapter {

    private Context mContext;
    private List<CustomerBean> mCustomerBeanList;
    private LayoutInflater mInflater;

    public SearchCustomerScAdapter(Context mContext) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
    }

    public void addSearchCustomerSc(List<CustomerBean> mCustomerBeanList) {
        this.mCustomerBeanList = mCustomerBeanList;
    }

    @Override
    public int getCount() {
        return mCustomerBeanList == null ? 0 : mCustomerBeanList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCustomerBeanList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.search_customer_sc_item, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        CustomerBean customerBean = mCustomerBeanList.get(position);
        if (customerBean != null) {
            CommonUtil.showImageLoader(customerBean.getsHeaderImage().equals("") ? "" : customerBean.getsHeaderImage(), viewHolder.ivHeadPic, R.mipmap.head);
            viewHolder.tvUserAccount.setText(customerBean.getsNickname());
        }
        return convertView;
    }

    private class ViewHolder {

        private ImageView ivHeadPic;
        private TextView tvUserAccount;

        private ViewHolder(View convertView) {
            ivHeadPic = (ImageView) convertView.findViewById(R.id.ivHeadPic);
            tvUserAccount = (TextView) convertView.findViewById(R.id.tvUserAccount);
        }
    }

}
