package com.ly.imart.view.First;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.ly.imart.adapter.NewsAdapter;
import com.ly.imart.R;
import com.ly.imart.gson.News;
import com.ly.imart.gson.NewsData;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Banner图 https://www.zcool.com.cn/search/content?&word=banner
 */
public class FirstFragment extends Fragment implements OnBannerListener, AdapterView.OnItemClickListener {
    private Banner mBanner;
    private MyImageLoader mMyImageLoader;
    private ArrayList<Integer> imagePath;
    private ArrayList<String> imageTitle;
    private NewsAdapter adapter;
    private ListView lvNews;
    private List<NewsData> dataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_fragment, container, false);
//        if (view != null) {
////            ViewGroup parent = (ViewGroup) view.getParent();
////            if (parent != null) {
////                parent.removeView(view);
////            }
////            return view;
////        }
////        View rootView = null;
////        rootView = inflater.inflate(R.layout.activity_fragment1, container, false);
////        view = rootView;
////        return rootView;
        //判断该存不存在
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                //存在就将其删除
                parent.removeView(view);
            }
        }

        view = View.inflate(getActivity(), R.layout.first_fragment, null);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //超过每日可允许请求次数,注释掉下面
//        sendRequestWithOKHttp();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        QMUITopBar topBar = (QMUITopBar) this.getActivity().findViewById(R.id.TopBar1);
        topBar.setTitle("新闻资讯");
        topBar.setBackgroundColor(Color.parseColor("#ffffff"));
        initData();
        initView();
        lvNews = (ListView) this.getActivity().findViewById(R.id.lvNews);
        dataList = new ArrayList<NewsData>();
        adapter = new NewsAdapter(this.getContext(), dataList);
        lvNews.setAdapter(adapter);
        lvNews.setOnItemClickListener(this);
        //超过每日可允许请求次数,注释掉下面
//        sendRequestWithOKHttp();
    }

    private void initData() {
        imagePath = new ArrayList<>();
        imageTitle = new ArrayList<>();
        imagePath.add(R.drawable.banner6);
        imagePath.add(R.drawable.banner1);
        imagePath.add(R.drawable.banner2);
        imagePath.add(R.drawable.banner3);
        imagePath.add(R.drawable.banner4);
        imagePath.add(R.drawable.banner5);


        imageTitle.add("武汉加油");
        imageTitle.add("iMart宣传图");
        imageTitle.add("iMart宣传图");
        imageTitle.add("iMart宣传图");
        imageTitle.add("iMart宣传图");
        imageTitle.add("iMart宣传图");


    }

    private void initView() {
        mMyImageLoader = new MyImageLoader();
        mBanner = (Banner) this.getActivity().findViewById(com.ly.imart.R.id.banner);
        //设置样式，里面有很多种样式可以自己都看看效果
        mBanner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
        //设置图片加载器
        mBanner.setImageLoader(mMyImageLoader);
        //设置轮播的动画效果,里面有很多种特效,可以都看看效果。
        mBanner.setBannerAnimation(Transformer.ZoomOutSlide);
        //轮播图片的文字
        mBanner.setBannerTitles(imageTitle);
        //设置轮播间隔时间
        mBanner.setDelayTime(3000);
        //设置是否为自动轮播，默认是true
        mBanner.isAutoPlay(true);
        //设置指示器的位置，小点点，居中显示
        mBanner.setIndicatorGravity(BannerConfig.CENTER);
        //设置图片加载地址
        mBanner.setImages(imagePath)
                //轮播图的监听
                .setOnBannerListener(this)
                //开始调用的方法，启动轮播图。
                .start();
        //超过每日可允许请求次数,注释掉下面
//        sendRequestWithOKHttp();

    }

    /**
     * 轮播图的监听
     *
     * @param position
     */
    @Override
    public void OnBannerClick(int position) {

    }

    /**
     * 图片加载类
     */
    private class MyImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Glide.with(context.getApplicationContext())
                    .load(path)
                    .into(imageView);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        NewsData newsData = dataList.get(position);
        Intent intent = new Intent(this.getActivity(), BrowseNewsActivity.class);
        intent.putExtra("content_url", newsData.getUrl());
        startActivity(intent);
    }

    private void sendRequestWithOKHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url("http://v.juhe.cn/toutiao/index?type=jiaoyu&key=5942035e21ecaf996e8c0d0ac11ee05d")
                            .build();
                    Response response = null;
                    response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    Log.d("测试：", responseData);
                    parseJsonWithGson(responseData);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void parseJsonWithGson(String jsonData) {
        Gson gson = new Gson();
        News news = gson.fromJson(jsonData, News.class);
        List<NewsData> list = news.getResult().getData();
        for (int i = 0; i < list.size(); i++) {
            String uniquekey = list.get(i).getUniqueKey();
            String title = list.get(i).getTitle();
            String date = list.get(i).getDate();
            String category = list.get(i).getCategory();
            String author_name = list.get(i).getAuthorName();
            String content_url = list.get(i).getUrl();
            String pic_url = list.get(i).getThumbnail_pic_s();
//            System.out.println("标题："+title);
//            System.out.println("日期："+date);
//            System.out.println("作者："+author_name);
//            System.out.println("网址："+content_url);
//            System.out.println("图片："+pic_url);
            dataList.add(new NewsData(uniquekey, title, date, category, author_name, content_url, pic_url));
        }
        //更新Adapter(务必在主线程中更新UI!!!)
        this.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}
