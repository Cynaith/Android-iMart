package com.ly.imart.view.Fourth.GaodeMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.ly.imart.R;
import com.ly.imart.view.Fourth.GaodeMap.util.ToastUtil;

import androidx.annotation.Nullable;

public class InitWay extends Activity implements View.OnClickListener, RouteSearch.OnRouteSearchListener {
    private Context mContext;
    private static LatLonPoint mStartPoint;
    private static LatLonPoint mEndPoint;

//    测试数据
//    private LatLonPoint mStartPoint = new LatLonPoint(40.132350, 116.325891);//起点，116.335891,39.942295
//    private LatLonPoint mEndPoint = new LatLonPoint(39.995576, 116.481288);//终点，116.481288,39.995576
//    private LatLonPoint mStartPoint_bus = new LatLonPoint(40.818311, 111.670801);//起点，111.670801,40.818311
//    private LatLonPoint mEndPoint_bus = new LatLonPoint(44.433942, 125.184449);//终点，
    private TextView button_walk,button_bus,button_car;
    private RouteSearch mRouteSearch;
    private ProgressDialog progDialog = null;// 搜索时进度条

    public static void openInitWay(Context context,LatLonPoint mStartPoint1,LatLonPoint mEndPoint1){
        mStartPoint = mStartPoint1;
        mEndPoint = mEndPoint1;
        Intent intent = new Intent(context, InitWay.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable @android.support.annotation.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selectway);
        initView();
        mRouteSearch = new RouteSearch(this);
        mRouteSearch.setRouteSearchListener(this);

    }

    void initView(){
        button_walk = findViewById(R.id.init_walking_way);
        button_bus = findViewById(R.id.init_bus_way);
        button_car = findViewById(R.id.init_car_way);
        button_walk.setOnClickListener(this);
        button_bus.setOnClickListener(this);
        button_car.setOnClickListener(this);
        mContext = this.getApplicationContext();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.init_bus_way:
                searchBusRouteResult();
                break;
            case R.id.init_walking_way:
                WalkWay.openWalkWay(InitWay.this,mStartPoint,mEndPoint);
                break;
            case R.id.init_car_way:
                CarWay.openCarWay(InitWay.this,mStartPoint,mEndPoint);
                break;
        }

    }

    public void searchBusRouteResult(){
        showProgressDialog();
        final RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                mStartPoint, mEndPoint);
        RouteSearch.BusRouteQuery query = new RouteSearch.BusRouteQuery(fromAndTo, RouteSearch.BusDefault,
                "上海", 1);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
        mRouteSearch.calculateBusRouteAsyn(query);
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
        dissmissProgressDialog();
        if (i == 1000) {
            if (busRouteResult != null && busRouteResult.getPaths() != null) {
                if (busRouteResult.getPaths().size() > 0) {
                    Intent intent = new Intent(mContext.getApplicationContext(),
                            BusWay.class);
                    intent.putExtra("bus_path", busRouteResult.getPaths().get(0));
                    intent.putExtra("bus_result", busRouteResult);
                    System.out.println(busRouteResult);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);

                } else if (busRouteResult != null && busRouteResult.getPaths() == null) {
                    ToastUtil.show(mContext, "对不起没有搜索到相关数据");
                }
            } else {
                ToastUtil.show(mContext, "对不起没有搜索到相关数据");
            }
        } else {
            ToastUtil.showerror(this.getApplicationContext(), i);
        }
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {

    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int errorCode) {
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {

    }
    /**
     * 显示进度框
     */
    private void showProgressDialog() {
        if (progDialog == null)
            progDialog = new ProgressDialog(this);
        progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDialog.setIndeterminate(false);
        progDialog.setCancelable(true);
        progDialog.setMessage("正在搜索...");
        progDialog.show();
    }

    /**
     * 隐藏进度框
     */
    private void dissmissProgressDialog() {
        if (progDialog != null) {
            progDialog.dismiss();
        }
    }
}
