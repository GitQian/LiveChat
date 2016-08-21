package com.livechat.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.livechat.chat.entity.UserLoginInfoBean;
import com.livechat.chat.service.UserLoginInfoService;
import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.utils.Constant;

import java.util.Collection;
import java.util.Iterator;

import io.rong.imkit.RongIM;
import io.rong.imlib.MessageTag;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.TypingMessage.TypingStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ImageMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 聊天会话页面
 * Created by Administrator on 2016/3/30.
 */
public class ConversationActivity extends FragmentActivity {

    private Button toChatName;
    private ImageView ivWoDetail;
    private TextView tvWelCome, tvWelComer;

    private String mTargetId = "", mConversationType = "", conversationObj = "", sNickname = "", currentTime = "";
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constant.SET_TEXT_TYPING_TITLE:// 正在输入
                    toChatName.setText("对方正在输入...");
                    break;
                case Constant.SET_VOICE_TYPING_TITLE:// 正在讲话
                    toChatName.setText("对方正在讲话...");
                    break;
                case Constant.SET_TARGETID_TITLE:// 初始化状态
                    toChatName.setText(conversationObj);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        toChatName = (Button) this.findViewById(R.id.toChatName);
        ivWoDetail = (ImageView) this.findViewById(R.id.ivWoDetail);
        tvWelCome = (TextView) this.findViewById(R.id.tvWelCome);
        tvWelComer = (TextView) this.findViewById(R.id.tvWelComer);

        Intent intent = this.getIntent();

        mTargetId = intent.getData().getQueryParameter("targetId");// 对方ID
        conversationObj = intent.getData().getQueryParameter("title");// 对方的昵称
        mConversationType = intent.getData().getLastPathSegment();// 获得当前会话类型

        UserLoginInfoBean userLoginInfoBean = new UserLoginInfoService(this).getSimpleUserLoginInfo(CommonUtil.getUserLoginInfo(this, Constant.ACCOUNT_CODE));
        if (userLoginInfoBean != null) {
            // 昵称
            sNickname = userLoginInfoBean.getsNickname();
            currentTime = CommonUtil.getCurrentTime();
        }

