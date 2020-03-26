package com.ly.imart.view.Fourth;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.services.core.LatLonPoint;
import com.bumptech.glide.Glide;
import com.ly.imart.R;
import com.ly.imart.bean.Fourth.DiyListBean;
import com.ly.imart.model.Fourth.DiyListModel;
import com.ly.imart.onerecycler.OnCreateVHListener;
import com.ly.imart.onerecycler.OneLoadingLayout;
import com.ly.imart.onerecycler.OneRecyclerView;
import com.ly.imart.onerecycler.OneVH;
import com.ly.imart.util.MyImageView;
import com.ly.imart.view.Fourth.GaodeMap.InitWay;
import com.ly.imart.view.Fourth.GaodeMap.util.Utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class DiyListActivity extends AppCompatActivity implements View.OnClickListener, AMapLocationListener {


    private OneRecyclerView mOneRecyclerView;
    private DiyListModel diyListModel;
    TextView textView;
    //声明mlocationClient对象
    public AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    String region = null;
    volatile boolean isRetuen = false;
    //用户所在地理位置
    double weidu;
    double jingdu;
    private ProgressDialog progDialog = null;// 搜索时进度条
    List<DiyListBean> listBeans = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diylist);
        diyListModel = new DiyListModel();
        mOneRecyclerView = (OneRecyclerView) findViewById(R.id.diylist);

        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(2000);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        showProgressDialog();
        mlocationClient.startLocation();
        mOneRecyclerView.init(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        requestData(false);
                    }
                },
                new OneLoadingLayout.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        requestData(true);
                    }
                },
                new OnCreateVHListener() {
                    @Override
                    public OneVH onCreateHolder(ViewGroup parent) {
                        return new DiyListBeanVH(parent);
                    }

                    @Override
                    public boolean isCreate(int position, Object t) {
                        return t instanceof DiyListBean;
                    }
                }
        );

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.diy_title:
//                initLocation();
                break;
        }
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //定位成功回调信息，设置相关消息
                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                weidu = aMapLocation.getLatitude();//获取纬度
                jingdu = aMapLocation.getLongitude();//获取经度
                aMapLocation.getAccuracy();//获取精度信息
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date(aMapLocation.getTime());
                df.format(date);//定位时间
                /**
                 * 存在多次获取的Bug
                 * isReturn判断定位是否获取
                 */
                if (!isRetuen) {
                    listBeans = fetchData();
                    mOneRecyclerView.setData(listBeans);
                    requestData(false);//刷新数据
                    isRetuen = !isRetuen;
                }
                Log.d("本次定位", "" + weidu + jingdu);
                if (progDialog != null) {
                    progDialog.dismiss();
                }
                mlocationClient.onDestroy();
            } else {
                Toast.makeText(this, aMapLocation.getErrorInfo(), Toast.LENGTH_SHORT).show();
                //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:"
                        + aMapLocation.getErrorCode() + ", errInfo:"
                        + aMapLocation.getErrorInfo());
            }
        }
    }


    class DiyListBeanVH extends OneVH<DiyListBean> {
        public DiyListBeanVH(ViewGroup parent) {
            super(parent, R.layout.activity_diylist_list);
        }

        @Override
        public void bindView(int position, final DiyListBean diyListBean) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LatLonPoint mStart = new LatLonPoint(weidu, jingdu);
                    String endLocation = diyListBean.getLocation();
                    String[] location = endLocation.split(",");
                    LatLonPoint mEnd = new LatLonPoint(Double.parseDouble(location[1]), Double.parseDouble(location[0]));
                    InitWay.openInitWay(getApplicationContext(),mStart,mEnd);
                }
            });
            MyImageView myImageView = itemView.findViewById(R.id.diylist_photo);
            TextView diyname = itemView.findViewById(R.id.diylist_name);
            TextView diytel = itemView.findViewById(R.id.diylist_tel);
            diytel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showDialog(diyListBean.getTel());
                }
            });
            TextView diydistance = itemView.findViewById(R.id.diylist_distance);
            TextView diyAddress = itemView.findViewById(R.id.diylist_address);
            Glide.with(itemView.getContext()).load(diyListBean.getPhoto())
                    .asBitmap()
                    .error(R.drawable.loadingimg)
                    .centerCrop()
                    .into(myImageView);
            diyname.setText(diyListBean.getName());
            diyAddress.setText(diyListBean.getAddress());
            diytel.setText(diyListBean.getTel());
        }

    }

    private void requestData(final boolean append) {
        mOneRecyclerView.postDelayed(new Runnable() {
            @Override
            public void run() {

                List<DiyListBean> listBeans = fetchData();
                if (append) {
//                    mOneRecyclerView.setData(fetchData());//下拉时增加的数据
                } else {
                    mOneRecyclerView.setData(listBeans);//刷新
                }
            }
        }, 1000);
    }

    private List<DiyListBean> fetchData() {

        if (!isRetuen) {
            listBeans = null;
        } else {
            try {
                listBeans = diyListModel.getInfo(weidu, jingdu);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return listBeans;
    }


    /**
     * 显示Dialog
     */
    private void showDialog(String mPhoneNumber) {
        //创建Dialog
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        //设置Dialong的内容
        dialog.setMessage("您确定要拨打:" + mPhoneNumber + "吗?");
        //设置确定拨打电话的时间
        dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //创建一个意图
                Intent intent = new Intent();
                //设置动作
                intent.setAction("android.intent.action.CALL");
                //携带数据，也就是携带电话号码
                intent.setData(Uri.parse("tel://" + mPhoneNumber));
                //跳转到意图
                startActivity(intent);
            }
        });
        //设置取消按钮
        dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //关闭Dialog
                dialog.dismiss();
            }
        });
        //显示Dialog
        dialog.show();
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
        progDialog.setMessage("正在搜索定位...");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
            mlocationClient = null;
        }

    }
}
