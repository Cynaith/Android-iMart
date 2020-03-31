package com.ly.imart.view.Third;


import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.ly.imart.R;
import com.ly.imart.demo.Douyin.OnViewPagerListener;
import com.ly.imart.demo.Douyin.ViewPagerLayoutManager;

public class ThirdFragment extends Fragment {

    private static final String TAG = "ViewPagerActivity";
    private RecyclerView mRecyclerView;
    private MyAdapter mAdapter;
    private ViewPagerLayoutManager mLayoutManager;
    private View view;
    private boolean isStart = false;//是否第一次进入此fragment
    private boolean isCreate = false; //view是否加载
    private VideoView videoView;
    private View itemView;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_view_pager_layout_manager, container, false);
        initView();
        initListener();

        isCreate = true;
        return view;
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isCreate){
            if (getUserVisibleHint()) {
                //可见第一次
                if (!isStart){
                    isStart = !isStart;
                    videoView.start();

                }
                //可见第n次
                else {
                    //重复进入时
                    videoView.start();
                }

            } else {
                //第一次不可见执行
                try{
                    videoView.pause();
                }catch (NullPointerException e){

                }
            }
        }

    }


    private void initView() {
        mRecyclerView = view.findViewById(R.id.recycler);
        mLayoutManager = new ViewPagerLayoutManager(this.getActivity(), OrientationHelper.VERTICAL);
        mAdapter = new MyAdapter();
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void initListener() {
        mLayoutManager.setOnViewPagerListener(new OnViewPagerListener() {
            @Override
            public void onPageRelease(boolean isNext, int position) {
                Log.e(TAG, "释放位置:" + position + " 下一页:" + isNext);
                int index = 0;
                if (isNext) {
                    index = 0;
                } else {
                    index = 1;
                }
                releaseVideo(index);
            }

            @Override
            public void onPageSelected(int position, boolean isBottom) {
                Log.e(TAG, "选中位置:" + position + "  是否是滑动到底部:" + isBottom);
                playVideo(0,true);
            }

            @Override
            public void onLayoutComplete() {
                playVideo(0,false);
            }

        });
    }

    private void playVideo(int position,boolean start) {
        itemView = mRecyclerView.getChildAt(0);
        videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final RelativeLayout rootView = itemView.findViewById(R.id.root_view);
        final MediaPlayer[] mediaPlayer = new MediaPlayer[1];

       if (start) videoView.start();
        videoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                mediaPlayer[0] = mp;
                Log.e(TAG, "onInfo");
                mp.setLooping(true);
                imgThumb.animate().alpha(0).setDuration(200).start();
                return false;
            }
        });
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.e(TAG, "onPrepared");

            }
        });


        imgPlay.setOnClickListener(new View.OnClickListener() {
            boolean isPlaying = true;

            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    Log.e(TAG, "isPlaying:" + videoView.isPlaying());
                    imgPlay.animate().alpha(1f).start();
                    videoView.pause();
                    isPlaying = false;
                } else {
                    Log.e(TAG, "isPlaying:" + videoView.isPlaying());
                    imgPlay.animate().alpha(0f).start();
                    videoView.start();
                    isPlaying = true;
                }
            }
        });
    }

    private void releaseVideo(int index) {
        View itemView = mRecyclerView.getChildAt(index);
        final VideoView videoView = itemView.findViewById(R.id.video_view);
        final ImageView imgThumb = itemView.findViewById(R.id.img_thumb);
        final ImageView imgPlay = itemView.findViewById(R.id.img_play);
        videoView.stopPlayback();
        imgThumb.animate().alpha(1).start();
        imgPlay.animate().alpha(0f).start();
    }


    class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        //private int[] imgs = {R.mipmap.img_video_1,R.mipmap.img_video_2};
//        private int[] videos = {R.raw.douyin_1, R.raw.douyin_2, R.raw.douyin_3, R.raw.douyin_4};

        public MyAdapter() {
        }


        @Override
        public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_pager, parent, false);
            return new MyAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
            //holder.img_thumb.setImageResource(imgs[position%2]);
           //holder.videoView.setVideoURI(Uri.parse("android.resource://" + getContext().getPackageName() + "/" + videos[position % 4]));
            holder.videoView.setVideoURI(Uri.parse("http://47.101.171.252:81/static/7ce4d0c3-b449-47db-bed6-b1ee392f4a5f.mp4"));
            holder.textView_username.setText("@"+"设置作者");
            holder.textView_title.setText("设置视频标题");
        }

        @Override
        public int getItemCount() {
            return 20;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_thumb;
            VideoView videoView;
            ImageView img_play;
            RelativeLayout rootView;
            TextView textView_username;
            TextView textView_title;
            public ViewHolder(View itemView) {
                super(itemView);
                img_thumb = itemView.findViewById(R.id.img_thumb);
                videoView = itemView.findViewById(R.id.video_view);
                img_play = itemView.findViewById(R.id.img_play);
                rootView = itemView.findViewById(R.id.root_view);
                textView_username = itemView.findViewById(R.id.video_username);
                textView_title = itemView.findViewById(R.id.video_title);
            }
        }
    }
}
