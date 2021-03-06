
package com.ly.imart.maxim.login.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import im.floo.floolib.BMXErrorCode;
import im.floo.floolib.BMXUserProfile;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import com.ly.imart.R;
import com.ly.imart.maxim.bmxmanager.BaseManager;
import com.ly.imart.maxim.bmxmanager.UserManager;
import com.ly.imart.maxim.common.base.BaseTitleActivity;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.maxim.common.view.Header;
import com.ly.imart.maxim.common.view.ImageRequestConfig;
import com.ly.imart.maxim.common.view.ShapeImageView;
import com.ly.imart.maxim.message.utils.ChatUtils;
import com.ly.imart.maxim.scan.utils.QRCodeShowUtils;

/**
 * Description : 我的二维码 Created by sunpengfei on 2019-06-26.
 */
public class MyQrCodeActivity extends BaseTitleActivity {

    private ShapeImageView mUserIcon;

    private TextView mUserName;

    private TextView mUserId;

    private TextView mNickName;

    private ImageView mIvQrCode;

    private ImageRequestConfig mConfig = new ImageRequestConfig.Builder().cacheInMemory(true)
            .showImageForEmptyUri(R.drawable.default_avatar_icon)
            .showImageOnFail(R.drawable.default_avatar_icon).cacheOnDisk(true)
            .bitmapConfig(Bitmap.Config.RGB_565).showImageOnLoading(R.drawable.default_avatar_icon)
            .build();

    public static void openMyQrcode(Context context) {
        Intent intent = new Intent(context, MyQrCodeActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(this, headerContainer);
        builder.setTitle(R.string.my_qrcode);
        builder.setBackIcon(R.drawable.header_back_icon, v -> finish());
        return builder.build();
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(this, R.layout.activity_qrcode_detail, null);
        mUserIcon = view.findViewById(R.id.iv_user_avatar);
        mUserName = view.findViewById(R.id.tv_user_name);
        mNickName = view.findViewById(R.id.tv_nick_name);
        mUserId = view.findViewById(R.id.tv_user_id);
        mIvQrCode = view.findViewById(R.id.iv_qrcode);
        return view;
    }

    @Override
    protected void initDataForActivity() {
        super.initDataForActivity();
        initUser();

    }

    private void initUser() {
        initQrCode();
        final BMXUserProfile profile = new BMXUserProfile();
        Observable.just(profile)
                .map(userProfile -> UserManager.getInstance().getProfile(userProfile, false))
                .flatMap(errorCode -> BaseManager.bmxFinish(errorCode, errorCode))
                .subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<BMXErrorCode>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(BMXErrorCode errorCode) {
                        String name = profile.username();
                        String nickName = profile.nickname();
                        ChatUtils.getInstance().showProfileAvatar(profile, mUserIcon, mConfig);
                        long userId = profile.userId();
                        mUserName.setText(TextUtils.isEmpty(name) ? "" : name);
                        mNickName.setText(TextUtils.isEmpty(nickName) ? "" : "昵称:" + nickName);
                        mUserId.setText(userId <= 0 ? "" : "BMXID:" + userId);
                    }
                });
    }

    /**
     * 设置二维码
     */
    private void initQrCode() {
        String qrUrl = QRCodeShowUtils.generateRosterQRCode(
                String.valueOf(SharePreferenceUtils.getInstance().getUserId()));
        Drawable drawable = QRCodeShowUtils.generateDrawable(qrUrl);
        mIvQrCode.setImageDrawable(drawable);
    }
}
