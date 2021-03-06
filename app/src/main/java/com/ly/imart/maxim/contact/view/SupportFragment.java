
package com.ly.imart.maxim.contact.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import im.floo.floolib.BMXMessage;
import im.floo.floolib.BMXRosterItem;
import com.ly.imart.R;
import com.ly.imart.maxim.bmxmanager.AppManager;
import com.ly.imart.maxim.common.base.BaseTitleFragment;
import com.ly.imart.maxim.common.utils.RosterFetcher;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.maxim.common.view.Header;
import com.ly.imart.maxim.common.view.ImageRequestConfig;
import com.ly.imart.maxim.common.view.ShapeImageView;
import com.ly.imart.maxim.common.view.recyclerview.BaseViewHolder;
import com.ly.imart.maxim.common.view.recyclerview.DividerItemDecoration;
import com.ly.imart.maxim.common.view.recyclerview.RecyclerWithHFAdapter;
import com.ly.imart.maxim.contact.bean.SupportBean;
import com.ly.imart.maxim.message.utils.ChatUtils;
import com.ly.imart.maxim.message.view.ChatSingleActivity;
import com.ly.imart.maxim.net.HttpResponseCallback;
import com.ly.imart.maxim.scan.config.ScanConfigs;

/**
 * Description : 支持 Created by Mango on 2018/11/06
 */
public class SupportFragment extends BaseTitleFragment {

    private RecyclerView mRecycler;

    private SupportAdapter mAdapter;

    private View mEmptyView;

    private TextView mTvEmpty;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(getActivity(), headerContainer);
        return builder.build();
    }

    @Override
    protected View onCreateView() {
        hideHeader();
        View view = View.inflate(getActivity(), R.layout.fragment_contact, null);
        mEmptyView = view.findViewById(R.id.view_empty);
        mTvEmpty = view.findViewById(R.id.tv_empty);
        mEmptyView.setVisibility(View.GONE);
        mRecycler = view.findViewById(R.id.contact_recycler);
        mRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecycler
                .addItemDecoration(new DividerItemDecoration(getActivity(), R.color.guide_divider));
        mAdapter = new SupportAdapter(getActivity());
        mRecycler.setAdapter(mAdapter);
        return view;
    }

    @Override
    protected boolean isFullScreen() {
        return false;
    }

    @Override
    protected void setViewListener() {
        mAdapter.setOnItemClickListener((parent, view, position, id) -> {
            SupportBean bean = mAdapter.getItem(position);
            if (bean != null) {
                ChatSingleActivity.startChatActivity(getActivity(), BMXMessage.MessageType.Single,
                        bean.getUser_id());
            }
        });
    }

    @Override
    protected void initDataForActivity() {
        super.initDataForActivity();
        initSupport();
    }

    @Override
    public void onShow() {
        if (mContentView != null) {
            initSupport();
        }
    }

    private void initSupport() {
        String appId = SharePreferenceUtils.getInstance().getAppId();
        if (!TextUtils.equals(appId, ScanConfigs.CODE_APP_ID)) {
            // 非默认appId
            showEmpty(getString(R.string.support_empty));
            return;
        }
        AppManager.getInstance().getTokenByName(SharePreferenceUtils.getInstance().getUserName(),
                SharePreferenceUtils.getInstance().getUserPwd(),
                new HttpResponseCallback<String>() {
                    @Override
                    public void onResponse(String result) {
                        AppManager.getInstance().getSupportStaff(result,
                                new HttpResponseCallback<List<SupportBean>>() {

                                    @Override
                                    public void onResponse(List<SupportBean> result) {
                                        if (result != null && !result.isEmpty()) {
                                            mAdapter.replaceList(result);
                                            mRecycler.setVisibility(View.VISIBLE);
                                            mEmptyView.setVisibility(View.GONE);
                                        } else {
                                            showEmpty(getString(R.string.common_empty));
                                        }
                                    }

                                    @Override
                                    public void onFailure(int errorCode, String errorMsg,
                                            Throwable t) {
                                        showEmpty(getString(R.string.common_empty));
                                    }
                                });
                    }

                    @Override
                    public void onFailure(int errorCode, String errorMsg, Throwable t) {

                    }
                });
    }

    private void showEmpty(String text) {
        mRecycler.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.VISIBLE);
        mTvEmpty.setText(text);
    }

    @Override
    public void onDestroyView() {
        setNull(mRecycler);
        super.onDestroyView();
    }

    private class SupportAdapter extends RecyclerWithHFAdapter<SupportBean> {

        private ImageRequestConfig mConfig;

        public SupportAdapter(Context context) {
            super(context);
            mConfig = new ImageRequestConfig.Builder().cacheInMemory(true)
                    .showImageForEmptyUri(R.drawable.default_avatar_icon)
                    .showImageOnFail(R.drawable.default_avatar_icon).cacheOnDisk(true)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .showImageOnLoading(R.drawable.default_avatar_icon).build();
        }

        @Override
        protected int onCreateViewById(int viewType) {
            return R.layout.item_contact_view;
        }

        @Override
        protected void onBindHolder(BaseViewHolder holder, int position) {
            SupportBean bean = getItem(position);
            ShapeImageView avatar = holder.findViewById(R.id.contact_avatar);
            TextView title = holder.findViewById(R.id.contact_title);
            TextView sticky = holder.findViewById(R.id.contact_sticky);
            sticky.setVisibility(View.GONE);
            if (bean == null) {
                return;
            }
            String userName = "", nickName = "";
            userName = bean.getUsername();
            nickName = bean.getNickname();
            if (!TextUtils.isEmpty(nickName)) {
                title.setText(nickName);
            } else if (!TextUtils.isEmpty(userName)) {
                title.setText(userName);
            } else {
                title.setText("");
            }

            BMXRosterItem rosterItem = RosterFetcher.getFetcher().getRoster(bean.getUser_id());
            ChatUtils.getInstance().showRosterAvatar(rosterItem, avatar, mConfig);
        }
    }
}
