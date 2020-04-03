package com.ly.imart.view.Fourth.ChatView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.ly.imart.R;
import com.ly.imart.maxim.common.base.BaseTitleActivity;
import com.ly.imart.maxim.common.utils.RxBus;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.maxim.common.view.Header;
import com.ly.imart.maxim.common.view.ImmerseLinearLayout;
import com.ly.imart.maxim.message.contract.ChatBaseContract;
import com.ly.imart.maxim.message.customviews.ChatRecyclerView;
import com.ly.imart.maxim.message.customviews.ChatViewGroup;
import com.ly.imart.maxim.message.customviews.MessageInputBar;
import com.ly.imart.maxim.message.utils.ChatRecyclerScrollListener;
import com.ly.imart.maxim.message.utils.ChatViewHelper;
import com.ly.imart.maxim.message.utils.MessageConfig;

import java.util.List;

import im.floo.floolib.BMXMessage;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public abstract class ChatActivity extends AppCompatActivity
        implements ChatBaseContract.View, MessageInputBar.OnInputPanelListener {

    protected long mMyUserId;

    protected long mChatId;

    protected int mChatType;

    protected ChatViewGroup mChatViewGroup;

    private ChatRecyclerView mChatRecyclerView;

    private ChatRecyclerScrollListener mScrollListener;

    protected ChatViewHelper mChatViewHelper;

    private MessageInputBar mInputBar;

    /* 语音录制view */
    private View mRecordView;

    private ImageView mRecordMic;

    protected ChatBaseContract.Presenter mPresenter;

    private View mContentView;

    private ImmerseLinearLayout mContainer;

    public static void startChatActivity(Context context, long chatId) {
        Intent intent = new Intent();
        intent.setClass(context, ChatSingleActivity.class);
        intent.putExtra(MessageConfig.CHAT_ID, chatId);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.base_activity);
        mContainer = ((ImmerseLinearLayout)findViewById(R.id.container));
        initDataFromFront(getIntent());
        addView();
        super.onCreate(savedInstanceState);
    }

    public View onCreateView() {
        View view = View.inflate(this, R.layout.activity_chat_base, null);
        mChatViewGroup = view.findViewById(R.id.chat_view_group);
        mChatRecyclerView = view.findViewById(R.id.rcy_chat);
        mInputBar = view.findViewById(R.id.chat_control_bar);
        mRecordView = view.findViewById(R.id.record_voice_view);
        mRecordMic = view.findViewById(R.id.iv_record_mic);
        mChatViewHelper = new ChatViewHelper(this, mChatRecyclerView);
        return view;
    }


    protected void initDataFromFront(Intent intent) {

        mChatId = intent.getLongExtra(MessageConfig.CHAT_ID, 0);

        mMyUserId = SharePreferenceUtils.getInstance().getUserId();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (mPresenter != null) {
            mPresenter.onSaveInstanceState(outState);
        }
        super.onSaveInstanceState(outState);
    }

    protected void receiveRxBus() {
        RxBus.getInstance().toObservable(Intent.class).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Intent>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Intent intent) {
                if (intent == null) {
                    return;
                }
                String action = intent.getAction();
                if (TextUtils.equals(action, "onRosterInfoUpdate")) {
                    updateListView();
                } else if (TextUtils.equals(action, "onInfoUpdated")) {
                    updateListView();
                } else if (TextUtils.equals(action, "onShowReadAckUpdated")) {
                    boolean ack = intent.getBooleanExtra("onShowReadAckUpdated", false);
                    showReadAck(ack);
                }
            }
        });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        if (mPresenter != null) {
            mPresenter.onRestoreInstanceState(savedInstanceState);
        }
        super.onRestoreInstanceState(savedInstanceState);
    }

    protected abstract void initChatInfo(long myUserId, long chatId);

    protected void setViewListener() {
        mChatViewGroup.setInterceptTouchListener(new ChatViewGroup.InterceptTouchListener() {
            @Override
            public boolean setInterceptTouchListener(MotionEvent ev) {
                switch (ev.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        if (mInputBar.isShowKeyBoard() || mInputBar.isShowPanel()) {
                            mInputBar.hidePanel();
                            return true;
                        }
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
        mInputBar.setInputListener(this);

        /* 上下拉刷新 */
        mChatRecyclerView.addOnScrollListener(mScrollListener = new ChatRecyclerScrollListener(
                (LinearLayoutManager) mChatRecyclerView.getLayoutManager()) {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            protected void onLoadPullDown(int offset) {
                super.onLoadPullDown(offset);
                if (mPresenter != null && mChatViewHelper != null) {
                    mPresenter.getPullDownChatMessages(mChatViewHelper.getFirstMsgId(), offset);
                }
            }

            @Override
            protected void onLoadPullUp(int offset) {
                super.onLoadPullUp(offset);
            }
        });
    }

    @Override
    public void setPresenter(ChatBaseContract.Presenter presenter) {
        mPresenter = presenter;
        mChatViewHelper.setChatPresenter(presenter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.onPause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mInputBar.isShowKeyBoard()) {
            mInputBar.hideKeyboard();
        }
    }


    @Override
    public void showChatMessages(List<BMXMessage> beans) {
        if (beans == null || beans.isEmpty()) {
            if (mScrollListener != null) {
                mScrollListener.resetLoadStatus();
            }
            return;
        }
        boolean isHasData = beans.size() >= MessageConfig.DEFAULT_PAGE_SIZE;
        if (mScrollListener != null) {
            // 如果有更多数据 需要重置scrollListener滑动到顶部加载数据回调
            mScrollListener.resetLoadStatus();
            // if (isHasData) {
            // // 如果有更多数据 需要重置scrollListener滑动到顶部加载数据回调
            // mScrollListener.resetLoadStatus();
            // } else {
            // // 如果没有更多数据 需要关闭scrollListener滑动到顶部加载数据回调
            // mScrollListener.closeLoading();
            // }
        }
        mChatViewHelper.initMessages(beans, isHasData);
        // 同步未读
        if (mPresenter != null) {
            mPresenter.readAllMessage();
        }
    }

    @Override
    public void showPullChatMessages(List<BMXMessage> beans, int offset) {
        if (beans == null || beans.isEmpty()) {
            if (mScrollListener != null) {
                mScrollListener.resetLoadStatus();
            }
            return;
        }
        boolean isHasData = beans.size() >= MessageConfig.DEFAULT_PAGE_SIZE;
        if (mScrollListener != null) {
            // 如果有更多数据 需要重置scrollListener滑动到顶部加载数据回调
            mScrollListener.resetLoadStatus();
            // if (isHasData) {
            // // 如果有更多数据 需要重置scrollListener滑动到顶部加载数据回调
            // mScrollListener.resetLoadStatus();
            // } else {
            // // 如果没有更多数据 需要关闭scrollListener滑动到顶部加载数据回调
            // mScrollListener.closeLoading();
            // }
        }
        mChatViewHelper.pullDownMessages(beans, offset, isHasData);
    }

    @Override
    public void sendChatMessage(BMXMessage bean) {
        if (bean != null) {
            mChatViewHelper.addChatMessage(bean);
        }
    }

    @Override
    public void receiveChatMessage(List<BMXMessage> beans) {
        if (beans != null && !beans.isEmpty()) {
            mChatViewHelper.addChatMessages(beans);
        }
    }

    @Override
    public void deleteChatMessage(BMXMessage bean) {
        if (bean == null) {
            return;
        }
        long msgId = bean.msgId();
        // 判断删除的消息是否是最后一条
        boolean isLastMsg = false;
        BMXMessage lastBean = mChatViewHelper
                .getChatMessageByPosition(mChatViewHelper.getLastVisiblePosition());
        if (lastBean != null && lastBean.msgId() == msgId) {
            isLastMsg = true;
        }
        mChatViewHelper.deleteMessage(msgId);

        // 删除消息后 如果加载消息类型显示出来 则需要加载最新数据
        int firstPos = mChatViewHelper.getFirstVisiblePosition();
        // 当显示的第一条消息 直接获取
        if (firstPos <= 1) {
            mPresenter.getPullDownChatMessages(mChatViewHelper.getFirstMsgId(), 0);
        }
        if (isLastMsg) {
            // 如果删除的消息是最后一条 需要滚动到消息底部
            mChatViewHelper.scrollBottom();
        }
    }

    @Override
    public void updateListView() {
        if (mChatViewHelper != null) {
            mChatViewHelper.updateListView();
        }
    }

    @Override
    public void showReadAck(boolean readAck) {
        if (mChatViewHelper != null) {
            mChatViewHelper.showReadAck(readAck);
        }
    }

    @Override
    public BMXMessage getLastMessage() {
        if (mChatViewHelper != null) {
            return mChatViewHelper.getLastMessage();
        }
        return null;
    }

    @Override
    public void clearChatMessages() {
        mScrollListener.closeLoading();
        mScrollListener.closeUpLoading();
        if (mChatViewHelper != null) {
            mChatViewHelper.clearChatMessages();
        }
    }

    @Override
    public void sendChatMessages(List<BMXMessage> beans) {
        if (beans != null && beans.size() > 0) {
            mChatViewHelper.addChatMessages(beans);
        }
    }

    @Override
    public void updateChatMessage(BMXMessage bean) {
        if (bean != null) {
            mChatViewHelper.updateChatMessage(bean);
        }
    }

    @Override
    public void cancelVoicePlay() {
        mChatViewHelper.cancelVoicePlay();
    }

    @Override
    public void showRecordView() {
        mRecordView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showRecordMicView(int radio) {
        if (mRecordView != null && mRecordView.isShown()) {
            if (radio > 20) {
                mRecordMic.setBackgroundResource(R.drawable.voice_mic_6);
                return;
            }
            if (radio <= 4) {
                mRecordMic.setBackgroundResource(R.drawable.voice_mic_1);
            } else if (radio <= 8) {
                mRecordMic.setBackgroundResource(R.drawable.voice_mic_2);
            } else if (radio <= 12) {
                mRecordMic.setBackgroundResource(R.drawable.voice_mic_3);
            } else if (radio <= 16) {
                mRecordMic.setBackgroundResource(R.drawable.voice_mic_4);
            } else {
                mRecordMic.setBackgroundResource(R.drawable.voice_mic_5);
            }
        }
    }

    @Override
    public void hideRecordView() {
        mRecordView.setVisibility(View.GONE);
    }

    @Override
    public void insertInAt(List<String> atNames) {
        if (mInputBar != null) {
            mInputBar.insertInAt(atNames);
        }
    }

    @Override
    public void setControlBarText(String content) {
        if (mInputBar != null && !TextUtils.isEmpty(content)) {
            mInputBar.appendString(content);
        }
    }

    @Override
    public String getControlBarText() {
        return mInputBar == null ? "" : mInputBar.getChatEditText();
    }

    @Override
    public void onFunctionRequest(String functionType) {
        if (mPresenter != null) {
            mPresenter.onFunctionRequest(functionType);
        }
    }

    @Override
    public void onSendTextRequest(String sendText) {
        if (mPresenter != null) {
            mPresenter.onSendTextRequest(sendText);
        }
    }

    @Override
    public void onSendVoiceRequest(int voiceAction, long time) {
        if (mPresenter != null) {
            mPresenter.onSendVoiceRequest(voiceAction, time);
        }
    }

    @Override
    public void onChatAtMember() {

    }

    @Override
    public void onTagChanged(int tag) {
        mChatViewHelper.scrollBottom();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPresenter != null) {
            mPresenter.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        if (mPresenter != null) {
            mPresenter.onDestroyPresenter();
        }
        super.onDestroy();
    }
    /**
     * 添加页面布局
     */
    private void addView() {
        mContentView = onCreateView();
        if (mContentView != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 0);
            params.weight = 1;
            mContainer.addView(mContentView, params);
        }
        setViewListener();
        initChatInfo(mMyUserId, mChatId);
        receiveRxBus();
    }
}
