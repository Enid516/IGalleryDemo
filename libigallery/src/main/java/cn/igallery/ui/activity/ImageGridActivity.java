package cn.igallery.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.enid.igallery.R;
import cn.igallery.Configuration;
import cn.igallery.GalleryOperator;
import cn.igallery.MediaScannerHelper;
import cn.igallery.anim.Animation;
import cn.igallery.anim.AnimationListener;
import cn.igallery.anim.SlideInUnderneathAnimation;
import cn.igallery.anim.SlideOutUnderneathAnimation;
import cn.igallery.model.BucketModel;
import cn.igallery.model.ImageModel;
import cn.igallery.rxbus.RxBus;
import cn.igallery.rxbus.RxBusResultSubscriber;
import cn.igallery.rxbus.event.BaseResultEvent;
import cn.igallery.rxbus.event.ImageCropResultEvent;
import cn.igallery.rxbus.event.ImageMultipleResultEvent;
import cn.igallery.ui.adapter.BucketListAdapter;
import cn.igallery.ui.adapter.ImageGridAdapter;
import cn.igallery.util.GalleryUtil;
import cn.igallery.util.MediaUtil;
import cn.igallery.util.Utils;
import cn.igallery.util.ViewUtil;
import rx.Observer;

import static cn.igallery.GalleryOperator.REQUEST_CODE_OPEN_CAMERA;

/**
 * Created by enid on 2016/9/7.
 * scanner image for select
 */
public class ImageGridActivity extends BaseActivity implements View.OnClickListener {
    public static final String EXTRA_CONFIGURATION = "extra_configuration";

    private ImageGridAdapter imageGridAdapter;

    public static final int REQUEST_CODE_FOR_PREVIEW = 0x1001;

    private Button btnOK;

    private TextView btnAllImage;

    private TextView btnPreview;

    private RecyclerView recyclerViewBucket;

    private LinearLayout layoutBucketOverview;

    private ArrayList<ImageModel> mImageList;

    private static final int MAX_LIMIT = 50;

