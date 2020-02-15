package com.ly.imart.presenter.Second;

import com.ly.imart.bean.Second.ArticleMainBean;
import com.ly.imart.model.Second.ArticleMainModelImpl;
import com.ly.imart.model.Second.IArticleMainModel;
import com.ly.imart.view.Second.IArticleMainView;

public class ArticleMainPresenter {
    private IArticleMainView iArticleMainView;
    private IArticleMainModel iArticleMainModel;

    public ArticleMainPresenter(IArticleMainView iArticleMainView) {
        this.iArticleMainView = iArticleMainView;
        iArticleMainModel = new ArticleMainModelImpl();
    }


    public void getUserInfo(String userPhone){
        iArticleMainView.initView(iArticleMainModel.getInfo(userPhone));;
    }

}
