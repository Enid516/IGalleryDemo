package cn.igallery.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.enid.igallery.R;
import cn.igallery.Configuration;
import cn.igallery.model.ImageModel;
import cn.igallery.util.GalleryUtil;
import cn.igallery.util.ViewUtil;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Enid on 2016/10/17.
 */

public class ImagePreviewActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ImagePreviewActivity";
    public static final int RESULT_CODE_SELECTED = 0x00011;
    public static final int RESULT_CODE_COMPLETED = 0x00012;
    public static final String IMAGE_CURRENT_INDEX_EXTRA = "imageCurrentIndex";
    public static final String IMAGE_PREVIEW_TYPE_EXTRA = "imagePreviewType";
    public static final String IMAGE_CONFIGURATION_EXTRA = "configuration";
    private int mCurrentIndex;
    private ViewPager viewPager;
    private CheckBox checkBox;
    private List<ImageModel> previewList = new ArrayList<>();
    private Button btnOK;
    private TextView textTitle;
    private LinearLayout layoutTop , layoutBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_image_preview);
        init();
    }

    private void init() {
        //init view
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        layoutTop = (LinearLayout) findViewById(R.id.layoutTop);
        layoutBottom = (LinearLayout) findViewById(R.id.layoutBottom);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        btnOK = (Button) findViewById(R.id.btnOK);
        textTitle = (TextView) findViewById(R.id.textTitle);
        findViewById(R.id.btnReturn).setOnClickListener(this);

        //get data
        Intent data = getIntent();
        mCurrentIndex = data.getIntExtra(ImagePreviewActivity.IMAGE_CURRENT_INDEX_EXTRA, 0);
        int mPreviewType = data.getIntExtra(ImagePreviewActivity.IMAGE_PREVIEW_TYPE_EXTRA, 0);
        mConfiguration = (Configuration) data.getSerializableExtra(ImagePreviewActivity.IMAGE_CONFIGURATION_EXTRA);
        previewList.addAll(mPreviewType == 0 ? mConfiguration.getImageList() : mConfiguration.getSelectedList());

        //init imageView list
        final List<PhotoView> listView = new ArrayList<>();
        PhotoView photoView;
        for (ImageModel image : previewList) {
            photoView = new PhotoView(this);
            Glide.with(this)
                    .load(image.getOriginalPath())
                    .centerCrop()
                    .into(photoView);
            photoView.setOnPhotoTapListener(new ImageViewClickListener());
            listView.add(photoView);
        }

        //set adapter
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return listView.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                Log.i(TAG,"pagerAdapter: instantiateItem");
                container.addView(listView.get(position));
                return listView.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                Log.i(TAG,"pagerAdapter: destroyItem");
                container.removeView(listView.get(position));
            }
        });

        //set listener
        setListener();

        //set the current index of viewPager
        viewPager.setCurrentItem(mCurrentIndex);

        //set choice model
        if (mConfiguration.getChoiceModel() == Configuration.ImageChoiceModel.MULTIPLE) {
            checkBox.setChecked(mConfiguration.getSelectedList().contains(previewList.get(mCurrentIndex)));
        } else {
            checkBox.setVisibility(View.GONE);
        }

        //init display
        textTitle.setText((mCurrentIndex + 1) + "/" + previewList.size());
        btnOK.setText(GalleryUtil.getBtnOKString(mConfiguration.getSelectedList().size(), mConfiguration.getMaxChoiceSize()));
        btnOK.setOnClickListener(this);
    }


    private void setListener() {
        //添加页面改变监听
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentIndex = position;
                ImageModel imageModel = previewList.get(position);
                checkBox.setChecked(mConfiguration.getSelectedList().contains(imageModel));
                textTitle.setText((position + 1) + "/" + previewList.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //设置checkBox 点击监听
        if (mConfiguration.getChoiceModel() == Configuration.ImageChoiceModel.MULTIPLE) {
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG,"checkBox is checked :" + checkBox.isChecked());
                    ImageModel imageModel = previewList.get(mCurrentIndex);
                    if (checkBox.isChecked()) {
                        String message = mConfiguration.addSelectImage(imageModel);
                        if (!TextUtils.isEmpty(message)) {
                            Toast.makeText(ImagePreviewActivity.this, message, Toast.LENGTH_SHORT).show();
                        }
                        if (!mConfiguration.getSelectedList().contains(imageModel)) {
                            checkBox.setChecked(false);
                        }
                    } else {
                        mConfiguration.removeSelectImage(imageModel);
                    }
                    btnOK.setText(GalleryUtil.getBtnOKString(mConfiguration.getSelectedList().size(), mConfiguration.getMaxChoiceSize()));
                }
            });
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.btnOK) {
            Intent data = new Intent();
            data.putExtra(IMAGE_CONFIGURATION_EXTRA, mConfiguration);
            setResult(RESULT_CODE_COMPLETED, data);
            finish();
        } else if (i == R.id.btnReturn) {
            onBackPressed();
        }
    }

    class ImageViewClickListener implements PhotoViewAttacher.OnPhotoTapListener {

        @Override
        public void onPhotoTap(View view, float x, float y) {
            Log.i(TAG,"click image");
            TranslateAnimation animationTopIn = new TranslateAnimation(0, 0, -1000, 0);
            animationTopIn.setDuration(500);
            TranslateAnimation animationTopOut = new TranslateAnimation(0, 0, 0, -1000);
            animationTopOut.setDuration(1000);
            animationTopOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });


            TranslateAnimation animationBottomIn = new TranslateAnimation(0, 0, ViewUtil.getScreenHeight(), ViewUtil.getScreenHeight() - ViewUtil.getViewHeight(layoutBottom));
            animationBottomIn.setDuration(500);
            TranslateAnimation animationBottomOut = new TranslateAnimation(0, 0, ViewUtil.getScreenHeight() - ViewUtil.getViewHeight(layoutBottom), ViewUtil.getScreenHeight());
            animationBottomOut.setDuration(1000);
            animationBottomOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            if (layoutTop.getVisibility() == View.VISIBLE) {
                layoutTop.setAnimation(animationTopOut);
                layoutBottom.setAnimation(animationBottomOut);
                layoutTop.setVisibility(View.INVISIBLE);
                layoutBottom.setVisibility(View.INVISIBLE);
            } else {
                layoutTop.setVisibility(View.VISIBLE);
                layoutBottom.setVisibility(View.VISIBLE);
                layoutTop.setAnimation(animationTopIn);
                layoutBottom.setAnimation(animationBottomIn);
            }
        }
    }

    @Override
    public void onBackPressed() {
        //setResult（）要放在finish()方法之前调用，onBackPressed()方法会自动调用finish（）方法
        //所以这里setResult()要放到super.onBackPressed()之前
        Intent data = new Intent();
        data.putExtra(IMAGE_CONFIGURATION_EXTRA, mConfiguration);
        setResult(RESULT_CODE_SELECTED, data);
        super.onBackPressed();
    }
}
