package com.ly.imart.presenter.Fourth;

import com.ly.imart.bean.Fourth.FriendListBean;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.model.Fourth.FollowListModelImpl;
import com.ly.imart.view.Fourth.IFollowListView;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public List<FriendListBean> getFriendList() throws ExecutionException, InterruptedException {
      return  followListModel.getFollowList(SharePreferenceUtils.getInstance().getUserName());

    }

    public List<FriendListBean> getFriendedList() throws ExecutionException, InterruptedException {
        return followListModel.getFollowedList(SharePreferenceUtils.getInstance().getUserName());

    }

}
