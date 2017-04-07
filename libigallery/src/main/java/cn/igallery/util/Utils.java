package cn.igallery.util;

import android.os.Environment;

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
}
