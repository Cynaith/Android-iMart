
package com.ly.imart.maxim.group.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import im.floo.floolib.BMXErrorCode;
import im.floo.floolib.BMXGroup;
import im.floo.floolib.BMXMessage;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import com.ly.imart.R;
import com.ly.imart.maxim.bmxmanager.AppManager;
import com.ly.imart.maxim.bmxmanager.BaseManager;
import com.ly.imart.maxim.bmxmanager.GroupManager;
import com.ly.imart.maxim.common.base.BaseTitleActivity;
import com.ly.imart.maxim.common.utils.RosterFetcher;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.maxim.common.utils.ToastUtil;
import com.ly.imart.maxim.common.view.Header;
import com.ly.imart.maxim.common.view.ImageRequestConfig;
import com.ly.imart.maxim.common.view.ShapeImageView;
import com.ly.imart.maxim.message.utils.ChatUtils;
import com.ly.imart.maxim.message.utils.MessageConfig;
import com.ly.imart.maxim.message.view.ChatGroupActivity;
import com.ly.imart.maxim.net.HttpResponseCallback;

/**
 * Description : 群二维码详情 Created by Mango on 2018/11/21.
 */
public class GroupQrcodeDetailActivity extends BaseTitleActivity {

    public static final String QR_INFO = "qr_info";

    private ShapeImageView mUserIcon;

    private TextView mUserName;

    private TextView mUserId;

    /* 开始聊天 */
    private TextView mTvJoin;

    private long mGroupId;

    private String mQrInfo;

    private ImageRequestConfig mConfig = new ImageRequestConfig.Builder().cacheInMemory(true)
            .showImageForEmptyUri(R.drawable.default_group_icon)
            .showImageOnFail(R.drawable.default_group_icon).bitmapConfig(Bitmap.Config.RGB_565)
            .cacheOnDisk(true).showImageOnLoading(R.drawable.default_group_icon).build();

    public static void openGroupQrcodeDetail(Context context, long groupId, String qrInfo) {
        Intent intent = new Intent(context, GroupQrcodeDetailActivity.class);
        intent.putExtra(MessageConfig.CHAT_ID, groupId);
        intent.putExtra(QR_INFO, qrInfo);
        context.startActivity(intent);
    }

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(this, headerContainer);
        builder.setTitle(R.string.group_qrcode);
        builder.setBackIcon(R.drawable.header_back_icon, v -> finish());
        return builder.build();
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(this, R.layout.activity_group_qrcode_detail, null);
        mUserIcon = view.findViewById(R.id.iv_user_avatar);
        mUserName = view.findViewById(R.id.tv_user_name);
        mUserId = view.findViewById(R.id.tv_user_id);
        mTvJoin = view.findViewById(R.id.tv_confirm_join);
        return view;
    }

    @Override
    protected void initDataFromFront(Intent intent) {
        super.initDataFromFront(intent);
        if (intent != null) {
            mGroupId = intent.getLongExtra(MessageConfig.CHAT_ID, 0);
            mQrInfo = intent.getStringExtra(QR_INFO);
        }
    }

    @Override
    protected void setViewListener() {
        mTvJoin.setOnClickListener(v -> getToken());
    }

    @Override
    protected void initDataForActivity() {
        super.initDataForActivity();
        initGroup();
    }

    private void initGroup() {
        mUserId.setText(mGroupId <= 0 ? "" : "群Id:" + mGroupId);
        if (mGroupId <= 0) {
            return;
        }
        showLoadingDialog(true);
        BMXGroup group = new BMXGroup();
        Observable.just(mGroupId).map(new Func1<Long, BMXErrorCode>() {
            @Override
            public BMXErrorCode call(Long aLong) {
                return GroupManager.getInstance().search(mGroupId, group, true);
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
                        GroupManager.getInstance().search(mGroupId, group, false);
                        bindGroup(group);
                    }

                    @Override
                    public void onNext(BMXErrorCode errorCode) {
                        dismissLoadingDialog();
                        bindGroup(group);
                        RosterFetcher.getFetcher().putGroup(group);
                    }
                });
    }

    private void bindGroup(BMXGroup group) {
        if (group.isMember()) {
            // 群成员直接跳转
            ChatGroupActivity.startChatActivity(this, BMXMessage.MessageType.Group, mGroupId);
            finish();
            return;
        }
        String name = group.name();
        ChatUtils.getInstance().showGroupAvatar(group, mUserIcon, mConfig);
        mUserName.setText(TextUtils.isEmpty(name) ? "" : name);
    }

    private void getToken() {
        showLoadingDialog(true);
        AppManager.getInstance().getTokenByName(SharePreferenceUtils.getInstance().getUserName(),
                SharePreferenceUtils.getInstance().getUserPwd(),
                new HttpResponseCallback<String>() {
                    @Override
                    public void onResponse(String result) {
                        joinGroup(result);
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg, Throwable t) {
                        dismissLoadingDialog();
                        ToastUtil.showTextViewPrompt("加入失败");
                    }
                });
    }

    public void joinGroup(String token) {
        AppManager.getInstance().groupInvite(token, mQrInfo, new HttpResponseCallback<Boolean>() {
            @Override
            public void onResponse(Boolean result) {
                dismissLoadingDialog();
                ToastUtil.showTextViewPrompt("申请成功");
                finish();
            }

            @Override
            public void onFailure(int errorCode, String errorMsg, Throwable t) {
                dismissLoadingDialog();
                ToastUtil.showTextViewPrompt(TextUtils.isEmpty(errorMsg) ? "申请失败" : errorMsg);
            }
        });
    }

}
