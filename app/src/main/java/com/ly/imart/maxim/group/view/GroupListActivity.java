
package com.ly.imart.maxim.group.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import im.floo.floolib.BMXErrorCode;
import im.floo.floolib.BMXGroup;
import im.floo.floolib.BMXGroupList;
import im.floo.floolib.BMXGroupService;
import im.floo.floolib.BMXMessage;
import im.floo.floolib.ListOfLongLong;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import com.ly.imart.R;
import com.ly.imart.maxim.bmxmanager.BaseManager;
import com.ly.imart.maxim.bmxmanager.GroupManager;
import com.ly.imart.maxim.common.base.BaseTitleFragment;
import com.ly.imart.maxim.common.utils.RosterFetcher;
import com.ly.imart.maxim.common.utils.ScreenUtils;
import com.ly.imart.maxim.common.utils.ToastUtil;
import com.ly.imart.maxim.common.utils.dialog.CommonCustomDialog;
import com.ly.imart.maxim.common.utils.dialog.DialogUtils;
import com.ly.imart.maxim.common.view.Header;
import com.ly.imart.maxim.common.view.ImageRequestConfig;
import com.ly.imart.maxim.common.view.ItemLineSwitch;
import com.ly.imart.maxim.common.view.ShapeImageView;
import com.ly.imart.maxim.common.view.recyclerview.BaseViewHolder;
import com.ly.imart.maxim.common.view.recyclerview.DividerItemDecoration;
import com.ly.imart.maxim.common.view.recyclerview.RecyclerWithHFAdapter;
import com.ly.imart.maxim.message.utils.ChatUtils;
import com.ly.imart.maxim.message.view.ChatBaseActivity;

/**
 * Description : 群 Created by Mango on 2018/11/21.
 */
public class GroupListActivity extends BaseTitleFragment {

    private RecyclerView mGroupView;

    private GroupAdapter mAdapter;

