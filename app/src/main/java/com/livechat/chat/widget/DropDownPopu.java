package com.livechat.chat.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.livechat.chat.R;

/**
 * PopupWindow窗口
 * Created by Administrator on 2016/3/31.
 */
public class DropDownPopu {

    private PopupWindow popupWindow = null;

    public void showPopupWindow(View parent, Context mContext) {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.drop_down_popu_layout, null);
            // 创建一个PopupWindow对象
            popupWindow = new PopupWindow(view, 300, ViewGroup.LayoutParams.WRAP_CONTENT);
            // 绑定单击事件 - 消息
            (view.findViewById(R.id.rlNews)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            // 首页
            (view.findViewById(R.id.rlHome)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            // 帮助
            (view.findViewById(R.id.rlHelp)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
        }
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.customDropDownAnimStyle);
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth() - popupWindow.getWidth();
        popupWindow.showAsDropDown(parent, xPos, 0);
    }

}
