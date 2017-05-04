package cn.igallery.util;

import android.os.Environment;

import cn.igallery.model.ImageModel;

/**
 * Created by Enid on 2016/9/21.
 */
public class Utils {
    public static boolean checkSD() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return false;
        }
        return true;
    }

    public static ImageModel getCameraImageModel(){
        ImageModel imageModel = new ImageModel();
        imageModel.setTitle("camera");
        imageModel.setId("-1");
        return imageModel;
    }
}
