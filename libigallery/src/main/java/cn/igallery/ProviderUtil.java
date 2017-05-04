package cn.igallery;

import android.content.Context;

/**
 * Created by Enid on 2017/4/25.
 */

public class ProviderUtil {
    public static String getFileProviderName(Context context) {
        return context.getPackageName() + ".provider";
    }
}
