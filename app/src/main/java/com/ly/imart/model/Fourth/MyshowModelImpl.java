package com.ly.imart.model.Fourth;

import com.ly.imart.bean.Fourth.MyshowBean;

public class MyshowModelImpl implements IMyshowModel {

    MyshowBean myshowBean;

    public MyshowModelImpl() {
        myshowBean = new MyshowBean();
    }

    @Override
    public void initData(int userid) {
        //从p层传id  从mysql获取数据传入Bean
        myshowBean.setFollow(true);
    }

    @Override
    public boolean isFollow() {
        return myshowBean.isFollow();
    }

    @Override
    public void setFollow(boolean follow) {
        //修改数据库中数据，修改 关注表
        myshowBean.setFollow(follow);
    }


}
