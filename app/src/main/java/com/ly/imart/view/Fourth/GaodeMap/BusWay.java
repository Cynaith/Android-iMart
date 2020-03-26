package com.ly.imart.view.Fourth.GaodeMap;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.ly.imart.R;
import com.ly.imart.view.Fourth.GaodeMap.util.AMapUtil;

public class BusWay  extends Activity implements AMap.OnMapLoadedListener,
        AMap.OnMapClickListener, AMap.InfoWindowAdapter, AMap.OnInfoWindowClickListener, AMap.OnMarkerClickListener {

    private AMap aMap;
    private MapView mapView;
    private BusPath mBuspath;
    private BusRouteResult mBusRouteResult;
    private BusRouteOverlay mBusrouteOverlay;
    private TextView bustitle;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busway);
        mapView = (MapView) findViewById(R.id.busroute_map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写

        getIntentData();
        init();
        onMapClick();
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            mBuspath = intent.getParcelableExtra("bus_path");
            mBusRouteResult = intent.getParcelableExtra("bus_result");
            bustitle = findViewById(R.id.busmap_title);
            bustitle.setText(AMapUtil.getBusPathTitle(mBuspath));//获取公交名
        }
    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        registerListener();
    }

    public void onMapClick() {

        mapView.setVisibility(View.VISIBLE);
        aMap.clear();// 清理地图上的所有覆盖物
        mBusrouteOverlay = new BusRouteOverlay(this, aMap,
                mBuspath, mBusRouteResult.getStartPos(),
                mBusRouteResult.getTargetPos());
        mBusrouteOverlay.removeFromMap();

    }
    private void registerListener() {
        aMap.setOnMapLoadedListener(this);
        aMap.setOnMapClickListener(this);
        aMap.setOnMarkerClickListener(this);
        aMap.setOnInfoWindowClickListener(this);
        aMap.setInfoWindowAdapter(this);
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLoaded() {
        if (mBusrouteOverlay != null) {
            mBusrouteOverlay.addToMap();
            mBusrouteOverlay.zoomToSpan();
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
