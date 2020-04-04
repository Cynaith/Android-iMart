package com.ly.imart.view.Fourth;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ly.imart.R;
import com.ly.imart.bean.Fourth.FriendListBean;
import com.ly.imart.onerecycler.OnCreateVHListener;
import com.ly.imart.onerecycler.OneLoadingLayout;
import com.ly.imart.onerecycler.OneRecyclerView;
import com.ly.imart.onerecycler.OneVH;
import com.ly.imart.presenter.Fourth.FollowListPresenter;
import com.ly.imart.util.CircleImageView;
import com.ly.imart.view.Second.ArticleActivity;
import com.ly.imart.view.Second.ArticleMainActivity;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FourthFollowlistActivity extends AppCompatActivity implements IFollowListView {


    private OneRecyclerView mOneRecyclerView;

    //    kind = 1 follow
//    kind = 2 followed
    static int kind;
    static String userName;
    FollowListPresenter followListPresenter;
    TextView listTitle=null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friendlist);
        Intent intent = getIntent();
        kind = intent.getIntExtra("kind",1);
        userName = intent.getStringExtra("userName");
        listTitle = findViewById(R.id.follow_title);
        if (kind==1) listTitle.setText("关注列表");
        else if (kind==2) listTitle.setText("粉丝列表");
        else listTitle.setText("文章列表");
        followListPresenter = new FollowListPresenter(this);
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
                        return new FriendListBeanVH(parent);
                    }

                    @Override
                    public boolean isCreate(int position, Object t) {
                        return t instanceof FriendListBean;
                    }
                }
        );


    }

    @Override
    public void gotoUserShow(String username) {
        Intent intent = new Intent(this,FourthMyshowActivity.class);
        intent.putExtra("userName",username);
        startActivity(intent);
    }


    class FriendListBeanVH extends OneVH<FriendListBean> {
        public FriendListBeanVH(ViewGroup parent) {
            super(parent, R.layout.activity_friendlist_list);
        }

        @Override
        public void bindView(int position, final FriendListBean friendListBean) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (kind ==1 ||kind==2)
                    followListPresenter.gotoUserPage(friendListBean.getName());
                    else {//如果是文章列表
                        ArticleMainActivity.openArticleMainById(FourthFollowlistActivity.this,friendListBean.getArticleId());
                    }

//                    Toast.makeText(view.getContext(), friendListBean.getName(), Toast.LENGTH_SHORT).show();
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
        System.out.println(userName);
        try {
            if (kind == 1)
                listBeans = followListPresenter.getFriendList(userName);
            else if (kind == 2)
                listBeans = followListPresenter.getFriendedList(userName);
            else listBeans = followListPresenter.getArticleList(userName);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        for(int i=0;i<26;i++){
//            FriendListBean friendListBean = new FriendListBean();
//            friendListBean.setName("name:"+i);
//            friendListBean.setContent("content"+i);
//            listBeans.add(friendListBean);
//        }
        return listBeans;
    }
}
