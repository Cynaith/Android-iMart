package com.ly.imart.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.ly.imart.demo.TabLayout.TablayoutFragment1;
import com.ly.imart.demo.TabLayout.TablayoutFragment2;
import com.ly.imart.demo.TabLayout.TablayoutFragment3;
import com.ly.imart.demo.TabLayout.TablayoutFragment4;
import com.ly.imart.view.Fourth.FourthMyshowFragment1;
import com.ly.imart.view.Fourth.FourthMyshowFragment2;
import com.ly.imart.view.Fourth.FourthMyshowFragment3;

import java.util.HashMap;

public class MyshowPageAdapter extends FragmentPagerAdapter {

    private int num;
    private HashMap<Integer, Fragment> mFragmentHashMap = new HashMap<>();

    public MyshowPageAdapter(FragmentManager fm, int num) {
        super(fm);
        this.num = num;
    }

    @Override
    public Fragment getItem(int position) {

        return createFragment(position);
    }

    @Override
    public int getCount() {
        return num;
    }

    private Fragment createFragment(int pos) {
        Fragment fragment = mFragmentHashMap.get(pos);

        if (fragment == null) {
            switch (pos) {
                case 0:
                    fragment = new FourthMyshowFragment1();
                    Log.i("fragment", "fragment1");
                    break;
                case 1:
                    fragment = new FourthMyshowFragment2();
                    Log.i("fragment", "fragment2");
                    break;
                case 2:
                    fragment = new FourthMyshowFragment3();
                    Log.i("fragment", "fragment3");
                    break;
            }
            mFragmentHashMap.put(pos, fragment);
        }
        return fragment;
    }
}