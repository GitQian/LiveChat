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

import java.util.List;

/**
 * 城市适配器
 * Created by Administrator on 2016/5/7.
 */
public class CityAdapter extends BaseAdapter {

    private Context mContext;
    private List<CountriesBean> mList;
    private LayoutInflater mInflater;
    private String mCityStr;

    public CityAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }

    public void selected(String mCityStr) {
        this.mCityStr = mCityStr;
    }

    public void addCity(List<CountriesBean> list) {
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
            convertView = mInflater.inflate(R.layout.countries_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.tvSelected.setVisibility(View.GONE);
            viewHolder.ivPoint.setVisibility(View.GONE);
        }

        CountriesBean countriesBean = mList.get(position);
        if (countriesBean != null) {
            viewHolder.tvCountry.setText(countriesBean.getsName());
            if ((countriesBean.getsName()).equals(mCityStr)) {
                viewHolder.tvSelected.setVisibility(View.VISIBLE);
                viewHolder.ivPoint.setVisibility(View.VISIBLE);
            }
        }
        return convertView;
    }

    private class ViewHolder {

        private TextView tvCountry;
        private TextView tvSelected;
        private ImageView ivPoint;

        private ViewHolder(View view) {
            tvCountry = (TextView) view.findViewById(R.id.tvCountry);
            tvSelected = (TextView) view.findViewById(R.id.tvSelected);
            ivPoint = (ImageView) view.findViewById(R.id.ivPoint);
        }
    }

}
