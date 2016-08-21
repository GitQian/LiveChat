package com.livechat.chat.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.livechat.chat.AboutActivity;
import com.livechat.chat.R;
import com.livechat.chat.entity.CountriesBean;
import com.livechat.chat.entity.EotherUrlBean;
import com.livechat.chat.fragment.InfoActFragment;
import com.livechat.chat.utils.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 菜单适配器
 * Created by Administrator on 2016/5/7.
 */
public class MemberAdapter extends BaseAdapter {

    private Context mContext;
    private List<EotherUrlBean> mList=new ArrayList<>();
    private LayoutInflater mInflater;
    private String mCityStr;
    private PopupWindow popupWindow;
    private InfoActFragment info;

    public MemberAdapter(Context context,PopupWindow popupWindow,InfoActFragment info) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.popupWindow=popupWindow;
        this.info=info;
    }

    public void selected(String mCityStr) {
        this.mCityStr = mCityStr;
    }

    public void addCity(List<EotherUrlBean> list) {
        if (!(list==null))
            this.mList = list;
    }

    @Override
    public int getCount() {
        return (mList.size()+2)/3;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_menu, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.relative_menu2.setVisibility(View.INVISIBLE);
        viewHolder.relative_menu3.setVisibility(View.INVISIBLE);
        viewHolder.view_1.setVisibility(View.INVISIBLE);
        viewHolder.view_2.setVisibility(View.INVISIBLE);

        EotherUrlBean countriesBean = mList.get(position);
        viewHolder.text_v1.setText(mList.get(position*3).getsMenuName());
        CommonUtil.showImageLoader(mList.get(position * 3).getsMenuIcon().equals("") ? "" : mList.get(position * 3).getsMenuIcon(), viewHolder.image_veiw1, R.mipmap.logo_icon);
        if(mList.size()>(position*3+1)){
            viewHolder.text_v2.setText(mList.get(position*3+1).getsMenuName());
            CommonUtil.showImageLoader(mList.get(position*3+1).getsMenuIcon().equals("") ? "" : mList.get(position*3+1).getsMenuIcon(), viewHolder.image_veiw2, R.mipmap.logo_icon);
            viewHolder.relative_menu2.setVisibility(View.VISIBLE);
//            viewHolder.view_1.setVisibility(View.VISIBLE);
        }
        if(mList.size()>(position*3+2)){
            viewHolder.text_v3.setText(mList.get(position*3+2).getsMenuName());
            CommonUtil.showImageLoader(mList.get(position * 3 +2).getsMenuIcon().equals("") ? "" : mList.get(position * 3 + 2).getsMenuIcon(), viewHolder.image_veiw3, R.mipmap.logo_icon);
            viewHolder.relative_menu3.setVisibility(View.VISIBLE);
//            viewHolder.view_2.setVisibility(View.VISIBLE);
        }


        viewHolder.relative_menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.doJumpToPublicWebView(mContext, true, "", "", "", mList.get(position * 3).getsMenuUrl());
                popupWindow.dismiss();
                info.upDrop(1);
            }
        });
        viewHolder.relative_menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.doJumpToPublicWebView(mContext, true, "", "", "", mList.get(position * 3+1).getsMenuUrl());
                popupWindow.dismiss();
                info.upDrop(1);
            }
        });
        viewHolder.relative_menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonUtil.doJumpToPublicWebView(mContext, true, "", "", "", mList.get(position * 3 + 2).getsMenuUrl());
                popupWindow.dismiss();
                info.upDrop(1);
            }
        });


        return convertView;
    }

    private class ViewHolder {

        private RelativeLayout relative_menu1;
        private RelativeLayout relative_menu2;
        private RelativeLayout relative_menu3;
        private ImageView image_veiw1;
        private ImageView image_veiw2;
        private ImageView image_veiw3;
        private TextView text_v1;
        private TextView text_v2;
        private TextView text_v3;
        private View view_1;
        private View view_2;

        private ViewHolder(View view) {
            relative_menu1 = (RelativeLayout) view.findViewById(R.id.relative_menu1);
            relative_menu2 = (RelativeLayout) view.findViewById(R.id.relative_menu2);
            relative_menu3 = (RelativeLayout) view.findViewById(R.id.relative_menu3);
            image_veiw1 = (ImageView) view.findViewById(R.id.image_veiw1);
            image_veiw2 = (ImageView) view.findViewById(R.id.image_veiw2);
            image_veiw3 = (ImageView) view.findViewById(R.id.image_veiw3);
            text_v1 = (TextView) view.findViewById(R.id.text_v1);
            text_v2 = (TextView) view.findViewById(R.id.text_v2);
            text_v3 = (TextView) view.findViewById(R.id.text_v3);
            view_1=(View)view.findViewById(R.id.view_1);
            view_2=(View)view.findViewById(R.id.view_2);
        }
    }

}
