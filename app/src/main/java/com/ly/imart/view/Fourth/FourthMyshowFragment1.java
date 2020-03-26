package com.ly.imart.view.Fourth;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ly.imart.R;
import com.ly.imart.bean.Fourth.FriendListBean;
import com.ly.imart.bean.Fourth.MyshowFragmentBean;
import com.ly.imart.model.Fourth.FourthMyshowFragmentModel;
import com.ly.imart.onerecycler.OnCreateVHListener;
import com.ly.imart.onerecycler.OneLoadingLayout;
import com.ly.imart.onerecycler.OneRecyclerView;
import com.ly.imart.onerecycler.OneVH;
import com.ly.imart.util.MyImageView;
import com.ly.imart.view.Second.ArticleActivity;
import com.ly.imart.view.Second.ArticleMainActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class FourthMyshowFragment1 extends Fragment {

    private View view;
    private OneRecyclerView mOneRecyclerView;

    static String   myshow1_userName;

    public static void setUserName(String userName){
        myshow1_userName = userName;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_myshow_fragment1, container, false);
        mOneRecyclerView = (OneRecyclerView) view.findViewById(R.id.myshow1_list);

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
                        return new MyshowFragment1BeanVH(parent);
                    }

                    @Override
                    public boolean isCreate(int position, Object t) {
                        return t instanceof MyshowFragmentBean;
                    }
                }
        );
        mOneRecyclerView.setSpanCount(3);

        return view;
    }
    class MyshowFragment1BeanVH extends OneVH<MyshowFragmentBean>{
        public MyshowFragment1BeanVH(ViewGroup parent) {
            super(parent, R.layout.activity_myshow_fragment1_list);
        }

        @Override
        public void bindView(int position, final MyshowFragmentBean myshowFragment1Bean) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArticleMainActivity.openArticleMainById(view.getContext(),myshowFragment1Bean.getId());
                }
            });
            MyImageView myImageView = itemView.findViewById(R.id.myshow1_list_image);
            Glide.with(view.getContext()).load(myshowFragment1Bean.getImageUrl())
                    .asBitmap()
                    .error(R.drawable.loadingimg)
                    .centerCrop()
                    .into(myImageView);


        }
    }
    private void requestData(final boolean append){
        mOneRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<MyshowFragmentBean> listBeans = fetchData();
                if (append){
//                    mOneRecyclerView.addData(listBeans);//下拉时增加的数据
                }
                else {
                    mOneRecyclerView.setData(listBeans);//刷新
                }
            }
        },1000);
    }

    private List<MyshowFragmentBean> fetchData(){
        List<MyshowFragmentBean> listBeans = null;
        FourthMyshowFragmentModel fourthMyshowFragmentModel = new FourthMyshowFragmentModel();
        try {
            listBeans = fourthMyshowFragmentModel.getMyshow1(myshow1_userName);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listBeans;
    }

}
