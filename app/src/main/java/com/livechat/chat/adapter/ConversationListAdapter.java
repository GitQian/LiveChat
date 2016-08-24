package com.livechat.chat.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.livechat.chat.R;
import com.livechat.chat.entity.CustomerBean;
import com.livechat.chat.utils.BitmapCache;
import com.livechat.chat.utils.CommonUtil;
import com.livechat.chat.widget.RoundedRectImageView;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.ImageMessage;
import io.rong.message.LocationMessage;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

/**
 * 谈话列表的适配器
 * Created by Administrator on 2016/4/14.
 */
public class ConversationListAdapter extends BaseAdapter {

    private Context mContext;
    private List<CustomerBean> mList;
    private LayoutInflater mInflater;
    private RequestQueue mQueue;
    private ImageLoader mImageLoader;

    public ConversationListAdapter(Context mContext,RequestQueue mQueue) {
        this.mContext = mContext;
        this.mInflater = LayoutInflater.from(mContext);
        this.mQueue=mQueue;
        mImageLoader = new ImageLoader(mQueue, new BitmapCache());
    }

    public void add(List<CustomerBean> mList) {
        this.mList = mList;
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
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.conversation_list_layout, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvUptime.setVisibility(View.GONE);
        CustomerBean customerBean = mList.get(position);
        if (customerBean != null) {
            viewHolder.tvTargetName.setText(customerBean.getsNickname());
            if (customerBean.getiOnlineStatus() == 0) {// 偶数(0在线)
                viewHolder.ivOnLine.setImageResource(R.mipmap.online);
            } else {// 奇数(1离线)
                viewHolder.ivOnLine.setImageResource(R.mipmap.offline);
            }
            String sPicUrl = customerBean.getsHeaderImage().equals("") ? "" : customerBean.getsHeaderImage();
//            CommonUtil.showImageLoader(sPicUrl, viewHolder.ivConversationPic, R.mipmap.customer_service);

            // 利用Volley加载图片
            ImageLoader.ImageListener listener = ImageLoader.getImageListener(viewHolder.ivConversationPic, 0, R.mipmap.customer_service);
            mImageLoader.get(sPicUrl, listener);
            MessageContent messageContent = customerBean.getContent();
            if (messageContent instanceof TextMessage) {// 文本消息
                TextMessage txtMessage = (TextMessage) messageContent;
                viewHolder.tvConversationContent.setText(txtMessage.getContent());
            } else if (messageContent instanceof ImageMessage) {// 图片消息
                viewHolder.tvConversationContent.setText(R.string.imageMessage);
            } else if (messageContent instanceof VoiceMessage) {// 语音消息
                viewHolder.tvConversationContent.setText(R.string.voiceMessage);
            } else if (messageContent instanceof RichContentMessage) {// 图文消息
                RichContentMessage richContentMessage = (RichContentMessage) messageContent;
                viewHolder.tvConversationContent.setText(richContentMessage.getContent());
            } else if (messageContent instanceof LocationMessage) {// 位置消息
                viewHolder.tvConversationContent.setText(R.string.locationMessage);
            } else {// 其他消息处理
                viewHolder.tvConversationContent.setText("");
            }
            // 最后发送的时间
            long sendTime = customerBean.getSentTime();
            if (sendTime > 0) {
                viewHolder.tvUptime.setText(CommonUtil.dateConversionString(sendTime));
                viewHolder.tvUptime.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvUptime.setVisibility(View.GONE);
            }




            // 获取消息的最后一条记录
//            if (RongIM.getInstance() != null) {
//                // 会话类型, 会话Id, 数量, 回调函数
//                List<Message> msgList = RongIM.getInstance().getRongIMClient().getLatestMessages(Conversation.ConversationType.PRIVATE, customerBean.getsAccount(), 1);
//                if (msgList != null) {
//                    for (Message msg : msgList) {
//                        MessageContent messageContent = msg.getContent();
//                        if (messageContent instanceof TextMessage) {// 文本消息
//                            TextMessage txtMessage = (TextMessage) messageContent;
//                            viewHolder.tvConversationContent.setText(txtMessage.getContent());
//                        } else if (messageContent instanceof ImageMessage) {// 图片消息
//                            viewHolder.tvConversationContent.setText(R.string.imageMessage);
//                        } else if (messageContent instanceof VoiceMessage) {// 语音消息
//                            viewHolder.tvConversationContent.setText(R.string.voiceMessage);
//                        } else if (messageContent instanceof RichContentMessage) {// 图文消息
//                            RichContentMessage richContentMessage = (RichContentMessage) messageContent;
//                            viewHolder.tvConversationContent.setText(richContentMessage.getContent());
//                        } else if (messageContent instanceof LocationMessage) {// 位置消息
//                            viewHolder.tvConversationContent.setText(R.string.locationMessage);
//                        } else {// 其他消息处理
//                            viewHolder.tvConversationContent.setText("");
//                        }
//                        // 最后发送的时间
//                        long sendTime = msg.getSentTime();
//                        if (sendTime > 0) {
//                            viewHolder.tvUptime.setText(CommonUtil.dateConversionString(sendTime));
//                            viewHolder.tvUptime.setVisibility(View.VISIBLE);
//                        } else {
//                            viewHolder.tvUptime.setVisibility(View.GONE);
//                        }
//                    }
//                } else {
//                    viewHolder.tvConversationContent.setText("");
//                }
//            }

            // 获取某人的未读消息数
            RongIM.getInstance().getRongIMClient().getUnreadCount(Conversation.ConversationType.PRIVATE, customerBean.getsAccount(), new RongIMClient.ResultCallback<Integer>() {

                @Override
                public void onSuccess(Integer integer) {
                    if (integer > 0) {
                        viewHolder.tvRedNumber.setVisibility(View.VISIBLE);
                        viewHolder.tvRedNumber.setText(String.valueOf(integer));
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
        return convertView;
    }

    private class ViewHolder {

        private RoundedRectImageView ivConversationPic;
        private ImageView ivOnLine;
        private TextView tvTargetName;
        private TextView tvUptime;
        private TextView tvConversationContent;
        private TextView tvRedNumber;

        private ViewHolder(View view) {
            ivConversationPic = (RoundedRectImageView) view.findViewById(R.id.ivConversationPic);
            ivOnLine = (ImageView) view.findViewById(R.id.ivOnLine);
            tvTargetName = (TextView) view.findViewById(R.id.tvTargetName);
            tvUptime = (TextView) view.findViewById(R.id.tvUptime);
            tvConversationContent = (TextView) view.findViewById(R.id.tvConversationContent);
            tvRedNumber = (TextView) view.findViewById(R.id.tvRedNumber);
        }
    }

}
