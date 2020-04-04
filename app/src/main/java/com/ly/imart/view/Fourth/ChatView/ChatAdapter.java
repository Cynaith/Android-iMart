package com.ly.imart.view.Fourth.ChatView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ly.imart.model.Fourth.ChatModel;
import com.ly.imart.util.BitmapUtils;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import im.floo.floolib.BMXConversation;
import im.floo.floolib.BMXGroup;
import im.floo.floolib.BMXMessage;
import im.floo.floolib.BMXMessageConfig;
import im.floo.floolib.BMXRosterItem;
import im.floo.floolib.ListOfLongLong;
import com.ly.imart.R;
import com.ly.imart.maxim.common.utils.RosterFetcher;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.maxim.common.utils.TimeUtils;
import com.ly.imart.maxim.common.view.ImageRequestConfig;
import com.ly.imart.maxim.common.view.ShapeImageView;
import com.ly.imart.maxim.common.view.recyclerview.BaseViewHolder;
import com.ly.imart.maxim.common.view.recyclerview.RecyclerWithHFAdapter;
import com.ly.imart.maxim.message.utils.ChatUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Description : 消息列表 Created by Mango on 2018/11/05.
 */
public class ChatAdapter extends RecyclerWithHFAdapter<BMXConversation> {

    private ImageRequestConfig mConfig;

    private ImageRequestConfig mGroupConfig;

    private List<String> nickname;

    public ChatAdapter(Context context) {
        super(context);
        mConfig = new ImageRequestConfig.Builder().cacheInMemory(true)
                .showImageForEmptyUri(R.drawable.default_avatar_icon)
                .showImageOnFail(R.drawable.default_avatar_icon)
                .cacheOnDisk(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showImageOnLoading(R.drawable.default_avatar_icon).build();

        nickname = new ArrayList<>();
    }

    @Override
    protected int onCreateViewById(int viewType) {
        return R.layout.item_session_view;
    }

    @Override
    protected void onBindHolder(BaseViewHolder holder, int position) {
        ShapeImageView avatar = holder.findViewById(R.id.session_avatar);
        TextView tvTitle = holder.findViewById(R.id.session_title);
        TextView desc = holder.findViewById(R.id.session_desc);
        TextView time = holder.findViewById(R.id.session_time);
        TextView tvUnReadCount = holder.findViewById(R.id.session_unread_num);
        ImageView ivDisturb = holder.findViewById(R.id.session_disturb);

        BMXConversation item = getItem(position);
        // 是否开启免打扰
        boolean isDisturb = false;
        BMXConversation.Type type = item == null ? null : item.type();
        String name = "";

        //获取ChatList
        if (type != null && type == BMXConversation.Type.Single) {
            BMXRosterItem rosterItem = RosterFetcher.getFetcher().getRoster(item.conversationId());
            if (rosterItem != null && !TextUtils.isEmpty(rosterItem.nickname())) {
                name = rosterItem.nickname();
            } else if (rosterItem != null) {
                name = rosterItem.username();
            }
            ChatUtils.getInstance().showRosterAvatar(rosterItem, avatar, mConfig);
            isDisturb = rosterItem != null && rosterItem.isMuteNotification();
        }  else {
            ChatUtils.getInstance().showRosterAvatar(null, avatar, mConfig);
        }
        BMXMessage lastMsg = item == null ? null : item.lastMsg();
        int unReadCount = item == null ? 0 : item.unreadNumber();
        if (isDisturb) {
            tvUnReadCount.setVisibility(View.GONE);
            ivDisturb.setVisibility(unReadCount > 0 ? View.VISIBLE : View.GONE);
        } else {
            ivDisturb.setVisibility(View.GONE);
            if (unReadCount > 0) {
                tvUnReadCount.setVisibility(View.VISIBLE);
                tvUnReadCount.setText(String.valueOf(unReadCount));
            } else {
                tvUnReadCount.setVisibility(View.GONE);
            }
        }
        tvTitle.setText(TextUtils.isEmpty(name) ? "" : name);
        time.setText(lastMsg != null ? TimeUtils.millis2String(lastMsg.serverTimestamp()) : "");
        String msgDesc = ChatUtils.getInstance().getMessageDesc(lastMsg);
        desc.setText(!TextUtils.isEmpty(msgDesc) ? msgDesc : "");
        //获取user头像
        String userimg = null;
        ChatModel chatModel = new ChatModel();
        try {
            userimg = chatModel.getUserimg(name);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        BitmapUtils bu = new BitmapUtils();
        avatar.setImageBitmap(bu.returnBitMap(userimg));

        nickname.add(name);
    }

    public String getName(int position) {
        return nickname.get(position);
    }
}
