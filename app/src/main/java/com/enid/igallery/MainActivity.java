package com.enid.igallery;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.enid.igallery.permission.PermissionActivity;
import com.enid.igallery.permission.PermissionListener;
import com.enid.igallerydemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.igallery.Configuration.ImageChoiceModel;
import cn.igallery.GalleryOperator;
import cn.igallery.model.ImageModel;
import cn.igallery.rxbus.RxBusResultSubscriber;
import cn.igallery.rxbus.event.ImageCropResultEvent;
import cn.igallery.rxbus.event.ImageMultipleResultEvent;

/**
 * Created by enid on 2017/4/7.
 */

public class MainActivity extends PermissionActivity {
    @BindView(R.id.rbSingle)
    RadioButton rbSingle;

    @BindView(R.id.rbMultiple)
    RadioButton rbMultiple;

    @BindView(R.id.btnOpen)
    Button btnOpen;

    private List<ImageModel> mImageModelList;

    private ImageSelectedFragment imagesFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mImageModelList = new ArrayList<>();
        addImageSelectedGridFragment();
    }

    @OnClick(R.id.btnOpen)
    public void btnOpen() {
        requestPermission();
    }

    private void requestPermission() {
        requestRuntimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, new PermissionListener() {
            @Override
            public void onGranted() {
                if (rbSingle.isChecked())
                    GalleryOperator.getInstance()
                            .selected((ArrayList<ImageModel>) mImageModelList)
                            .setChoiceModel(ImageChoiceModel.SINGLE)
                            .subscribe(new RxBusResultSubscriber<ImageCropResultEvent>() {
                                @Override
                                protected void onEvent(ImageCropResultEvent resultEvent) {
                                    mImageModelList.clear();
                                    mImageModelList.add(resultEvent.getImageModel());
                                    imagesFragment.refreshData(mImageModelList);
                                }
                            })
                            .openGallery(MainActivity.this);
                else if (rbMultiple.isChecked())
                    GalleryOperator.getInstance()
                            .selected((ArrayList<ImageModel>) mImageModelList)
                            .setMaxSize(9)
                            .setChoiceModel(ImageChoiceModel.MULTIPLE)
                            .subscribe(new RxBusResultSubscriber<ImageMultipleResultEvent>() {
                                @Override
                                protected void onEvent(ImageMultipleResultEvent resultEvent) {
                                    mImageModelList.clear();
                                    mImageModelList.addAll(resultEvent.getImageModelList());
                                    imagesFragment.refreshData(mImageModelList);
                                }
                            })
                            .openGallery(MainActivity.this);
            }

            @Override
            public void onDenied(List<String> permissions) {
                for (String permission :
                        permissions) {
                    Toast.makeText(MainActivity.this, "denied " + permission + " permission ", Toast.LENGTH_SHORT).show();
                }
                showPermissionDialog(permissions);
            }
        });
    }

    private void showPermissionDialog(List<String> permissions) {
        StringBuilder stringBuilder = new StringBuilder();
        if (permissions.contains(Manifest.permission.READ_EXTERNAL_STORAGE))
            stringBuilder.append("应用功能需要请求SD卡访问权限" + "\n");
        if (permissions.contains(Manifest.permission.CAMERA)) stringBuilder.append("应用需要请求相机访问权限");
        new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage(stringBuilder.toString())
                .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        requestPermission();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "已取消", Toast.LENGTH_SHORT).show();
                    }
                })
                .create()
                .show();
    }

    private void addImageSelectedGridFragment() {
        imagesFragment = new ImageSelectedFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.container, imagesFragment).commit();
    }
}
