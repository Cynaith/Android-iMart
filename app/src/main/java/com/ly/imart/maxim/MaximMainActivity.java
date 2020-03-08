
package com.ly.imart.maxim;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import com.ly.imart.R;
import com.ly.imart.maxim.common.base.BaseSwitchActivity;
import com.ly.imart.maxim.common.utils.CommonConfig;
import com.ly.imart.maxim.common.utils.RxBus;
import com.ly.imart.maxim.contact.view.AllContactFragment;
import com.ly.imart.maxim.message.view.SessionFragment;
import com.ly.imart.maxim.push.NotificationUtils;
import com.ly.imart.maxim.push.PushClientMgr;
import com.ly.imart.maxim.login.view.MineFragment;

/**
 * Description : 首页view Created by Mango on 2018/11/05.
 */
public class MaximMainActivity extends BaseSwitchActivity {

    protected TabSwitchView mSessionTab;

    protected TabSwitchView mContactTab;

    protected TabSwitchView mSettingTab;

    public static void openMain(Context context) {
        Intent intent = new Intent(context, MaximMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        //((Activity)context).finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PushClientMgr.getManager().register(this);
        initRxBus();
    }

    @Override
    protected void initFragment(final List<TabSwitchView> tabSwitch) {
        mSessionTab = new BaseSwitchActivity.TabSwitchView(R.drawable.message_icon_selector,
                R.string.tab_chat, new SessionFragment(), 0);
        tabSwitch.add(mSessionTab);

        mContactTab = new BaseSwitchActivity.TabSwitchView(R.drawable.contact_icon_selector,
                R.string.tab_contact, new AllContactFragment(), 1);
        tabSwitch.add(mContactTab);

        mSettingTab = new BaseSwitchActivity.TabSwitchView(R.drawable.mine_icon_selector,
                R.string.tab_mine, new MineFragment(), 2);
        tabSwitch.add(mSettingTab);
    }

    @Override
    protected void onTabClick() {
        super.onTabClick();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCurrentFragment != null) {
            mCurrentFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void initRxBus() {
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
                        if (mSessionTab != null && TextUtils.equals(action, CommonConfig.SESSION_COUNT_ACTION)) {
                            int count = intent.getIntExtra(CommonConfig.TAB_COUNT, 0);
                            mSessionTab.setCount(count);
                            NotificationUtils.getInstance().setCorner(null, count);
                        }
                    }
                });
    }
}
