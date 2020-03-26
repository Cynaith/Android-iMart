package com.ly.imart.view.Others;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Toast;


import com.ly.imart.R;
import com.ly.imart.model.Others.AddArticleModel;
import com.ly.imart.util.SystemInfo;
import com.ly.imart.view.MainActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.ly.imart.view.Others.AddArticleImg.saveImage;

/**
 * 主Activity入口
 *
 * @author xmuSistone
 *
 */
@SuppressLint("SimpleDateFormat")
public class AddArticle extends FragmentActivity {
    private static final int REQUEST_CODE_PICK_IMAGE = 1023;
    private static final int REQUEST_CODE_CAPTURE_CAMEIA = 1022;
    private RichTextEditor editor;
    private View btn1, btn2, btn3;
    private OnClickListener btnListener;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    //请求状态码
    private static int REQUEST_PERMISSION_CODE = 1;
    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    private File mCurrentPhotoFile;// 照相机拍照得到的图片
    AddArticleModel addArticleModel;

    //后台传输数据
    private static String articleTitle;
    private static int articleKind;
    private static String articleImg;

    public static void openAddArticle(Context context,String title,int kind,String img){
        Intent intent = new Intent(context,AddArticle.class);
        articleTitle = title;
        articleKind = kind;
        articleImg = img;
        context.startActivity(intent);
        ((Activity)context).finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.add_article);
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
//            }
//        }
        addArticleModel = new AddArticleModel();
        editor = (RichTextEditor) findViewById(R.id.richEditor);
        btnListener = new OnClickListener() {

            @Override
            public void onClick(View v) {
                editor.hideKeyBoard();
                if (v.getId() == btn1.getId()) {
                    // 打开系统相册
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");// 相片类型
                    startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
                } else if (v.getId() == btn2.getId()) {
                    // 打开相机
                    openCamera();
                } else if (v.getId() == btn3.getId()) {
                    List<RichTextEditor.EditData> editList = editor.buildEditData();
                    // 下面的代码可以上传、或者保存，请自行实现
                    dealEditData(editList);
                }
            }
        };

        btn1 = findViewById(R.id.button1);
        btn2 = findViewById(R.id.button2);
        btn3 = findViewById(R.id.button3);

        btn1.setOnClickListener(btnListener);
        btn2.setOnClickListener(btnListener);
        btn3.setOnClickListener(btnListener);
    }

    /**
     * 负责处理编辑数据提交等事宜，请自行实现
     */
    protected void dealEditData(List<RichTextEditor.EditData> editList) {
        StringBuilder stringBuilder = new StringBuilder();

        for (RichTextEditor.EditData itemData : editList) {
            if (itemData.inputStr != null) {
                StringBuilder stringBuilder1 = new StringBuilder();
                stringBuilder1.append(itemData.inputStr);
                stringBuilder1.deleteCharAt(itemData.inputStr.length()-1);
                stringBuilder1.deleteCharAt(0);
                stringBuilder.append(stringBuilder1.toString().replaceAll(",","<br/>")+"<br/>");
//                stringBuilder1.append("</h3>");
//                Log.d("RichEditor", "commit inputStr=" + stringBuilder1.toString().replaceAll(",","</h3><br/>"));
            } else if (itemData.imagePath != null) {
                String imgUrl = null;
                try {
                   imgUrl =  addArticleModel.uploadImg(itemData.imagePath);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("RichEditor", "commit imgePath=" + imgUrl);
                stringBuilder.append("<img src=\""+imgUrl+"\"/><br/>");
//                Log.d("RichEditor", "commit imgePath=" + itemData.imagePath);

            }

        }
        Log.d("RichEditor",stringBuilder.toString());
        try {
            if (addArticleModel.postArticle(articleTitle,stringBuilder.toString(),articleKind,articleImg)){
                this.finish();
            }

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    protected void openCamera() {
//        try {
//            // Launch camera to take photo for selected contact
//            PHOTO_DIR.mkdirs();// 创建照片的存储目录
//            mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
//            final Intent intent = getTakePickIntent(mCurrentPhotoFile);
//            startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
//        } catch (ActivityNotFoundException e) {
//        }
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent getImageByCamera = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(getImageByCamera, REQUEST_CODE_CAPTURE_CAMEIA);
        }
        else {
            Toast.makeText(getApplicationContext(), "请确认已经插入SD卡", Toast.LENGTH_LONG).show();
        }
    }

    public static Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            Uri uri = data.getData();
            insertBitmap(getRealFilePath(uri));
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            Uri uri = data.getData();
            if(uri == null){
                //use bundle to get data
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    Bitmap photo = SystemInfo.imageScale((Bitmap) bundle.get("data"),SystemInfo.getScreenWidth(AddArticle.this),SystemInfo.getScreenWidth(AddArticle.this)/2); //get bitmap
                    //spath :生成图片取个名字和路径包含类型
                    PHOTO_DIR.mkdirs();
                    String imgPath= PHOTO_DIR.getPath()+getPhotoFileName();
                    saveImage(photo, imgPath);
                    insertBitmap(imgPath);
                } else {
                    Toast.makeText(getApplicationContext(), "系统异常", Toast.LENGTH_LONG).show();
                    return;
                }
            }else{
                insertBitmap(mCurrentPhotoFile.getAbsolutePath());
                //to do find the path of pic by uri
            }
        }
    }

    /**
     * 添加图片到富文本剪辑器
     *
     * @param imagePath
     */
    private void insertBitmap(String imagePath) {

        editor.insertImage(imagePath);
    }

    /**
     * 根据Uri获取图片文件的绝对路径
     */
    public String getRealFilePath(final Uri uri) {
        if (null == uri) {
            return null;
        }

        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = getContentResolver().query(uri,
                    new String[] { ImageColumns.DATA }, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                Log.i("MainActivity", "申请的权限为：" + permissions[i] + ",申请结果：" + grantResults[i]);
            }
        }
    }

}

