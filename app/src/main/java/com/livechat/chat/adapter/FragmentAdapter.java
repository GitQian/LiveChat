package com.livechat.chat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.livechat.chat.fragment.CustomerScFragment;
import com.livechat.chat.fragment.InfoActFragment;
import com.livechat.chat.fragment.LiveChatMainActivity;
import com.livechat.chat.fragment.MeFragment;
import com.livechat.chat.fragment.OtherFragment;

/**
 * Created by Administrator on 2016/3/21.
 */
public class FragmentAdapter extends FragmentPagerAdapter {

    public final static int TAB_COUNT = 4;

    public FragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int id) {
        switch (id) {
            case LiveChatMainActivity.TAB_PAGE1:
                CustomerScFragment customerScFragment = new CustomerScFragment();
                return customerScFragment;
            case LiveChatMainActivity.TAB_PAGE2:
                InfoActFragment infoActFragment = new InfoActFragment();
                return infoActFragment;
            case LiveChatMainActivity.TAB_PAGE3:
                MeFragment meFragment = new MeFragment();
                return meFragment;
            case LiveChatMainActivity.TAB_PAGE4:
                OtherFragment otherFragment = new OtherFragment();
                return otherFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_COUNT;
    }

}