        // 设置昵称
        if (!TextUtils.isEmpty(conversationObj)) {
            toChatName.setText(conversationObj);
            tvWelCome.setText("您好" + sNickname + "，您于" + currentTime + "进入本窗口");
            tvWelComer.setText("您于联系上" + conversationObj + "...");
        } else {
            // 若为空的话, 则可以根据mTargetId去本地服务器中获取
            toChatName.setText(mTargetId);
            tvWelCome.setText("您好" + sNickname + "，您于" + currentTime + "进入本窗口");
            tvWelComer.setText("您于联系上" + mTargetId + "...");
        }
        ivWoDetail.setOnClickListener(new OnHeadDetailInfo());
        /**
         * 设置会话界面操作的监听器。
         */
        RongIM.setConversationBehaviorListener(new MyConversationBehaviorListener());
        /**
         * 设置对方正在输入的状态
         */
        RongIMClient.setTypingStatusListener(new MyTypingStatusListener());
    }

    /**
     * 点击右上角进入详情
     */
    private class OnHeadDetailInfo implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "进入", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 会话界面操作的监听器
     */
    private class MyConversationBehaviorListener implements RongIM.ConversationBehaviorListener {

        /**
         * 当点击用户头像后执行。
         *
         * @param context          上下文。
         * @param conversationType 会话类型。
         * @param userInfo         被点击的用户的信息。
         * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
         */
        @Override
        public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {

            Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();

            if ((conversationType.getName()).equals(mConversationType) && userInfo != null) {// 单聊
//                Intent intent = new Intent(getApplicationContext(), DetailInfoActivity.class);
//                intent.putExtra("UserId", userInfo.getUserId());
//                intent.putExtra("UserName", userInfo.getName());
//                intent.putExtra("PortraitUri", userInfo.getPortraitUri().toString());
//                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "数据错误！", Toast.LENGTH_SHORT).show();
            }
            return false;
        }

        /**
         * 当长按用户头像后执行。
         *
         * @param context          上下文。
         * @param conversationType 会话类型。
         * @param userInfo         被点击的用户的信息。
         * @return 如果用户自己处理了点击后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
         */
        @Override
        public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
            Toast.makeText(getApplicationContext(), "2", Toast.LENGTH_SHORT).show();
            return false;
        }

        /**
         * 当点击消息时执行。
         *
         * @param context 上下文。
         * @param view    触发点击的 View。
         * @param message 被点击的消息的实体信息。
         * @return 如果用户自己处理了点击后的逻辑，则返回 true， 否则返回 false, false 走融云默认处理方式。
         */
        @Override
        public boolean onMessageClick(Context context, View view, io.rong.imlib.model.Message message) {
            Toast.makeText(getApplicationContext(), "3", Toast.LENGTH_SHORT).show();

            if (message.getContent() instanceof ImageMessage) {// 点击的聊天内容是图片
                ImageMessage imageMessage = (ImageMessage) message.getContent();
                Intent intent = new Intent(context, PhotoActivity.class);
                intent.putExtra("photo", imageMessage.getLocalUri() == null ? imageMessage.getRemoteUri() : imageMessage.getLocalUri());
                if (imageMessage.getThumUri() != null) {
                    intent.putExtra("thumbnail", imageMessage.getThumUri());
                }
                context.startActivity(intent);
            }

            return false;
        }

        /**
         * 当点击链接消息时执行。
         *
         * @param context 上下文。
         * @param link    被点击的链接。
         * @return 如果用户自己处理了点击后的逻辑处理，则返回 true， 否则返回 false, false 走融云默认处理方式。
         */
        @Override
        public boolean onMessageLinkClick(Context context, String link) {
            Toast.makeText(getApplicationContext(), "4", Toast.LENGTH_SHORT).show();
            return false;
        }

        /**
         * 当长按消息时执行。
         *
         * @param context 上下文。
         * @param view    触发点击的 View。
         * @param message 被长按的消息的实体信息。
         * @return 如果用户自己处理了长按后的逻辑，则返回 true，否则返回 false，false 走融云默认处理方式。
         */
        @Override
        public boolean onMessageLongClick(Context context, View view, io.rong.imlib.model.Message message) {
            Toast.makeText(getApplicationContext(), "5", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    /**
     * 对方输入的状态
     */
    private class MyTypingStatusListener implements RongIMClient.TypingStatusListener {

        @Override
        public void onTypingStatusChanged(Conversation.ConversationType conversationType, String targetId, Collection<TypingStatus> typingStatusSet) {
            // 当输入状态的会话类型和targetID与当前会话一致时, 才需要显示
            if (conversationType.equals(mConversationType) || targetId.equals(mTargetId)) {
                // count表示当前会话中正在输入的用户数量，目前只支持单聊，所以判断大于0就可以给予显示了
                int count = typingStatusSet.size();
                if (count > 0) {
                    Iterator iterator = typingStatusSet.iterator();
                    TypingStatus status = (TypingStatus) iterator.next();
                    String objectName = status.getTypingContentType();

                    MessageTag textTag = TextMessage.class.getAnnotation(MessageTag.class);
                    MessageTag voiceTag = VoiceMessage.class.getAnnotation(MessageTag.class);
                    //匹配对方正在输入的是文本消息还是语音消息
                    if (objectName.equals(textTag.value())) {
                        //显示"对方正在输入"
                        mHandler.sendEmptyMessage(Constant.SET_TEXT_TYPING_TITLE);
                    } else if (objectName.equals(voiceTag.value())) {
                        //显示"对方正在讲话"
                        mHandler.sendEmptyMessage(Constant.SET_VOICE_TYPING_TITLE);
                    }
                }else{
                    // 当前会话没有用户正在输入, 标题栏仍显示原来标题
                    mHandler.sendEmptyMessage(Constant.SET_TARGETID_TITLE);
                }
            } else {
                // 当前会话没有用户正在输入, 标题栏仍显示原来标题
                mHandler.sendEmptyMessage(Constant.SET_TARGETID_TITLE);
            }
        }
    }

    /**
     * 返回
     *
     * @param view
     */
    public void doBack(View view) {
        finish();
    }

}
