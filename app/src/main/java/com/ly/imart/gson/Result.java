package com.ly.imart.gson;

import com.ly.imart.gson.NewsData;

import java.util.List;


public class Result {

    private String stat;

    List<NewsData> data;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
    }

    public List<NewsData> getData() {
        return data;
    }

    public void setData(List<NewsData> data) {
        this.data = data;
    }
}
