package com.ly.imart.presenter.Fourth;

import com.ly.imart.bean.Fourth.FriendListBean;
import com.ly.imart.model.Fourth.FollowListModelImpl;
import com.ly.imart.view.Fourth.IFollowListView;

public class FollowListPresenter {

    private FriendListBean friendListBean = new FriendListBean();
    private FollowListModelImpl followListModel;
    private IFollowListView iFollowListView;

    public FollowListPresenter(IFollowListView iFollowListView){
        this.iFollowListView = iFollowListView;
        followListModel = new FollowListModelImpl();
    }

    public void gotoUserPage(String username){
        followListModel.getUserInfoByUsername(username);
    }

}
