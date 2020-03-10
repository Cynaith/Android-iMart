package com.ly.imart.presenter.Fourth;

import com.ly.imart.bean.Fourth.FourthBean;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.model.Fourth.FourthModelImpl;
import com.ly.imart.model.Fourth.IFourthModel;
import com.ly.imart.view.Fourth.IFourthView;

import java.util.concurrent.ExecutionException;

public class FourthPresenter {

    private IFourthModel iFourthModel;
    private IFourthView iFourthView;

    public FourthPresenter(IFourthView iFourthView) {
        this.iFourthView = iFourthView;
        iFourthModel = new FourthModelImpl();
    }


    public void gotoFollowPage() {
        iFourthView.gotoFollowPage();
    }

    public void gotoFollowedPage() {
        iFourthView.gotoFollowedPage();
    }

    public void gotoFriendPage() {
        iFourthView.gotoFriendPage();
    }

    public void gotoArticlePage() {
        iFourthView.gotoArticlePage();
    }

    public void gotoMyshowPage(){
        iFourthView.gotoMyshowPage(SharePreferenceUtils.getInstance().getUserName());
    }

    public FourthBean getInfo() throws ExecutionException, InterruptedException {
        return iFourthModel.getInfo(SharePreferenceUtils.getInstance().getUserName());
    }

}