    private String takeImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_image_grid);
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.getInstance().clear();
    }

    private void init() {
        //init view
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        btnOK = (Button) findViewById(R.id.btnOK);
        btnAllImage = (TextView) findViewById(R.id.btnAllImage);
        btnPreview = (TextView) findViewById(R.id.btnPreview);
        recyclerViewBucket = (RecyclerView) findViewById(R.id.recyclerViewBucket);
        recyclerViewBucket.setLayoutManager(new LinearLayoutManager(this));
        layoutBucketOverview = (LinearLayout) findViewById(R.id.layoutBucketOverview);

        //set click listener
        findViewById(R.id.btnReturn).setOnClickListener(this);
        btnOK.setOnClickListener(this);
        btnAllImage.setOnClickListener(this);
        btnPreview.setOnClickListener(this);
        layoutBucketOverview.setOnClickListener(this);

        //get intent data
        Bundle bundle = getIntent().getExtras();
        mConfiguration = (Configuration) bundle.getSerializable(EXTRA_CONFIGURATION);

        //set adapter
        mImageList = new ArrayList<>();
        imageGridAdapter = new ImageGridAdapter(this,mImageList, mConfiguration);

        //register on item onclick listener
        imageGridAdapter.setOnItemOnClickListener(new ImageGridAdapter.OnItemOnClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (position == 0) {
                    takeImagePath = GalleryOperator.getInstance()
                            .setChoiceModel(Configuration.ImageChoiceModel.SINGLE)
                            .subscribe(new RxBusResultSubscriber<ImageCropResultEvent>() {
                                @Override
                                protected void onEvent(ImageCropResultEvent resultEvent) {
                                    ImageModel cameraImageModel = resultEvent.getImageModel();
                                    ImageCropResultEvent event = new ImageCropResultEvent(cameraImageModel);
                                    RxBus.getInstance().post(event);
                                    finish();
                                }
                            })
                            .openCamera(ImageGridActivity.this);
                } else if (mConfiguration.getChoiceModel() == Configuration.ImageChoiceModel.SINGLE) {
                    String path = mImageList.get(position).getOriginalPath();
                    ImageModel imageModel = new ImageModel();
                    imageModel.setOriginalPath(path);
                    ImageCropResultEvent event = new ImageCropResultEvent(imageModel);
                    RxBus.getInstance().post(event);
                    finish();
                } else {
                    ImagePreviewActivity.actionStart(ImageGridActivity.this,mImageList,position,mConfiguration);
                }
            }

            @Override
            public void onItemCheck() {
                btnOK.setText(GalleryUtil.getBtnOKString(mConfiguration.getSelectedList().size(), mConfiguration.getMaxChoiceSize()));
                btnPreview.setText(GalleryUtil.getBtnPreviewString(mConfiguration.getSelectedList().size()));
            }
        });
        recyclerView.setAdapter(imageGridAdapter);

        getImagesData();
        if (mConfiguration.getChoiceModel() == Configuration.ImageChoiceModel.SINGLE) {
            btnOK.setVisibility(View.INVISIBLE);
            btnPreview.setVisibility(View.INVISIBLE);
        }
    }

    private void getImagesData() {
        getImagesWithBucketId(MediaUtil.ALL_IMAGES_BUCKETID);
        getBuckets();
    }

    private void getImagesWithBucketId(String bucketId) {
        Observer<List<ImageModel>> observer = new Observer<List<ImageModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(List<ImageModel> imageModels) {
                initData(imageModels);

            }
        };
        int mCurrentPage = 0;
        MediaScannerHelper.generateImagesWithBucketId(observer, this, bucketId, mCurrentPage + 1, MAX_LIMIT);
    }

    private void initData(List<ImageModel> imageModels) {
        mImageList.add(Utils.getCameraImageModel());
        mImageList.addAll(imageModels);
        imageGridAdapter.setData(mImageList);
    }

    private void getBuckets() {
        Observer<List<BucketModel>> observer = new Observer<List<BucketModel>>() {

            private BucketListAdapter bucketListAdapter;

            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(final List<BucketModel> bucketModels) {
                bucketListAdapter = new BucketListAdapter(ImageGridActivity.this, bucketModels);
                bucketListAdapter.setOnItemOnClickListener(new BucketListAdapter.OnItemOnClickListener() {
                    @Override
                    public void onItemClick(BucketModel bucketModel, int position) {
                        if (!bucketModel.getBucketId().equals(bucketListAdapter.getSelectedBucket().getBucketId())) {
                            bucketListAdapter.setSelectedBucket(bucketModel);
                            bucketListAdapter.notifyDataSetChanged();
                            btnAllImage.setText(bucketModels.get(position).getBucketName());
                            getImagesWithBucketId(bucketModel.getBucketId());
                        }
                        showBucketOverview(false);
                    }
                });
                bucketListAdapter.setSelectedBucket(bucketModels.get(0));
                recyclerViewBucket.setAdapter(bucketListAdapter);
            }
        };
        MediaScannerHelper.getImageBuckets(observer, this, 0, Integer.MAX_VALUE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_FOR_PREVIEW) {
            mConfiguration = (Configuration) data.getSerializableExtra(ImagePreviewActivity.IMAGE_CONFIGURATION_EXTRA);
            if (resultCode == ImagePreviewActivity.RESULT_CODE_SELECTED) {
                btnOK.setText(GalleryUtil.getBtnOKString(mConfiguration.getSelectedList().size(), mConfiguration.getMaxChoiceSize()));
                btnPreview.setText(GalleryUtil.getBtnPreviewString(mConfiguration.getSelectedList().size()));
                imageGridAdapter.setData(mImageList);
            } else if (resultCode == ImagePreviewActivity.RESULT_CODE_COMPLETED) {
                ImageMultipleResultEvent event = new ImageMultipleResultEvent(mConfiguration.getSelectedList());
                RxBus.getInstance().post(event);
                finish();
            }
        } else if (requestCode == REQUEST_CODE_OPEN_CAMERA) {
            ImageModel imageModel = new ImageModel();
            imageModel.setOriginalPath(takeImagePath);
            BaseResultEvent event;
            if (mConfiguration.getChoiceModel() == Configuration.ImageChoiceModel.SINGLE) {
                event = new ImageCropResultEvent(imageModel);
            } else {
                ArrayList<ImageModel> imageModels = new ArrayList<>();
                imageModels.add(imageModel);
                event = new ImageMultipleResultEvent(imageModels);
            }
            if (event != null) RxBus.getInstance().post(event);
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnOK) {
            ImageMultipleResultEvent imageSelectedResult = new ImageMultipleResultEvent(mConfiguration.getSelectedList());
            RxBus.getInstance().post(imageSelectedResult);
            finish();
        } else if (i == R.id.btnReturn) {
            finish();
        } else if (i == R.id.btnPreview) {
            if (mConfiguration.getSelectedList().size() > 0)
                ImagePreviewActivity.actionStart(this, (ArrayList<ImageModel>) mConfiguration.getSelectedList(),0,mConfiguration);
        } else if (i == R.id.btnAllImage || i == R.id.layoutBucketOverview) {
            if (layoutBucketOverview.getVisibility() == View.VISIBLE) {
                showBucketOverview(false);
            } else {
                showBucketOverview(true);
            }
        }
    }

    /**
     * 显示或隐藏bucket列表
     *
     * @param isShow isShow 的值为true显示bucket列表，否则隐藏bucket列表
     */
    private void showBucketOverview(boolean isShow) {
        if (isShow) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.height = ViewUtil.getScreenHeight() / 4 * 3;
            layoutParams.gravity = Gravity.BOTTOM;
            recyclerViewBucket.setLayoutParams(layoutParams);

            layoutBucketOverview.setVisibility(View.VISIBLE);
            new SlideInUnderneathAnimation(recyclerViewBucket)
                    .setDirection(Animation.DIRECTION_DOWN)
                    .setDuration(Animation.DURATION_DEFAULT)
                    .setListener(new AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }
                    }).animate();
        } else {
            new SlideOutUnderneathAnimation(recyclerViewBucket)
                    .setDirection(Animation.DIRECTION_DOWN)
                    .setDuration(Animation.DURATION_DEFAULT)
                    .setListener(new AnimationListener() {
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            layoutBucketOverview.setVisibility(View.INVISIBLE);
                        }
                    }).animate();

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (layoutBucketOverview.getVisibility() == View.VISIBLE) {
                showBucketOverview(false);
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
