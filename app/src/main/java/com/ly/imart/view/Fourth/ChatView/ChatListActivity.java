package com.ly.imart.view.Fourth.ChatView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LongDef;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ly.imart.R;
import com.ly.imart.maxim.bmxmanager.BaseManager;
import com.ly.imart.maxim.bmxmanager.ChatManager;
import com.ly.imart.maxim.bmxmanager.GroupManager;
import com.ly.imart.maxim.bmxmanager.RosterManager;
import com.ly.imart.maxim.common.utils.CommonConfig;
import com.ly.imart.maxim.common.utils.RosterFetcher;
import com.ly.imart.maxim.common.utils.RxBus;
import com.ly.imart.maxim.common.utils.ScreenUtils;
import com.ly.imart.maxim.common.utils.ToastUtil;
import com.ly.imart.maxim.common.utils.dialog.CustomDialog;
import com.ly.imart.maxim.message.view.ChatBaseActivity;
import com.ly.imart.maxim.message.view.ChatSingleActivity;
import com.ly.imart.view.Fourth.IMUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

import im.floo.floolib.BMXChatServiceListener;
import im.floo.floolib.BMXConversation;
import im.floo.floolib.BMXConversationList;
import im.floo.floolib.BMXErrorCode;
import im.floo.floolib.BMXGroupList;
import im.floo.floolib.BMXMessage;
import im.floo.floolib.BMXMessageList;
import im.floo.floolib.BMXRosterItemList;
import im.floo.floolib.ListOfLongLong;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class ChatListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ChatAdapter mAdapter;
    private View mEmptyView;
    private ChatListActivity chatListActivity;
    long chatId = 0;
    volatile boolean isIn = false;
    private ProgressDialog progDialog = null;// 搜索时进度条
    private BMXChatServiceListener mListener = new BMXChatServiceListener() {

        @Override
        public void onStatusChanged(BMXMessage msg, BMXErrorCode error) {
        }

        @Override
        public void onAttachmentStatusChanged(BMXMessage msg, BMXErrorCode error, int percent) {
        }

        @Override
        public void onRecallStatusChanged(BMXMessage msg, BMXErrorCode error) {
        }

        @Override
        public void onReceive(BMXMessageList list) {
            // 收到消息
            loadSession();
        }

        @Override
        public void onReceiveSystemMessages(BMXMessageList list) {
            // 收到系统消息
            loadSession();
        }

        @Override
        public void onReceiveReadAcks(BMXMessageList list) {
        }

        @Override
        public void onReceiveDeliverAcks(BMXMessageList list) {
        }

        @Override
        public void onReceiveRecallMessages(BMXMessageList list) {
            // 收到撤回消息
            loadSession();
        }

        @Override
        public void onAttachmentUploadProgressChanged(BMXMessage msg, int percent) {
        }
    };

    public static void openFromMessage(Context context, long id) {
        Intent intent = new Intent();
        intent.setClass(context, ChatListActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);
        chatListActivity = this;
        Intent intent = getIntent();
        chatId = intent.getLongExtra("id", 0);
        if (chatId != 0) {
            startChat(chatId);
        }
        loadSession();
        mRecyclerView = findViewById(R.id.session_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter = new ChatAdapter(this));
        ChatManager.getInstance().addChatListener(mListener);
        mEmptyView = View.inflate(this, R.layout.view_empty, null);
        mAdapter.addFooterView(mEmptyView);
        mEmptyView.setVisibility(View.GONE);
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter == null) {
                    return;
                }
                BMXConversation item = mAdapter.getItem(position);
                if (item == null) {
                    return;
                }
//                ChatBaseActivity.startChatActivity(ChatListActivity.this, BMXMessage.MessageType.Single, item.conversationId());
//                ChatActivity.startChatActivity(ChatListActivity.this, item.conversationId(),mAdapter.getName(position));
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
////                        int people = mAdapter.getPeopleCount();
////                        int nicknum = mAdapter.getNickCount();
////                        Log.d("聊天",mAdapter.getNickname().toString());
////                        String username = mAdapter.getName((position-mAdapter.getPeopleCount()) + mAdapter.getNickCount() );
////                        chatListActivity.setId(IMUtils.searchRoster(mAdapter.getName((position-mAdapter.getPeopleCount()) + mAdapter.getNickCount())));
//                        chatListActivity.setId(IMUtils.searchRoster(mAdapter.getName(position)));
//                    }
//                }).start();

