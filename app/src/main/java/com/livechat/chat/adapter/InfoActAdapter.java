package com.livechat.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.livechat.chat.R;
import com.livechat.chat.entity.InformationBean;
import com.livechat.chat.entity.MaterialInfoBean;
import com.livechat.chat.utils.BitmapCache;
import com.livechat.chat.utils.CommonUtil;

import java.util.List;

/**
 * 资讯适配器
 * Created by Administrator on 2016/3/25.
 */
public class InfoActAdapter extends BaseAdapter implements View.OnClickListener {

    private Context mContext;
    private List<MaterialInfoBean> mList;
    private LayoutInflater mInflater;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    public InfoActAdapter(Context context,RequestQueue mQueue) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mQueue=mQueue;
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
    }

    public void addInfoData(List<MaterialInfoBean> list) {
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
            convertView = mInflater.inflate(R.layout.info_act_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.rlAlpha.setAlpha(0.5f);
//        JSONArray jsonArray;

        final List<InformationBean> informationBeans=JSON.parseArray(mList.get(position).getsArticles(),InformationBean.class);
        // 素材对象
//        if (materialInfoBean != null) {
//            viewHolder.tvUpdateTime.setText(CommonUtil.dateConversionStringTwo(materialInfoBean.getlDate()));
//            String sArticles = materialInfoBean.getsArticles();
//
//            jsonArray = JSON.parseArray(sArticles);
//            if (jsonArray != null && jsonArray.size() > 0) {
//                for (int i = 0; i < jsonArray.size(); i++) {
//                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                    String author = jsonObject.getString("author");
//                    String articleId = jsonObject.getString("articleId");
//                    String digest = jsonObject.getString("digest");
//                    String contentSourceUrl = jsonObject.getString("contentSourceUrl");
//                    String coverPic = jsonObject.getString("coverPic");
//                    String title = jsonObject.getString("title");
//                    String content = jsonObject.getString("content");
//
//                    CommonUtil.showImageLoader(coverPic, viewHolder.ivInfoBg, R.mipmap.info_bg);
//                    viewHolder.tvInfoTitle.setText(title);
//                }
//            }
//        }
        viewHolder.tvUpdateTime.setText(CommonUtil.dateConversionStringTwo(mList.get(position).getlDate()));
        viewHolder.rlItemTitle1.setVisibility(View.GONE);
        viewHolder.rlItemTitle2.setVisibility(View.GONE);
        viewHolder.rlItemTitle3.setVisibility(View.GONE);
        viewHolder.rlItemTitle4.setVisibility(View.GONE);


        ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewHolder.ivInfoBg, 0, R.mipmap.customer_service);
        ImageLoader.ImageListener listener1 = ImageLoader.getImageListener(viewHolder.ivItemTitle1, 0, R.mipmap.customer_service);
        ImageLoader.ImageListener listener2 = ImageLoader.getImageListener(viewHolder.ivItemTitle2, 0, R.mipmap.customer_service);
        ImageLoader.ImageListener listener3 = ImageLoader.getImageListener(viewHolder.ivItemTitle3, 0, R.mipmap.customer_service);
        ImageLoader.ImageListener listener4 = ImageLoader.getImageListener(viewHolder.ivItemTitle4, 0, R.mipmap.customer_service);
        if(informationBeans.size()>0){ // 利用Volley加载图片
            mImageLoader.get(informationBeans.get(0).getCoverPic(), listener);
//            CommonUtil.showImageLoader(informationBeans.get(0).getCoverPic(), viewHolder.ivInfoBg, R.mipmap.info_bg);
            viewHolder.tvInfoTitle.setText(informationBeans.get(0).getTitle());
        }
        if(informationBeans.size()>1){
            mImageLoader.get(informationBeans.get(1).getCoverPic(), listener1);
            viewHolder.rlItemTitle1.setVisibility(View.VISIBLE);
//            CommonUtil.showImageLoader(informationBeans.get(1).getCoverPic(), viewHolder.ivItemTitle1, R.mipmap.info_bg);
            viewHolder.tvItemTitle1.setText(informationBeans.get(1).getTitle());
        }
        if(informationBeans.size()>2){
            mImageLoader.get(informationBeans.get(2).getCoverPic(), listener2);
            viewHolder.rlItemTitle2.setVisibility(View.VISIBLE);
//            CommonUtil.showImageLoader(informationBeans.get(2).getCoverPic(), viewHolder.ivItemTitle2, R.mipmap.info_bg);
            viewHolder.tvItemTitle2.setText(informationBeans.get(2).getTitle());
        }
        if(informationBeans.size()>3){
            mImageLoader.get(informationBeans.get(3).getCoverPic(), listener3);
            viewHolder.rlItemTitle3.setVisibility(View.VISIBLE);
//            CommonUtil.showImageLoader(informationBeans.get(3).getCoverPic(), viewHolder.ivItemTitle3, R.mipmap.info_bg);
            viewHolder.tvItemTitle3.setText(informationBeans.get(3).getTitle());
        }
        if(informationBeans.size()>4){
            mImageLoader.get(informationBeans.get(4).getCoverPic(), listener4);
            viewHolder.rlItemTitle4.setVisibility(View.VISIBLE);
//            CommonUtil.showImageLoader(informationBeans.get(4).getCoverPic(), viewHolder.ivItemTitle4, R.mipmap.info_bg);
            viewHolder.tvItemTitle4.setText(informationBeans.get(4).getTitle());
        }


        viewHolder.flTitleTop.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CommonUtil.doJumpToPublicWebView(mContext, true, "", "资讯", "", informationBeans.get(0).getContent());
            }
        });
        viewHolder.rlItemTitle1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CommonUtil.doJumpToPublicWebView(mContext, true, "", "资讯", "", informationBeans.get(1).getContent());
            }
        });
        viewHolder.rlItemTitle2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CommonUtil.doJumpToPublicWebView(mContext, true, "", "资讯", "", informationBeans.get(2).getContent());
            }
        });
        viewHolder.rlItemTitle3.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CommonUtil.doJumpToPublicWebView(mContext, true, "", "资讯", "", informationBeans.get(3).getContent());
            }
        });
        viewHolder.rlItemTitle4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                CommonUtil.doJumpToPublicWebView(mContext, true, "", "资讯", "", informationBeans.get(4).getContent());
            }
        });

        // 其他标题和线条
