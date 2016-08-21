package com.livechat.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;

import java.util.ArrayList;
import java.util.List;

/**
 * 引导页面
 */
public class GuidePageActivity extends BaseActivity {

    private ViewPager viewPage;
    private ImageView ivPageOne, ivPageTwo, ivPageThree, ivPageFour;

    private List<View> viewList;
    private GuidePageChangeListener guidePageChangeListener;
    private CommonUtil commonUtil = new CommonUtil();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_page);

        initUI();
    }

    /**
     * 初始化控件UI
     */
    private void initUI() {
        viewPage = (ViewPager) this.findViewById(R.id.viewPage);
        ivPageOne = (ImageView) this.findViewById(R.id.ivPageOne);
        ivPageTwo = (ImageView) this.findViewById(R.id.ivPageTwo);
        ivPageThree = (ImageView) this.findViewById(R.id.ivPageThree);
        ivPageFour = (ImageView) this.findViewById(R.id.ivPageFour);

        if (CommonUtil.getIsOpen(this)) {
            Intent intent = new Intent(this, LauncherActivity.class);
            startActivity(intent);
            finish();
        } else {
            viewList = new ArrayList<>();

            LayoutInflater inflater = this.getLayoutInflater();
            View ivPage1 = inflater.inflate(R.layout.iv_page_one_layout, null);
            ImageView imageView1 = (ImageView) ivPage1.findViewById(R.id.page_log1);
            imageView1.setImageBitmap(commonUtil.readBitMap(this, R.mipmap.welcome_bg_one));

            View ivPage2 = inflater.inflate(R.layout.iv_page_two_layout, null);
            ImageView imageView2 = (ImageView) ivPage2.findViewById(R.id.page_log2);
            imageView2.setImageBitmap(commonUtil.readBitMap(this, R.mipmap.welcome_bg_two));

            View ivPage3 = inflater.inflate(R.layout.iv_page_three_layout, null);
            ImageView imageView3 = (ImageView) ivPage3.findViewById(R.id.page_log3);
            imageView3.setImageBitmap(commonUtil.readBitMap(this, R.mipmap.welcome_bg_three));

            View ivPage4 = inflater.inflate(R.layout.iv_page_four_layout, null);
            ImageView imageView4 = (ImageView) ivPage4.findViewById(R.id.page_log4);
            imageView4.setImageBitmap(commonUtil.readBitMap(this, R.mipmap.bg));

            (ivPage4.findViewById(R.id.tvTitle)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CommonUtil.isOpen(GuidePageActivity.this, true);
                    Intent intent = new Intent();
                    intent.setClass(GuidePageActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
            (ivPage4.findViewById(R.id.ivSlice)).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    CommonUtil.isOpen(GuidePageActivity.this, true);
                    Intent intent = new Intent();
                    intent.setClass(GuidePageActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            });

            viewList.add(ivPage1);
            viewList.add(ivPage2);
            viewList.add(ivPage3);
            viewList.add(ivPage4);

            guidePageChangeListener = new GuidePageChangeListener();
            viewPage.setAdapter(guidePageChangeListener);
            viewPage.addOnPageChangeListener(ViewOnPageChangeListener);
            guidePageChangeListener.notifyDataSetChanged();
        }
    }

    /**
     * ViewPage的适配器
     */
    private class GuidePageChangeListener extends PagerAdapter {

        @Override
        public int getCount() {
            return viewList == null ? 0 : viewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(viewList.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(viewList.get(position));
            return viewList.get(position);
        }
    }

    /**
     * 滑动的事件
     */
    private ViewPager.OnPageChangeListener ViewOnPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position) {
                case Constant.PAGE_ZERO:
                    ivPageOne.setImageResource(R.mipmap.page_focused);
                    ivPageTwo.setImageResource(R.mipmap.page_unfocused);
                    ivPageThree.setImageResource(R.mipmap.page_unfocused);
                    ivPageFour.setImageResource(R.mipmap.page_unfocused);
                    break;
                case Constant.PAGE_ONE:
                    ivPageOne.setImageResource(R.mipmap.page_unfocused);
                    ivPageTwo.setImageResource(R.mipmap.page_focused);
                    ivPageThree.setImageResource(R.mipmap.page_unfocused);
                    ivPageFour.setImageResource(R.mipmap.page_unfocused);
                    break;
                case Constant.PAGE_TWO:
                    ivPageOne.setImageResource(R.mipmap.page_unfocused);
                    ivPageTwo.setImageResource(R.mipmap.page_unfocused);
                    ivPageThree.setImageResource(R.mipmap.page_focused);
                    ivPageFour.setImageResource(R.mipmap.page_unfocused);
                    break;
                case Constant.PAGE_THREE:
                    ivPageOne.setImageResource(R.mipmap.page_unfocused);
                    ivPageTwo.setImageResource(R.mipmap.page_unfocused);
                    ivPageThree.setImageResource(R.mipmap.page_unfocused);
                    ivPageFour.setImageResource(R.mipmap.page_focused);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

}
