package cn.igallery.util;

/**
 * Created by enid on 2016/12/23.
 */

public class GalleryUtil {
    public static String getBtnOKString(int size, int limitSize) {
        String string = "完成";
        if (size == 0) {
            return string;
        }
        return string + "(" + +size + "/" + limitSize + ")";
    }

    public static String getBtnPreviewString(int size) {
        String string = "预览";
        if (size == 0) {
            return string;
        }
        return string + "(" + +size + ")";
    }
}
