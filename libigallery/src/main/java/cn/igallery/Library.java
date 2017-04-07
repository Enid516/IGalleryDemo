package cn.igallery;

import android.app.Application;

import cn.igallery.ui.GalleryTheme;

/**
 * Created by enid on 2017/4/6.
 */

public class Library {
    private static  Library INSTANCE;
    private Application mApplication;
    private GalleryTheme mTheme;
    private Library(){}
    public static Library getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new Library();
        }
        return INSTANCE;
    }

    public void init(Application application) {
        this.mApplication = application;
    }

    public Application getApplication(){
        return this.mApplication;
    }

    public void setTheme(GalleryTheme theme){
        this.mTheme = theme;
    }

    public GalleryTheme getTheme() {
        return this.mTheme;
    }
}
