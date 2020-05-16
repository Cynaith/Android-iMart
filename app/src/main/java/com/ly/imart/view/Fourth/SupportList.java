package com.ly.imart.view.Fourth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ly.imart.R;
import com.ly.imart.bean.Fourth.FriendListBean;
import com.ly.imart.model.Fourth.SupportModel;
import com.ly.imart.onerecycler.OnCreateVHListener;
import com.ly.imart.onerecycler.OneLoadingLayout;
import com.ly.imart.onerecycler.OneRecyclerView;
import com.ly.imart.onerecycler.OneVH;
import com.ly.imart.util.CircleImageView;
import com.ly.imart.view.Second.ArticleMainActivity;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class SupportList extends AppCompatActivity {

    private OneRecyclerView mOneRecyclerView;
    private SupportModel supportModel;
    private TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);
        supportModel = new SupportModel();
        textView = findViewById(R.id.follow_title);
        textView.setText("点赞动态");
        mOneRecyclerView = (OneRecyclerView) findViewById(R.id.friendlist);
        mOneRecyclerView.init(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        requestData(false);
                    }
                },
                new OneLoadingLayout.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        requestData(true);
                    }
                },
                new OnCreateVHListener() {
                    @Override
                    public OneVH onCreateHolder(ViewGroup parent) {
                        return new SupportListBeanVH(parent);
                    }

                    @Override
                    public boolean isCreate(int position, Object t) {
                        return t instanceof FriendListBean;
                    }
                }
        );

    }
    class SupportListBeanVH extends OneVH<FriendListBean> {
        public SupportListBeanVH(ViewGroup parent) {
            super(parent, R.layout.activity_friendlist_list);
        }

        @Override
        public void bindView(int position, final FriendListBean friendListBean) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            TextView textView_friendlist_name = itemView.findViewById(R.id.friendlist_username);
            TextView textView_friendlist_content = itemView.findViewById(R.id.friendlist_content);
            CircleImageView userImg = itemView.findViewById(R.id.friendlist_userImage);
            textView_friendlist_name.setText(friendListBean.getName());
            textView_friendlist_content.setText(friendListBean.getContent());
            userImg.setImageURL(friendListBean.getImageUrl());
        }
    }

    private void requestData(final boolean append) {
        mOneRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<FriendListBean> listBeans = fetchData();
                if (append) {
                    mOneRecyclerView.setData(fetchData());//下拉时增加的数据
                } else {
                    mOneRecyclerView.setData(listBeans);//刷新
                }
            }
        }, 1000);
    }

    private List<FriendListBean> fetchData() {
        List<FriendListBean> listBeans = null;
        try {
            listBeans = supportModel.getUserimg();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listBeans;
    }
}
