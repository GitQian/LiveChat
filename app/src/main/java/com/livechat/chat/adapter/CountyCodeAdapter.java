package com.livechat.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.livechat.chat.R;
import com.livechat.chat.entity.CountryBean;

import java.util.List;

/**
 * 国家地区的适配器
 * Created by Administrator on 2016/3/30.
 */
public class CountyCodeAdapter extends BaseAdapter {

    private Context mContext;
    private List<CountryBean> mList;
    private LayoutInflater mInflater;

    public CountyCodeAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void addData(List<CountryBean> list) {
        this.mList = list;
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
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.county_code_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        CountryBean bean = mList.get(position);
        viewHolder.tvCountry.setText(bean.getCountry());
        viewHolder.tvCode.setText("+" + bean.getCode());
        return convertView;
    }

    private class ViewHolder {

        public TextView tvCountry;
        public TextView tvCode;

        public ViewHolder(View view) {
            tvCountry = (TextView) view.findViewById(R.id.tvCountry);
            tvCode = (TextView) view.findViewById(R.id.tvCode);
        }
    }

}
