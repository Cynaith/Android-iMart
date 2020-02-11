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

import com.ly.imart.R;
import com.ly.imart.bean.Fourth.MyshowFragmentBean;
import com.ly.imart.bean.Second.SecondFragmentBean;
import com.ly.imart.onerecycler.OnCreateVHListener;
import com.ly.imart.onerecycler.OneLoadingLayout;
import com.ly.imart.onerecycler.OneRecyclerView;
import com.ly.imart.onerecycler.OneVH;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecondFragmentViewpage4 extends Fragment {
    private View view;
    private OneRecyclerView mOneRecyclerView;

//    @BindView(R.id.second_fragment_viewPager4_list_imageview)
    ImageView imageView;

    @BindView(R.id.second_fragment_viewPager4_list_name)
    TextView textView_name;

    @BindView(R.id.second_fragment_viewPager4_list_price)
    TextView textView_price;

    @BindView(R.id.second_fragment_viewPager4_list_fire)
    TextView textView_fire;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.second_fragment_viewpage4, container, false);

        mOneRecyclerView = (OneRecyclerView) view.findViewById(R.id.second_fragment_viewPager4_list);

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
            super(parent, R.layout.second_fragment_viewpage4_list);
        }

        @Override
        public void bindView(int position, final SecondFragmentBean secondFragmentBean) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(view.getContext(),""+secondFragmentBean.getName(),Toast.LENGTH_SHORT).show();
                }
            });

            imageView = (ImageView) itemView.findViewById(R.id.second_fragment_viewPager4_list_imageview);
            textView_name = (TextView) itemView.findViewById(R.id.second_fragment_viewPager4_list_name);
            textView_price = (TextView) itemView.findViewById(R.id.second_fragment_viewPager4_list_price);
            textView_fire = (TextView) itemView.findViewById(R.id.second_fragment_viewPager4_list_fire);


            textView_name.setText(secondFragmentBean.getName());
            textView_price.setText(secondFragmentBean.getPrice());
            textView_fire.setText(secondFragmentBean.getFire());
        }
    }
    private void requestData(final boolean append){
        mOneRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<SecondFragmentBean> listBeans = fetchData();
                if (append){
                    mOneRecyclerView.addData(listBeans);//下拉时增加的数据
                }
                else {
                    mOneRecyclerView.setData(listBeans);//刷新
                }
            }
        },1000);
    }

    private List<SecondFragmentBean> fetchData(){
        List<SecondFragmentBean> listBeans = new ArrayList<>();
        for(int i=0;i<26;i++){
            SecondFragmentBean secondFragmentBean = new SecondFragmentBean();
            secondFragmentBean.setName("name"+i);
            secondFragmentBean.setPrice("¥"+(i+100));
            secondFragmentBean.setFire("热度"+(1050+i));
            listBeans.add(secondFragmentBean);
        }
        return listBeans;
    }
}
