package com.ly.imart.view.Second;


import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ly.imart.R;
import com.ly.imart.adapter.MyshowPageAdapter;
import com.ly.imart.adapter.SecondPageAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SecondFragment extends Fragment implements ISecondView{

    private View view;

//    @BindView(R.id.second_fragment_tabLayout)
    TabLayout tabLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.second_fragment,container,false);
//        ButterKnife.bind(this.getActivity());

        tabLayout = (TabLayout) view.findViewById(R.id.second_fragment_tabLayout);
        setTablayout();
        return view;
    }

    @Override
    public void setTablayout() {

            tabLayout.addTab(tabLayout.newTab().setText("创意分类"));
            tabLayout.addTab(tabLayout.newTab().setText("创意分类"));
            tabLayout.addTab(tabLayout.newTab().setText("创意分类"));
            tabLayout.addTab(tabLayout.newTab().setText("创意分类"));
            final ViewPager viewPager = (ViewPager) view.findViewById(R.id.second_fragment_viewPager);
            viewPager.setAdapter(new SecondPageAdapter(this.getActivity().getSupportFragmentManager(), tabLayout.getTabCount()));
            viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            //绑定tab点击事件
            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

    }
}
