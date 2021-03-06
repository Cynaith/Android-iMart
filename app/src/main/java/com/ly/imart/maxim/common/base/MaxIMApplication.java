
package com.ly.imart.maxim.common.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

import com.ly.imart.maxim.bmxmanager.BaseManager;
import com.ly.imart.maxim.common.utils.AppContextUtils;
import com.ly.imart.maxim.common.utils.BuglyTask;
import com.ly.imart.maxim.common.utils.FileConfig;
import com.ly.imart.maxim.common.utils.FileUtils;
import com.ly.imart.maxim.common.utils.SharePreferenceUtils;
import com.ly.imart.maxim.push.PushClientMgr;
import com.ly.imart.maxim.push.PushUtils;

/**
 * Description : application Created by Mango on 2018/11/5.
 */
public class MaxIMApplication extends Application {

    private static final int THREAD_POOL_MAX = 5;

    private static final int MAX_IMG_W_FOR_MEMORY_CACHE = 240;

    private static final int MAX_IMG_H_FOR_MEMORY_CACHE = 240;

    private static final int MEMORY_CACHE_SIZE_MAX = 10 * 1024 * 1024;
    
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        SDKInitializer.setCoordType(CoordType.BD09LL);
        initUtils();
        initBMXSDK();
    }

    /**
     * 初始化工具类
     */
    private void initUtils() {
        AppContextUtils.initApp(this);
        initImageLoader();
        // push
//        PushClientMgr.initManager(this);
//        PushUtils.getInstance().registerActivityListener(this);
//        BuglyTask.get().init(this);
    }

    /**
     * 初始化图片查看
     */
    private void initImageLoader() {
        // 设置图片缓存目录
        File cacheDir = FileUtils.getFileByPath(FileConfig.DIR_APP_CACHE);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .threadPoolSize(THREAD_POOL_MAX).threadPriority(Thread.MIN_PRIORITY)
                .memoryCache(new UsingFreqLimitedMemoryCache(MEMORY_CACHE_SIZE_MAX))
                .memoryCacheExtraOptions(MAX_IMG_W_FOR_MEMORY_CACHE, MAX_IMG_H_FOR_MEMORY_CACHE)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .memoryCacheSize(2 * 1024 * 1024)
                .diskCacheSize(50 * 1024 * 1024)
                .diskCache(new UnlimitedDiskCache(cacheDir)).build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 初始化sdk
     */
    private void initBMXSDK() {
        int custom = SharePreferenceUtils.getInstance().getCustomDns();
        if (custom < 0 || custom > 4) {
            custom = 0;
        }
        BaseManager.initTestBMXSDK(custom);
    }
}
