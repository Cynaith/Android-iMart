package com.ly.imart.view.Fourth;

import android.text.TextUtils;

import com.ly.imart.maxim.bmxmanager.BaseManager;
import com.ly.imart.maxim.bmxmanager.RosterManager;
import com.ly.imart.maxim.common.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import im.floo.floolib.BMXErrorCode;
import im.floo.floolib.BMXRosterItem;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class IMUtils {

    // 注释 search里输入username 即可自动加好友
    public static void  searchRoster(String search) {
        search = search.toLowerCase();
        List<BMXRosterItem> mSearchs = new ArrayList<>();
        final BMXRosterItem item = new BMXRosterItem();

        RosterManager.getInstance().search(search, true, item);

//        Observable.just(search).map(new Func1<String, BMXErrorCode>() {
//            @Override
//            public BMXErrorCode call(String s) {
//                BMXErrorCode bmxErrorCode =
//                System.out.println(bmxErrorCode.toString());
//                return bmxErrorCode;
//            }
//        }).flatMap(new Func1<BMXErrorCode, Observable<BMXErrorCode>>() {
//            @Override
//            public Observable<BMXErrorCode> call(BMXErrorCode errorCode) {
//                return BaseManager.bmxFinish(errorCode, errorCode);
//            }
//        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<BMXErrorCode>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//
//                    }
//
//                    @Override
//                    public void onNext(BMXErrorCode errorCode) {
//                        mSearchs.add(item);
//                    }
//                });
        mSearchs.add(item);
//        long rosterId = item.rosterId();
        while (mSearchs.size()==0){
            mSearchs.add(item);
        }
        long rosterId = mSearchs.get(0).rosterId();
        RosterManager.getInstance().apply(rosterId, " ");
//        Observable.just(rosterId).map(new Func1<Long, BMXErrorCode>() {
//            @Override
//            public BMXErrorCode call(Long s) {
//                return RosterManager.getInstance().apply(s, " ");
//            }
//        }).flatMap(new Func1<BMXErrorCode, Observable<BMXErrorCode>>() {
//            @Override
//            public Observable<BMXErrorCode> call(BMXErrorCode bmxErrorCode) {
//                return BaseManager.bmxFinish(bmxErrorCode, bmxErrorCode);
//            }
//        }).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<BMXErrorCode>() {
//                    @Override
//                    public void onCompleted() {
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                    }
//
//                    @Override
//                    public void onNext(BMXErrorCode errorCode) {
//                    }
//                });



    }
}
