package com.enid.igallery.utils;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

/**
 * Created by enid on 2017/3/31.
 */

public class ImageLoaderUtil {
    private static final String TAG = "ImageLoader";
    private static ImageLoaderUtil INSTANCE = null;
    private ImageLoaderUtil(){}
    public static ImageLoaderUtil getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new ImageLoaderUtil();
        }
        return INSTANCE;
    }

    public void loadImage(Context context, String path, ImageView imageView){
        Glide.with(context)
                .load(path)
                .centerCrop()
                .into(imageView);
    }
}
