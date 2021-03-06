
package com.ly.imart.maxim.login.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import im.floo.floolib.BMXDevice;
import im.floo.floolib.BMXDeviceList;
import im.floo.floolib.BMXErrorCode;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import com.ly.imart.R;
import com.ly.imart.maxim.bmxmanager.BaseManager;
import com.ly.imart.maxim.bmxmanager.UserManager;
import com.ly.imart.maxim.common.base.BaseTitleActivity;
import com.ly.imart.maxim.common.utils.ToastUtil;
import com.ly.imart.maxim.common.view.Header;
import com.ly.imart.maxim.common.view.recyclerview.BaseViewHolder;
import com.ly.imart.maxim.common.view.recyclerview.DividerItemDecoration;
import com.ly.imart.maxim.common.view.recyclerview.RecyclerWithHFAdapter;

/**
 * Description : 多设备列表 Created by Mango on 2018/11/06
 */
public class DeviceListActivity extends BaseTitleActivity {

    private RecyclerView mRecycler;

    private DeviceAdapter mAdapter;

    private BMXDeviceList deviceList = new BMXDeviceList();

    public static void startDeviceActivity(Context context) {
        Intent intent = new Intent(context, DeviceListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(this, headerContainer);
        builder.setTitle(R.string.device_list);
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
        mAdapter = new DeviceAdapter(this);
        mRecycler.setAdapter(mAdapter);
        return view;
    }

    private void deleteDevice(final int deviceSn) {
        showLoadingDialog(true);
        Observable.just("").map(new Func1<String, BMXErrorCode>() {
            @Override
            public BMXErrorCode call(String s) {
                return UserManager.getInstance().deleteDevice(deviceSn);
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
                        String error = e != null ? e.getMessage() : "网络错误";
                        ToastUtil.showTextViewPrompt(error);
                    }

                    @Override
                    public void onNext(BMXErrorCode errorCode) {
                        init();
                    }
                });
    }

    @Override
    protected void initDataForActivity() {
        super.initDataForActivity();
        init();
    }

    protected void init() {
        if (deviceList != null && !deviceList.isEmpty()) {
            deviceList.clear();
        }
        showLoadingDialog(true);
        Observable.just("").map(new Func1<String, BMXErrorCode>() {
            @Override
            public BMXErrorCode call(String s) {
                return UserManager.getInstance().getDeviceList(deviceList);
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
                        String error = e != null ? e.getMessage() : "网络错误";
                        ToastUtil.showTextViewPrompt(error);
                    }

                    @Override
                    public void onNext(BMXErrorCode errorCode) {
                        bindData();
                    }
                });
    }

    private void bindData() {
        List<BMXDevice> devices = new ArrayList<>();
        for (int i = 0; i < deviceList.size(); i++) {
            devices.add(deviceList.get(i));
        }
        mAdapter.replaceList(devices);
    }

    /**
     * 展示设备adapter
     */
    protected class DeviceAdapter extends RecyclerWithHFAdapter<BMXDevice> {

        public DeviceAdapter(Context context) {
            super(context);
        }

        @Override
        protected int onCreateViewById(int viewType) {
            return R.layout.item_device_view;
        }

        @Override
        protected void onBindHolder(BaseViewHolder holder, int position) {
            TextView tvDeviceSN = holder.findViewById(R.id.tv_device_sn);
            TextView tvDeviceAgent = holder.findViewById(R.id.tv_device_agent);
            TextView quit = holder.findViewById(R.id.tv_quit);

            final BMXDevice device = getItem(position);
            if (device == null) {
                return;
            }
            // 退出
            quit.setOnClickListener(v -> deleteDevice(device.deviceSN()));
            long platform = device.platform();
            // 当前设备没有退出按钮
            boolean isCurrent = device.isCurrentDevice();
            quit.setVisibility(isCurrent ? View.GONE : View.VISIBLE);
            tvDeviceSN.setText("设备序列号:" + device.deviceSN());
            tvDeviceAgent.setText(TextUtils.isEmpty(device.userAgent()) ? "" : device.userAgent());
        }
    }
}
