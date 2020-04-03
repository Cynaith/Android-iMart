package com.ly.imart.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class BitmapUtils {
    public Bitmap returnBitMap(final String src){
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<Bitmap> future = es.submit(new getBitmap(src));
        Bitmap bitmap = null;
        try {
            bitmap = future.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    class getBitmap implements Callable<Bitmap> {

        String src;

        public getBitmap(String src) {
            this.src = src;
        }

        @Override
        public Bitmap call() throws Exception {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        }
    }
}