//        viewHolder.view1.setVisibility(View.VISIBLE);
//        viewHolder.rlItemTitle1.setVisibility(View.VISIBLE);
//        viewHolder.view2.setVisibility(View.VISIBLE);
//        viewHolder.rlItemTitle2.setVisibility(View.VISIBLE);
//        viewHolder.view3.setVisibility(View.VISIBLE);
//        viewHolder.rlItemTitle3.setVisibility(View.VISIBLE);
//        viewHolder.view4.setVisibility(View.VISIBLE);
//        viewHolder.rlItemTitle4.setVisibility(View.VISIBLE);

        return convertView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flTitleTop:// 图片标题
                CommonUtil.doJumpToPublicWebView(mContext, true, "资讯", "", "", "http://www.baidu.com/");
                break;
            case R.id.rlItemTitle1:// 内容标题1
                CommonUtil.doJumpToPublicWebView(mContext, true, "资讯", "", "", "http://www.baidu.com/");
                break;
            case R.id.rlItemTitle2:// 内容标题2
                CommonUtil.doJumpToPublicWebView(mContext, true, "资讯", "", "", "http://www.baidu.com/");
                break;
            case R.id.rlItemTitle3:// 内容标题3
                CommonUtil.doJumpToPublicWebView(mContext, true, "资讯", "", "", "http://www.baidu.com/");
                break;
            case R.id.rlItemTitle4:// 内容标题4
                CommonUtil.doJumpToPublicWebView(mContext, true, "资讯", "", "", "http://www.baidu.com/");
                break;
        }
    }

    private class ViewHolder {

        private LinearLayout ll;
        public FrameLayout flTitleTop;
        public RelativeLayout rlAlpha, rlItemTitle1, rlItemTitle2, rlItemTitle3, rlItemTitle4;
        public ImageView ivInfoBg, ivItemTitle1, ivItemTitle2, ivItemTitle3, ivItemTitle4;
        public TextView tvUpdateTime, tvInfoTitle, tvItemTitle1, tvItemTitle2, tvItemTitle3, tvItemTitle4;
        private View view1, view2, view3, view4;

        public ViewHolder(View view) {
            ll = (LinearLayout) view.findViewById(R.id.ll);
            flTitleTop = (FrameLayout) view.findViewById(R.id.flTitleTop);
            rlAlpha = (RelativeLayout) view.findViewById(R.id.rlAlpha);
            ivInfoBg = (ImageView) view.findViewById(R.id.ivInfoBg);// 图片
            tvUpdateTime = (TextView) view.findViewById(R.id.tvUpdateTime);// 更新的时间
            tvInfoTitle = (TextView) view.findViewById(R.id.tvInfoTitle);// 图片下的标题
            rlItemTitle1 = (RelativeLayout) view.findViewById(R.id.rlItemTitle1);// 内容标题1
            tvItemTitle1 = (TextView) view.findViewById(R.id.tvItemTitle1);
            ivItemTitle1 = (ImageView) view.findViewById(R.id.ivItemTitle1);
            rlItemTitle2 = (RelativeLayout) view.findViewById(R.id.rlItemTitle2);// 内容标题2
            tvItemTitle2 = (TextView) view.findViewById(R.id.tvItemTitle2);
            ivItemTitle2 = (ImageView) view.findViewById(R.id.ivItemTitle2);
            rlItemTitle3 = (RelativeLayout) view.findViewById(R.id.rlItemTitle3);// 内容标题3
            tvItemTitle3 = (TextView) view.findViewById(R.id.tvItemTitle3);
            ivItemTitle3 = (ImageView) view.findViewById(R.id.ivItemTitle3);
            rlItemTitle4 = (RelativeLayout) view.findViewById(R.id.rlItemTitle4);// 内容标题4
            tvItemTitle4 = (TextView) view.findViewById(R.id.tvItemTitle4);
            ivItemTitle4 = (ImageView) view.findViewById(R.id.ivItemTitle4);
            view1 = view.findViewById(R.id.view1);
            view2 = view.findViewById(R.id.view2);
            view3 = view.findViewById(R.id.view3);
            view4 = view.findViewById(R.id.view4);
        }
    }

}