    public static int CHOOSE_MEMBER_CODE = 1000;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(getActivity(), headerContainer);
        return builder.build();
    }

    @Override
    protected View onCreateView() {
        hideHeader();
        View view = View.inflate(getActivity(), R.layout.activity_group, null);
        mGroupView = view.findViewById(R.id.group_recycler);
        mGroupView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGroupView
                .addItemDecoration(new DividerItemDecoration(getActivity(), R.color.guide_divider));
        mAdapter = new GroupAdapter(getActivity());
        mGroupView.setAdapter(mAdapter);
        buildContactHeaderView();
        return view;
    }

    @Override
    protected boolean isFullScreen() {
        return false;
    }

    @Override
    protected void setViewListener() {
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BMXGroup item = mAdapter.getItem(position);
                if (item == null) {
                    return;
                }
                ChatBaseActivity.startChatActivity(getActivity(),
                        BMXMessage.MessageType.Group, item.groupId());
            }
        });
    }

    @Override
    protected void initDataForActivity() {
        super.initDataForActivity();
        getAllGroup();
    }

    @Override
    public void onShow() {
        if (mContentView != null) {
            getAllGroup();
        }
    }

    /**
     * 设置联系人headerView
     */
    private void buildContactHeaderView() {
        View headerView = View.inflate(getActivity(), R.layout.item_contact_header, null);
//        FrameLayout search = headerView.findViewById(R.id.fl_contact_header_search);
//        search.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                GroupSearchActivity.openGroupSearch(getActivity());
//            }
//        });
        LinearLayout ll = headerView.findViewById(R.id.ll_contact_header);
//        // 群组
//        View groupView = View.inflate(getActivity(), R.layout.item_contact_view, null);
//        groupView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                RosterChooseActivity.startRosterListActivity(getActivity(), true, true,
//                        CHOOSE_MEMBER_CODE);
//            }
//        });
//        ((TextView)groupView.findViewById(R.id.contact_title))
//                .setText(getString(R.string.create_group));
//        ((ShapeImageView)groupView.findViewById(R.id.contact_avatar))
//                .setImageResource(R.drawable.icon_group);
//        ll.addView(groupView);

        // 邀请通知
        View inviteView = View.inflate(getActivity(), R.layout.item_contact_view, null);
        inviteView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupInviteActivity.openGroupInvite(getActivity());
            }
        });
        ((TextView)inviteView.findViewById(R.id.contact_title))
                .setText(getString(R.string.group_invite));
        ShapeImageView icon = inviteView.findViewById(R.id.contact_avatar);
        icon.setFrameStrokeWidth(0);
        icon.setImageResource(R.drawable.icon_group);
        ll.addView(inviteView);
        mAdapter.addHeaderView(headerView);
    }

    private void getAllGroup() {
        final BMXGroupList list = new BMXGroupList();
        Observable.just(list).map(new Func1<BMXGroupList, BMXErrorCode>() {
            @Override
            public BMXErrorCode call(BMXGroupList bmxGroupList) {
                return GroupManager.getInstance().search(bmxGroupList, false);
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
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        if (list.size() > 0) {
                            // 空的错误不提示
                            String error = e != null ? e.getMessage() : "网络错误";
                            ToastUtil.showTextViewPrompt(error);
                        }
                        GroupManager.getInstance().search(list, false);
                        RosterFetcher.getFetcher().putGroups(list);
                        mAdapter.replaceList(filterGroup(list));
                    }

                    @Override
                    public void onNext(BMXErrorCode errorCode) {
                        RosterFetcher.getFetcher().putGroups(list);
                        mAdapter.replaceList(filterGroup(list));
                    }
                });
    }

    /**
     * 创建群聊
     */
    private void showCreateGroup(final ListOfLongLong members) {
        final LinearLayout ll = new LinearLayout(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams textP = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams editP = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editP.setMargins(ScreenUtils.dp2px(10), ScreenUtils.dp2px(5), ScreenUtils.dp2px(10),
                ScreenUtils.dp2px(5));
        // 名称
        TextView name = new TextView(getActivity());
        name.setPadding(ScreenUtils.dp2px(15), ScreenUtils.dp2px(15), ScreenUtils.dp2px(15),
                ScreenUtils.dp2px(15));
        name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        name.setTextColor(getResources().getColor(R.color.color_black));
        name.setBackgroundColor(getResources().getColor(R.color.color_white));
        name.setText(getString(R.string.group_name));
        ll.addView(name, textP);

        final EditText editName = new EditText(getActivity());
        editName.setBackgroundResource(R.drawable.common_edit_corner_bg);
        editName.setPadding(ScreenUtils.dp2px(5), 0, ScreenUtils.dp2px(5), 0);
        editName.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        editName.setTextColor(getResources().getColor(R.color.color_black));
        editName.setMinHeight(ScreenUtils.dp2px(40));
        ll.addView(editName, editP);

        // 描述
        TextView desc = new TextView(getActivity());
        desc.setPadding(ScreenUtils.dp2px(15), ScreenUtils.dp2px(15), ScreenUtils.dp2px(15),
                ScreenUtils.dp2px(15));
        desc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        desc.setTextColor(getResources().getColor(R.color.color_black));
        desc.setBackgroundColor(getResources().getColor(R.color.color_white));
        desc.setText(getString(R.string.group_desc));
        ll.addView(desc, textP);

        final EditText editDesc = new EditText(getActivity());
        editDesc.setBackgroundResource(R.drawable.common_edit_corner_bg);
        editDesc.setPadding(ScreenUtils.dp2px(5), 0, ScreenUtils.dp2px(5), 0);
        editDesc.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14);
        editDesc.setTextColor(getResources().getColor(R.color.color_black));
        editDesc.setMinHeight(ScreenUtils.dp2px(40));
        ll.addView(editDesc, editP);

        // 公开
        final ItemLineSwitch.Builder isPublic = new ItemLineSwitch.Builder(getActivity()).setLeftText("是否公开")
                .setMarginTop(ScreenUtils.dp2px(15))
                .setOnItemSwitchListener(new ItemLineSwitch.OnItemViewSwitchListener() {
                    @Override
                    public void onItemSwitch(View v, boolean curCheck) {

                    }
                });
        ll.addView(isPublic.build(), textP);

        DialogUtils.getInstance().showCustomDialog(getActivity(), ll, getString(R.string.create_group),
                getString(R.string.confirm), getString(R.string.cancel),
                new CommonCustomDialog.OnDialogListener() {
                    @Override
                    public void onConfirmListener() {
                        String name = editName.getEditableText().toString().trim();
                        String desc = editDesc.getEditableText().toString().trim();
                        boolean publicCheckStatus = isPublic.getCheckStatus();
                        createGroup(members, name, desc, publicCheckStatus);
                    }

                    @Override
                    public void onCancelListener() {

                    }
                });
    }
    
    private List<BMXGroup> filterGroup(BMXGroupList groupList) {
        List<BMXGroup> list = new ArrayList<>();
        if (groupList != null && !groupList.isEmpty()) {
            for (int i = 0; i < groupList.size(); i++) {
                BMXGroup group = groupList.get(i);
                if (group != null && group.groupStatus() != null) {
                    BMXGroup.GroupStatus status = group.groupStatus();
                    if (status == BMXGroup.GroupStatus.Normal) {
                        list.add(group);
                    }
                }
            }
        }
        return list;
    }

    /**
     * 创建群聊
     */
    private void createGroup(ListOfLongLong members, String name, String desc,
                             boolean publicCheckStatus) {
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showTextViewPrompt("群聊名称不能为空");
            return;
        }
        BMXGroupService.CreateGroupOptions options = new BMXGroupService.CreateGroupOptions(name,
                desc, publicCheckStatus);
        options.setMMembers(members);
        final BMXGroup group = new BMXGroup();
        showLoadingDialog(true);
        Observable.just(options).map(new Func1<BMXGroupService.CreateGroupOptions, BMXErrorCode>() {
            @Override
            public BMXErrorCode call(BMXGroupService.CreateGroupOptions createGroupOptions) {
                return GroupManager.getInstance().create(createGroupOptions, group);
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
                        dismissLoadingDialog();
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissLoadingDialog();
                        String error = e != null ? e.getMessage() : "创建失败";
                        ToastUtil.showTextViewPrompt(error);
                    }

                    @Override
                    public void onNext(BMXErrorCode errorCode) {
                        ChatBaseActivity.startChatActivity(getActivity(),
                                BMXMessage.MessageType.Group, group.groupId());
                    }
                });
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == CHOOSE_MEMBER_CODE && resultCode == Activity.RESULT_OK && data != null) {
//            List<Long> chooseList = (List<Long>)data
//                    .getSerializableExtra(ChatGroupListMemberActivity.CHOOSE_DATA);
//            ListOfLongLong members = new ListOfLongLong();
//            if (chooseList != null && chooseList.size() > 0) {
//                for (Long id : chooseList) {
//                    members.add(id);
//                }
//            }
//            showCreateGroup(members);
//        }
//    }

    private class GroupAdapter extends RecyclerWithHFAdapter<BMXGroup> {

        private ImageRequestConfig mConfig;

        public GroupAdapter(Context context) {
            super(context);
            mConfig = new ImageRequestConfig.Builder().cacheInMemory(true)
                    .showImageForEmptyUri(R.drawable.default_group_icon)
                    .showImageOnFail(R.drawable.default_group_icon)
                    .cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageOnLoading(R.drawable.default_group_icon).build();
        }

        @Override
        protected int onCreateViewById(int viewType) {
            return R.layout.item_contact_view;
        }

        @Override
        protected void onBindHolder(BaseViewHolder holder, int position) {
            ShapeImageView avatar = holder.findViewById(R.id.contact_avatar);
            TextView title = holder.findViewById(R.id.contact_title);
            BMXGroup bean = getItem(position);
            String name = bean == null ? "" : bean.name();
            title.setText(name);
            ChatUtils.getInstance().showGroupAvatar(bean, avatar, mConfig);
        }
    }
}
