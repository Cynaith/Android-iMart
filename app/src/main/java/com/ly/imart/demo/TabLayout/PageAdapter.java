package com.ly.imart.demo.TabLayout;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.HashMap;

public class PageAdapter extends FragmentPagerAdapter {

    private int num;
    private HashMap<Integer, Fragment> mFragmentHashMap = new HashMap<>();

    public PageAdapter(FragmentManager fm, int num) {
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
                    fragment = new TablayoutFragment1();
                    Log.i("fragment", "fragment1");
                    break;
                case 1:
                    fragment = new TablayoutFragment2();
                    Log.i("fragment", "fragment2");
                    break;
                case 2:
                    fragment = new TablayoutFragment3();
                    Log.i("fragment", "fragment3");
                    break;
                case 3:
                    fragment = new TablayoutFragment4();
                    Log.i("fragment", "fragment4");
                    break;
            }
            mFragmentHashMap.put(pos, fragment);
        }
        return fragment;
    }
}