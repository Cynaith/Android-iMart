
package com.ly.imart.maxim.contact.view;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import im.floo.floolib.BMXErrorCode;
import im.floo.floolib.BMXMessage;
import im.floo.floolib.BMXRosterItem;
import im.floo.floolib.BMXRosterItemList;
import im.floo.floolib.ListOfLongLong;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import com.ly.imart.R;
import com.ly.imart.maxim.bmxmanager.BaseManager;
import com.ly.imart.maxim.bmxmanager.RosterManager;
import com.ly.imart.maxim.common.base.BaseTitleActivity;
import com.ly.imart.maxim.common.bean.MessageBean;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.maxim.common.utils.ToastUtil;
import com.ly.imart.maxim.common.view.Header;
import com.ly.imart.maxim.common.view.ShapeImageView;
import com.ly.imart.maxim.common.view.recyclerview.DividerItemDecoration;
import com.ly.imart.maxim.contact.adapter.ContactAdapter;
import com.ly.imart.maxim.group.view.ForwardMsgGroupActivity;
import com.ly.imart.maxim.message.utils.MessageConfig;
import com.ly.imart.maxim.sdk.utils.MessageSendUtils;

/**
 * Description : 转发 Created by Mango on 2018/11/06
 */
public class ForwardMsgRosterActivity extends BaseTitleActivity {

    private RecyclerView mRecycler;

    private ContactAdapter mAdapter;

    private MessageBean messageBean;

    private long mUserId;

    private MessageSendUtils mSendUtils;

    private final int FORWARD_GROUP_REQUEST = 1000;

    public static void openForwardMsgRosterActivity(Activity context, MessageBean message, int requestCode) {
        Intent intent = new Intent(context, ForwardMsgRosterActivity.class);
        intent.putExtra(MessageConfig.CHAT_MSG, message);
        context.startActivityForResult(intent, requestCode);
    }

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(this, headerContainer);
        builder.setTitle("转发");
        builder.setBackIcon(R.drawable.header_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return builder.build();
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(this, R.layout.fragment_contact, null);
        mRecycler = view.findViewById(R.id.contact_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(this));
        mRecycler.addItemDecoration(new DividerItemDecoration(this, R.color.guide_divider));
        mAdapter = new ContactAdapter(this);
        mRecycler.setAdapter(mAdapter);
        buildContactHeaderView();
        return view;
    }

    @Override
    protected void initDataFromFront(Intent intent) {
        super.initDataFromFront(intent);
        if (intent != null) {
            messageBean = (MessageBean)intent.getSerializableExtra(MessageConfig.CHAT_MSG);
        }
        mUserId = SharePreferenceUtils.getInstance().getUserId();
        mSendUtils = new MessageSendUtils();
    }

    /**
     * 设置联系人headerView
     */
    private void buildContactHeaderView() {
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);
        // 群组
        View groupView = View.inflate(this, R.layout.item_contact_view, null);
        groupView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ForwardMsgGroupActivity.openForwardMsgGroupActivity(ForwardMsgRosterActivity.this,
                        messageBean, FORWARD_GROUP_REQUEST);
            }
        });
        ((TextView)groupView.findViewById(R.id.contact_title))
                .setText(getString(R.string.contact_group));
        ((ShapeImageView)groupView.findViewById(R.id.contact_avatar))
                .setImageResource(R.drawable.icon_group);
        ll.addView(groupView);
        mAdapter.addHeaderView(ll);
    }

    @Override
    protected void setViewListener() {
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BMXRosterItem item = mAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                forwardMessage(item.rosterId());
            }
        });
    }

    private void forwardMessage(long rosterId) {
        if (messageBean == null) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(MessageConfig.CHAT_ID, rosterId);
        intent.putExtra(MessageConfig.CHAT_TYPE, BMXMessage.MessageType.Single);
        intent.putExtra(MessageConfig.CHAT_MSG, messageBean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void initDataForActivity() {
        super.initDataForActivity();
        final ListOfLongLong listOfLongLong = new ListOfLongLong();
        final BMXRosterItemList itemList = new BMXRosterItemList();
        Observable.just(listOfLongLong).map(new Func1<ListOfLongLong, BMXErrorCode>() {
            @Override
            public BMXErrorCode call(ListOfLongLong longs) {
                return RosterManager.getInstance().get(longs, false);
            }
        }).flatMap(new Func1<BMXErrorCode, Observable<BMXErrorCode>>() {
            @Override
            public Observable<BMXErrorCode> call(BMXErrorCode errorCode) {
                return BaseManager.bmxFinish(errorCode, errorCode);
            }
        }).map(new Func1<BMXErrorCode, BMXErrorCode>() {
            @Override
            public BMXErrorCode call(BMXErrorCode errorCode) {
                return RosterManager.getInstance().search(listOfLongLong, itemList, true);
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
                        String error = e != null ? e.getMessage() : "网络错误";
                        ToastUtil.showTextViewPrompt(error);
                        RosterManager.getInstance().get(listOfLongLong, false);
                        List<BMXRosterItem> rosterItems = new ArrayList<>();
                        for (int i = 0; i < itemList.size(); i++) {
                            rosterItems.add(itemList.get(i));
                        }
                        mAdapter.replaceList(rosterItems);
                    }

                    @Override
                    public void onNext(BMXErrorCode errorCode) {
                        List<BMXRosterItem> rosterItems = new ArrayList<>();
                        for (int i = 0; i < itemList.size(); i++) {
                            rosterItems.add(itemList.get(i));
                        }
                        mAdapter.replaceList(rosterItems);
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FORWARD_GROUP_REQUEST && resultCode == RESULT_OK) {
            if (data != null) {
                Intent intent = new Intent();
                intent.putExtra(MessageConfig.CHAT_ID, data.getLongExtra(MessageConfig.CHAT_ID, 0));
                intent.putExtra(MessageConfig.CHAT_TYPE,
                        data.getSerializableExtra(MessageConfig.CHAT_TYPE));
                intent.putExtra(MessageConfig.CHAT_MSG, messageBean);
                setResult(RESULT_OK, intent);
            }
            finish();
        }
    }
}
