package com.movies;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

public class ImageGetter {

    MemoryCache memoryCache=new MemoryCache();
    FileCache fileCache;
    private Map<ImageView, String> imageViews=Collections.synchronizedMap(new WeakHashMap<ImageView, String>());
    ExecutorService executorService;

    public ImageGetter(Context context){
        fileCache=new FileCache(context);
        executorService=Executors.newFixedThreadPool(5);
    }

    final int stub_id=R.drawable.stub_img_small;
    public void DisplayImage(String url, ImageView imageView, float scale)
    {
        imageViews.put(imageView, url);
        Bitmap bitmap=memoryCache.get(url);
        if(bitmap!=null)
            imageView.setImageBitmap(bitmap);
        else
        {
            queuePhoto(url, imageView, scale);
//            imageView.setImageResource(stub_id);
        }
    }

    private void queuePhoto(String url, ImageView imageView, float scale)
    {
        PhotoToLoad p=new PhotoToLoad(url, imageView, scale);
        executorService.submit(new PhotosLoader(p));
    }

    private Bitmap getBitmap(String url, float scale)
    {
        File f=fileCache.getFile(url);

        //from SD cache
        Bitmap b = decodeFile(f, scale);
        if(b!=null)
            return b;

        //from web
        try {
            Bitmap bitmap=null;
            URL imageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)imageUrl.openConnection();
            conn.setConnectTimeout(30000);
            conn.setReadTimeout(30000);
            conn.setInstanceFollowRedirects(true);
            InputStream is=conn.getInputStream();
            OutputStream os = new FileOutputStream(f);
            copyStream(is, os);
            os.close();
            bitmap = decodeFile(f, scale);
            return bitmap;
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    //decodes image and scales it to reduce memory consumption
    private Bitmap decodeFile(File f, float scale){
        try {
            Bitmap img = BitmapFactory.decodeStream(new FileInputStream(f));
            if (scale == 1) {
                return img;
            }
            else {
                return Bitmap.createScaledBitmap(img, Math.round(img.getWidth()*scale),
                                                 Math.round(img.getHeight()*scale), false);
            }
//            //decode image size
//            BitmapFactory.Options o = new BitmapFactory.Options();
//            o.inJustDecodeBounds = true;
//            BitmapFactory.decodeStream(new FileInputStream(f),null,o);
//
//            //Find the correct scale value. It should be the power of 2.
//            final int REQUIRED_SIZE=70;
//            int width_tmp=o.outWidth, height_tmp=o.outHeight;
//            int scale=1;
//            while(true){
//                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
//                    break;
//                width_tmp/=2;
//                height_tmp/=2;
//                scale*=2;
//            }
//
//            //decode with inSampleSize
//            BitmapFactory.Options o2 = new BitmapFactory.Options();
//            o2.inSampleSize=scale;
//            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (FileNotFoundException e) {}
        return null;
    }

    //Task for the queue
    private class PhotoToLoad
    {
        public String url;
        public ImageView imageView;
        public float scale;
        public PhotoToLoad(String u, ImageView i, float s){
            url=u;
            imageView=i;
            scale = s;
        }
    }

    class PhotosLoader implements Runnable {
        PhotoToLoad photoToLoad;
        PhotosLoader(PhotoToLoad photoToLoad){
            this.photoToLoad=photoToLoad;
        }

        @Override
        public void run() {
            if(imageViewReused(photoToLoad))
                return;
            Bitmap bmp=getBitmap(photoToLoad.url, photoToLoad.scale);
            memoryCache.put(photoToLoad.url, bmp);
            if(imageViewReused(photoToLoad))
                return;
            BitmapDisplayer bd=new BitmapDisplayer(bmp, photoToLoad);
            Activity a=(Activity)photoToLoad.imageView.getContext();
            a.runOnUiThread(bd);
        }
    }

    boolean imageViewReused(PhotoToLoad photoToLoad){
        String tag=imageViews.get(photoToLoad.imageView);
        if(tag==null || !tag.equals(photoToLoad.url))
            return true;
        return false;
    }

    //Used to display bitmap in the UI thread
    class BitmapDisplayer implements Runnable
    {
        Bitmap bitmap;
        PhotoToLoad photoToLoad;
        public BitmapDisplayer(Bitmap b, PhotoToLoad p){bitmap=b;photoToLoad=p;}
        public void run()
        {
            if(imageViewReused(photoToLoad))
                return;
            if(bitmap!=null)
                photoToLoad.imageView.setImageBitmap(bitmap);
            else
                photoToLoad.imageView.setImageResource(stub_id);
        }
    }

    public void clearCache() {
        memoryCache.clear();
        fileCache.clear();
    }
    public static void copyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
                int count=is.read(bytes, 0, buffer_size);
                if(count==-1)
                    break;
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }

}
//import android.content.Context;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.net.http.AndroidHttpClient;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.widget.ImageView;
//import org.apache.http.HttpEntity;
//import org.apache.http.HttpResponse;
//import org.apache.http.HttpStatus;
//import org.apache.http.client.methods.HttpGet;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.ref.WeakReference;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Created with IntelliJ IDEA.
// * User: keemax
// * Date: 7/11/12
// * Time: 1:00 PM
// * To change this template use File | Settings | File Templates.
// */
//public class ImageGetter extends AsyncTask<String, Void, Bitmap> {
//    private final static String tag = "IMAGE_GETTER";
//    private final static String baseurl = "http://cf2.imgobject.com/t/p/";
//    private String viewId;
//    private final Map<String, Bitmap> cache;
//
//    private ImageView imageView;
//
//    public ImageGetter(Context inCtx) {
////        imageView = inImageView;
////        viewId = imageView.getTag().toString();
//        cache = new HashMap<String, Bitmap>();
//    }
//
//    public Bitmap downloadBitmap(String url, float scale) {
//        if (cache.containsKey(url)) {
//            return cache.get(url);
//        }
//        final AndroidHttpClient client = AndroidHttpClient.newInstance("Android");
//        final HttpGet getRequest = new HttpGet(url);
//
//        try {
//            HttpResponse response = client.execute(getRequest);
//            final int statusCode = response.getStatusLine().getStatusCode();
//            if (statusCode != HttpStatus.SC_OK) {
//                Log.w("ImageGetter", "Error " + statusCode + " while retrieving bitmap from " + url);
//                return null;
//            }
//
//            final HttpEntity entity = response.getEntity();
//            if (entity != null) {
//                InputStream inputStream = null;
//                try {
//                    inputStream = entity.getContent();
//                    Bitmap img = BitmapFactory.decodeStream(inputStream);
//                    if (scale == 1) {
//                        cache.put(url, img);
//                        return img;
//                    }
//                    else {
//                        Bitmap scaledImg = Bitmap.createScaledBitmap(img, Math.round(img.getWidth()*scale), Math.round(img.getHeight()*scale), false);
//                        cache.put(url, scaledImg);
//                        return scaledImg;
//                    }
//                } finally {
//                    if (inputStream != null) {
//                        inputStream.close();
//                    }
//                    entity.consumeContent();
//                }
//            }
//        } catch (Exception e) {
//            getRequest.abort();
//            Log.d("ImageDownloader", "Error while retrieving bitmap from " + url);
//        } finally {
//            if (client != null) {
//                client.close();
//            }
//        }
//        return null;
//    }
//    @Override
//    public Bitmap doInBackground(String... params) {
//        //0 = size, 1 = path, 2 = scale, 3=listview id
//        if (params[1].equals("null")) {
//            return null; //TODO:blank picture needed
//        }
//        else {
//            String url = baseurl + params[0] + params[1];
//            float scale = Float.parseFloat(params[2]);
//            Bitmap img = downloadBitmap(url, scale);
//            return img;
//        }
//    }
//    @Override
//    protected void onPostExecute(Bitmap bitmap) {
//        if (!imageView.getTag().toString().equals(viewId)) {
//            return;
//        }
//        else {
//            imageView.setImageBitmap(bitmap);
//        }
//    }
//}
