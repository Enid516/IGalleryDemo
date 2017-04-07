package cn.igallery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import cn.igallery.model.ImageModel;
import cn.igallery.rxbus.RxBus;
import cn.igallery.rxbus.RxBusResultSubscriber;
import cn.igallery.rxbus.RxBusSubscriber;
import cn.igallery.rxbus.event.BaseResultEvent;
import cn.igallery.rxbus.event.ImageCropResultEvent;
import cn.igallery.rxbus.event.ImageMultipleResultEvent;
import cn.igallery.ui.activity.ImageGridActivity;
import cn.igallery.util.Utils;
import rx.Subscription;

/**
 * Created by Enid on 2016/9/21.
 */
public class GalleryOperator {
    private static final String TAG = "GalleryOperator";
    private static GalleryOperator INSTANCE;
    private Configuration configuration = new Configuration();
    private RxBusSubscriber mRxBusResultSubscriber;
//    private Context mContext;
    public static final int REQUEST_CODE_OPEN_CAMERA = 2;

    private GalleryOperator() {
    }

    public static GalleryOperator getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GalleryOperator();
        }
        return INSTANCE;
    }

    public GalleryOperator setChoiceModel(Configuration.ImageChoiceModel choiceModel) {
        configuration.setChoiceModel(choiceModel);
        return this;
    }

    public GalleryOperator setMaxSize(@IntRange(from = 1) int size) {
        configuration.setMaxChoiceSize(size);
        return this;
    }

    public GalleryOperator selected(@NonNull ArrayList<ImageModel> selectedList) {
        configuration.setSelectedList(selectedList);
        return this;
    }

    public void openGallery(Context context) {
        execute(context);
    }

    private void execute(Context context) {
        if (context == null)
            return;
        if (!Utils.checkSD()) {
            Toast.makeText(context, "SD card does not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        if (mRxBusResultSubscriber == null) {
            return;
        }
        Subscription subscription;
        if (configuration.getChoiceModel() == Configuration.ImageChoiceModel.SINGLE) {
            subscription = RxBus.getInstance()
                    .toObservable(ImageCropResultEvent.class)
                    .subscribe(mRxBusResultSubscriber);
        } else {
            subscription = RxBus.getInstance()
                    .toObservable(ImageMultipleResultEvent.class)
                    .subscribe(mRxBusResultSubscriber);
        }
        RxBus.getInstance().add(subscription);
        Intent intent = new Intent(context, ImageGridActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ImageGridActivity.EXTRA_CONFIGURATION, configuration);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    public GalleryOperator subscribe(RxBusResultSubscriber<? extends BaseResultEvent> rxBusResultSubscriber) {
        this.mRxBusResultSubscriber = rxBusResultSubscriber;
        return this;
    }

    private final String IMAGE_STORE_FILE_NAME = "IMG_%s.jpg";

    /**
     * open camera
     *
     * @param context
     * @return
     */
    public String openCamera(Activity context) {
        String imagePath = "";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            //set photo output path
            File imageStoreDir = new File(Environment.getExternalStorageDirectory(), "/DCIM/iGallery/");
            if (!imageStoreDir.exists()) {
                imageStoreDir.mkdirs();
            }
            String fileName = String.format(IMAGE_STORE_FILE_NAME, new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA).format(new Date()));
            imagePath = new File(imageStoreDir, fileName).getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(imagePath)));
            //open camera
            context.startActivityForResult(intent, REQUEST_CODE_OPEN_CAMERA);
        } else {
            Log.e(TAG,"the camera is not available");
        }
        return imagePath;
    }

}