//                FutureTask<Long> ft = new FutureTask<>(new Callable<Long>() {
//                    @Override
//                    public Long call() throws Exception {
//                        return IMUtils.searchRoster(mAdapter.getName(position));
//                    }
//                });
//                new Thread(ft).start();
//                showProgressDialog();
//                while (chatId == 0){
//                    try {
//                        chatId = ft.get();
//                    } catch (ExecutionException e) {
//                        e.printStackTrace();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
                dissmissProgressDialog();
//                startChat(chatId);
                ChatBaseActivity.startChatActivity(ChatListActivity.this, BMXMessage.MessageType.Single, item.conversationId());
            }
        });

        mAdapter.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
                                           long id) {
                showOperateSession(mAdapter.getItem(position), position);
                return true;
            }
        });
    }

    private void loadSession() {
        // 获取所有未读数
        Observable.just("").map(s -> ChatManager.getInstance().getAllConversationsUnreadCount())
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Integer count) {
                        Intent intent = new Intent();
                        intent.setAction(CommonConfig.SESSION_COUNT_ACTION);
                        intent.putExtra(CommonConfig.TAB_COUNT, count == null ? 0 : count);
                        RxBus.getInstance().send(intent);
                    }
                });
        // 获取所有会话
        Observable.just("").map(s -> ChatManager.getInstance().getAllConversations())
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BMXConversationList>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BMXConversationList bmxConversationList) {
                        List<BMXConversation> conversationList = new ArrayList<>();
                        if (bmxConversationList != null && !bmxConversationList.isEmpty()) {
                            for (int i = 0; i < bmxConversationList.size(); i++) {
                                BMXConversation conversation = bmxConversationList.get(i);
                                if (conversation != null && conversation.conversationId() > 0) {
                                    conversationList.add(conversation);
                                }
                            }
                        }
                        if (!conversationList.isEmpty()) {
                            showEmpty(false);
                            sortSession(conversationList);
                            mAdapter.replaceList(conversationList);
                            notifySession(conversationList);
                        } else {
                            showEmpty(true);
                        }
                    }
                });
    }

    private void showEmpty(boolean empty) {
        RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) mEmptyView.getLayoutParams();
        if (empty) {
            mEmptyView.setVisibility(View.VISIBLE);
            params.width = RecyclerView.LayoutParams.MATCH_PARENT;
            params.height = RecyclerView.LayoutParams.MATCH_PARENT;
        } else {
            mEmptyView.setVisibility(View.GONE);
            params.width = 0;
            params.height = 0;
        }
        mEmptyView.setLayoutParams(params);
    }

    /**
     * 排序
     *
     * @param conversationList list
     */
    private void sortSession(List<BMXConversation> conversationList) {
        if (conversationList == null || conversationList.isEmpty()) {
            return;
        }
        Collections.sort(conversationList, (o1, o2) -> {
            BMXMessage m1 = o1 == null || o1.lastMsg() == null ? null : o1.lastMsg();
            BMXMessage m2 = o2 == null || o2.lastMsg() == null ? null : o2.lastMsg();
            long o1Time = m1 == null ? -1 : m1.serverTimestamp();
            long o2Time = m2 == null ? -1 : m2.serverTimestamp();
            if (o1Time == o2Time) {
                return 0;
            }
            return o1Time > o2Time ? -1 : 1;
        });
    }

    /**
     * 刷新roster名称 头像
     *
     * @param conversationList
     */
    private void notifySession(List<BMXConversation> conversationList) {
        if (conversationList == null || conversationList.isEmpty()) {
            return;
        }
        ListOfLongLong rosterIds = new ListOfLongLong();
        ListOfLongLong groupIds = new ListOfLongLong();
        for (int i = 0; i < conversationList.size(); i++) {
            BMXConversation conversation = conversationList.get(i);
            if (conversation == null) {
                continue;
            }
            if (conversation.type() == BMXConversation.Type.Single) {
                if (RosterFetcher.getFetcher().getRoster(conversation.conversationId()) == null) {
                    rosterIds.add(conversation.conversationId());
                }
            }
        }
        if (!rosterIds.isEmpty()) {
            Observable.just(rosterIds).map(new Func1<ListOfLongLong, BMXErrorCode>() {
                @Override
                public BMXErrorCode call(ListOfLongLong listOfLongLong) {
                    BMXRosterItemList itemList = new BMXRosterItemList();
                    BMXErrorCode errorCode = RosterManager.getInstance().search(listOfLongLong,
                            itemList, true);
                    if (errorCode == BMXErrorCode.NoError) {
                        RosterFetcher.getFetcher().putRosters(itemList);
                    }
                    return errorCode;
                }
            }).flatMap(new Func1<BMXErrorCode, Observable<BMXErrorCode>>() {
                @Override
                public Observable<BMXErrorCode> call(BMXErrorCode errorCode) {
                    return BaseManager.bmxFinish(errorCode, errorCode);
                }
            }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BMXErrorCode>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(BMXErrorCode errorCode) {
                            if (mAdapter != null) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
        if (!groupIds.isEmpty()) {
            Observable.just(groupIds).map(new Func1<ListOfLongLong, BMXErrorCode>() {
                @Override
                public BMXErrorCode call(ListOfLongLong listOfLongLong) {
                    BMXGroupList list = new BMXGroupList();
                    BMXErrorCode errorCode = GroupManager.getInstance().search(listOfLongLong, list,
                            false);
                    if (errorCode == BMXErrorCode.NoError) {
                        RosterFetcher.getFetcher().putGroups(list);
                    }
                    return errorCode;
                }
            }).flatMap(new Func1<BMXErrorCode, Observable<BMXErrorCode>>() {
                @Override
                public Observable<BMXErrorCode> call(BMXErrorCode errorCode) {
                    return BaseManager.bmxFinish(errorCode, errorCode);
                }
            }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<BMXErrorCode>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(BMXErrorCode errorCode) {
                            if (mAdapter != null) {
                                mAdapter.notifyDataSetChanged();
                            }
                        }
                    });
        }
    }

    private void showOperateSession(final BMXConversation conversation, final int position) {

        final CustomDialog dialog = new CustomDialog();
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        // 删除
        TextView name = new TextView(this);
        name.setPadding(ScreenUtils.dp2px(15), 0, ScreenUtils.dp2px(15), 0);
        name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        name.setTextColor(getResources().getColor(R.color.color_black));
        name.setBackgroundColor(getResources().getColor(R.color.color_white));
        name.setText(getString(R.string.delete));
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Observable.just(conversation).map(new Func1<BMXConversation, BMXErrorCode>() {
                    @Override
                    public BMXErrorCode call(BMXConversation conversation) {
                        if (conversation == null) {
                            return null;
                        }
                        ChatManager.getInstance().deleteConversation(conversation.conversationId(),
                                true);
                        return BMXErrorCode.NoError;
                    }
                }).flatMap(new Func1<BMXErrorCode, Observable<BMXErrorCode>>() {
                    @Override
                    public Observable<BMXErrorCode> call(BMXErrorCode errorCode) {
                        return BaseManager.bmxFinish(errorCode, errorCode);
                    }
                }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<BMXErrorCode>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(BMXErrorCode errorCode) {
                                mAdapter.remove(position);
                                showEmpty(mAdapter.getItemCount() <= 2);
                            }
                        });
            }
        });
        ll.addView(name, params);
        // 清空聊天记录
        TextView clear = new TextView(this);
        clear.setPadding(ScreenUtils.dp2px(15), ScreenUtils.dp2px(15), ScreenUtils.dp2px(15), 0);
        clear.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
        clear.setTextColor(getResources().getColor(R.color.color_black));
        clear.setBackgroundColor(getResources().getColor(R.color.color_white));
        clear.setText(getString(R.string.chat_clear_msg));
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Observable.just(conversation).map(new Func1<BMXConversation, BMXErrorCode>() {
                    @Override
                    public BMXErrorCode call(BMXConversation conversation) {
                        if (conversation == null) {
                            return null;
                        }
                        return conversation.removeAllMessages();
                    }
                }).flatMap(new Func1<BMXErrorCode, Observable<BMXErrorCode>>() {
                    @Override
                    public Observable<BMXErrorCode> call(BMXErrorCode errorCode) {
                        return BaseManager.bmxFinish(errorCode, errorCode);
                    }
                }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<BMXErrorCode>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                ToastUtil.showTextViewPrompt("清除失败");
                            }

                            @Override
                            public void onNext(BMXErrorCode errorCode) {
                                ToastUtil.showTextViewPrompt("清除成功");
                                loadSession();
                            }
                        });
            }
        });
        // TODO 清除聊天记录功能暂时不加 C++暂时没有对外提供
        // ll.addView(clear, params);

        dialog.setCustomView(ll);
        dialog.showDialog(this);
    }

    public void setId(long id) {
        isIn = true;
        chatId = id;
    }

    public void startChat(long id) {

        ChatSingleActivity.startChatActivity(this,
                BMXMessage.MessageType.Single, id);

    }

    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("请稍候");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isIn = false;
    }

    @Override
    protected void onResume() {
        chatId = 0;
        super.onResume();
    }
}
