package com.ly.imart.view.Others;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ly.imart.R;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AddVideo extends FragmentActivity implements View.OnClickListener {

    private final int REQUEST_CODE_SELECT_VIDEO = 112;
    private final int REQUEST_CODE_VIDEO_MAKE = 113;
    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    TextView photoByCamera;
    TextView photoByAblum;
    TextView next;

    private Uri uri = null;//视频地址

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_video_select);

        initView();
    }

    private void initView() {
        photoByCamera = findViewById(R.id.add_video1);
        photoByAblum = findViewById(R.id.add_video2);

        next = findViewById(R.id.add_video_next);
        photoByCamera.setOnClickListener(this);
        photoByAblum.setOnClickListener(this);
        next.setOnClickListener(this);
    }

    private void chooseVideo() {
        Intent intent = new Intent();
        /* 开启Pictures画面Type设定为image */
        //intent.setType("image/*");
        // intent.setType("audio/*"); //选择音频
        intent.setType("video/*"); //选择视频 （mp4 3gp 是android支持的视频格式）

        // intent.setType("video/*;image/*");//同时选择视频和图片

        /* 使用Intent.ACTION_GET_CONTENT这个Action */
        intent.setAction(Intent.ACTION_GET_CONTENT);
        /* 取得相片后返回本画面 */
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        System.out.println(requestCode);
        if (resultCode != RESULT_OK) {
            return;
        }
        // 选取图片的返回值
        if (requestCode == REQUEST_CODE_SELECT_VIDEO) {
            Uri uri = data.getData();
            Cursor cursor = getContentResolver().query(uri, null, null,
                    null, null);
            cursor.moveToFirst();
            // String imgNo = cursor.getString(0); // 图片编号
            String v_path = cursor.getString(1); // 图片文件路径
            String v_size = cursor.getString(2); // 图片大小
            String v_name = cursor.getString(3); // 图片文件名
            System.out.println(v_path + v_name + v_name);

        } else if (requestCode == REQUEST_CODE_VIDEO_MAKE) {
            Toast.makeText(this, "Video saved to:\n" +
                    data.getData(), Toast.LENGTH_LONG).show();
            System.out.println(uri);
//            vv_play.setVideoURI(fileUri);
//            vv_play.requestFocus();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.add_video2) {
            chooseVideo();
        } else if (view.getId() == R.id.add_video1) {
            Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File("/sdcard/test.mp4")));
            startActivityForResult(intent, REQUEST_CODE_VIDEO_MAKE);

        }
    }

    private File createMediaFile() throws IOException {

        if ((Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))) {
            // 选择自己的文件夹
            String path = Environment.getExternalStorageDirectory().getPath() + "/myvideo/";
            // Constants.video_url 是一个常量，代表存放视频的文件夹
            File mediaStorageDir = new File(path);
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.e("TAG", "文件夹创建失败");
                    return null;
                }
            }

            // 文件根据当前的毫秒数给自己命名
            String timeStamp = String.valueOf(System.currentTimeMillis());
            timeStamp = timeStamp.substring(7);
            String imageFileName = "V" + timeStamp;
            String suffix = ".mp4";
            File mediaFile = new File(mediaStorageDir + File.separator + imageFileName + suffix);
            return mediaFile;
        }

        return null;
    }
}

