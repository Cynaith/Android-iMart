package com.ly.imart.view.Second;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ly.imart.R;
import com.ly.imart.bean.Fourth.MyshowFragmentBean;
import com.ly.imart.bean.Second.SecondFragmentBean;
import com.ly.imart.model.Second.SecondFragmentViewPageModel;
import com.ly.imart.onerecycler.OnCreateVHListener;
import com.ly.imart.onerecycler.OneLoadingLayout;
import com.ly.imart.onerecycler.OneRecyclerView;
import com.ly.imart.onerecycler.OneVH;
import com.ly.imart.util.MyImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecondFragmentViewpage3 extends Fragment {
    private View view;
    private OneRecyclerView mOneRecyclerView;

//    @BindView(R.id.second_fragment_viewPager3_list_imageview)
MyImageView imageView;

//    @BindView(R.id.second_fragment_viewPager3_list_name)
    TextView textView_name;

//    @BindView(R.id.second_fragment_viewPager3_list_price)
    TextView textView_price;

//    @BindView(R.id.second_fragment_viewPager3_list_fire)
    TextView textView_fire;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.second_fragment_viewpage3, container, false);

        mOneRecyclerView = (OneRecyclerView) view.findViewById(R.id.second_fragment_viewPager3_list);

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
                        return new SecondFragmentBeanVH(parent);
                    }

                    @Override
                    public boolean isCreate(int position, Object t) {
                        return t instanceof MyshowFragmentBean;
                    }
                }
        );
        mOneRecyclerView.setSpanCount(2);


        return view;
    }


    class SecondFragmentBeanVH extends OneVH<SecondFragmentBean> {
        public SecondFragmentBeanVH(ViewGroup parent) {
            super(parent, R.layout.second_fragment_viewpage3_list);
        }

        @Override
        public void bindView(int position, final SecondFragmentBean secondFragmentBean) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ArticleMainActivity.openArticleMainById(view.getContext(),secondFragmentBean.getId());
                }
            });
            imageView = (MyImageView) itemView.findViewById(R.id.second_fragment_viewPager3_list_imageview);
            textView_name = (TextView) itemView.findViewById(R.id.second_fragment_viewPager3_list_name);
            textView_price = (TextView) itemView.findViewById(R.id.second_fragment_viewPager3_list_price);
            textView_fire = (TextView) itemView.findViewById(R.id.second_fragment_viewPager3_list_fire);
            Glide.with(view.getContext()).load(secondFragmentBean.getImageUrl())
                    .asBitmap()
                    .error(R.drawable.loadingimg)
                    .centerCrop()
                    .into(imageView);
            textView_name.setText(secondFragmentBean.getName());
            textView_price.setText("热度: "+secondFragmentBean.getFire()*20);
            textView_fire.setText(secondFragmentBean.getUsername());
        }
    }
    private void requestData(final boolean append){
        mOneRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<SecondFragmentBean> listBeans = fetchData();
                if (append){
                    //mOneRecyclerView.addData(listBeans);//下拉时增加的数据
                }
                else {
                    mOneRecyclerView.setData(listBeans);//刷新
                }
            }
        },1000);
    }

    private List<SecondFragmentBean> fetchData(){
        List<SecondFragmentBean> listBeans = null;
        SecondFragmentViewPageModel secondFragmentViewPageModel = new SecondFragmentViewPageModel();
        try {
            listBeans = secondFragmentViewPageModel.getKindArticle(3);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return listBeans;
    }

}
