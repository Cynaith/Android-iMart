package com.ly.imart.model.Fourth;

import com.ly.imart.bean.Fourth.FourthBean;

import java.util.concurrent.ExecutionException;

public interface IFourthModel {

    FourthBean getInfo(String userName) throws ExecutionException, InterruptedException;
}
