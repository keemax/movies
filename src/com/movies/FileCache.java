package com.movies;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: keemax
 * Date: 8/8/12
 * Time: 11:48 AM
 * To change this template use File | Settings | File Templates.
 */
public class FileCache {

    private File cacheDir;

    public FileCache(Context context){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(android.os.Environment.getExternalStorageDirectory(),"LazyList");
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url){
        String filename = "";
        try {
            filename= URLEncoder.encode(url, "utf-8");
        } catch(Exception e) {
            Log.d("FILE_CACHE", "unable to encode url for filename");
        }
        File f = new File(cacheDir, filename);
        return f;

    }

    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

}
