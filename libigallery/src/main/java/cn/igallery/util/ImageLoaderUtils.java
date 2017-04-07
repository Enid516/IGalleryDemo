package cn.igallery.util;//package cn.hth.igallery.util;
//
//import android.content.Context;
//import android.widget.ImageView;
//
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//
//
///**
// * Created by Enid on 2016/9/18.
// */
//public class ImageLoaderUtils {
//    public static ImageLoaderUtils INSTANCE;
//    private ImageLoaderUtils() {
//        INSTANCE = this;
//    }
//
//    public static ImageLoaderUtils getInstance(Context context) {
//        if (INSTANCE == null) {
//            INSTANCE = new ImageLoaderUtils();
//        }
//        init(context);
//
//        return INSTANCE;
//    }
//
//    public static void init(Context context) {
//        if (!ImageLoader.getInstance().isInited()) {
//            ImageLoaderConfiguration configuration = ImageLoaderConfiguration.createDefault(context);
//            ImageLoader.getInstance().init(configuration);
//        }
//    }
//
//    public void displayImage(String uri, ImageView imageView) {
//        ImageLoader.getInstance().displayImage("file://" + uri,imageView);
//    }
//
//
//}
