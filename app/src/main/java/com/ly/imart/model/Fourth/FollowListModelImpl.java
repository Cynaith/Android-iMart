package com.ly.imart.model.Fourth;

import com.ly.imart.bean.Fourth.UserPageBean;

public class FollowListModelImpl implements IFollowListModel {

    private UserPageBean userPageBean;

    public FollowListModelImpl() {
        userPageBean = new UserPageBean();
    }

    @Override
    public UserPageBean getUserInfoByUsername(String username) {
        return userPageBean;
    }




}
