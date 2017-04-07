package com.enid.igallery;

import android.app.Application;

import com.enid.igallerydemo.R;

import cn.igallery.Library;
import cn.igallery.ui.GalleryTheme;

/**
 * Created by enid on 2017/4/6.
 */

public class MApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        initGallery();
    }

    private void initGallery() {
        //step 1: init gallery library
        Library.getInstance().init(this);
        //step 2: init theme
        GalleryTheme theme =  new GalleryTheme.Builder()
                .statusBarColor(R.color.colorPrimary)
                .toolbarColor(R.color.colorPrimary)
                .activityWidgetColor(R.color.colorPrimary)
                .toolbarWidgetColor(R.color.colorPrimary)
                .cropFrameColor(R.color.colorPrimary)
                .build();
        Library.getInstance().setTheme(theme);
    }
}
