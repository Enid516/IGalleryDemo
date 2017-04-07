package cn.igallery.job;

import android.content.Context;

import java.io.File;

import cn.igallery.model.ImageModel;
import cn.igallery.util.BitmapUtils;
import cn.igallery.util.MediaUtil;

/**
 * Created by Enid on 2016/9/23.
 */
public class ImageThumbnailJob implements Job {
    private Context mContext;
    private ImageModel mImageModel;
    public ImageThumbnailJob(Context context, ImageModel imageModel) {
        this.mContext = context;
        this.mImageModel = imageModel;
    }

    @Override
    public Result onRunJob() {
        String originalPath = mImageModel.getOriginalPath();
        File thumbnailBigFileName = MediaUtil.createThumbnailBigFileName(mContext, originalPath);
        File thumbnailSmallFileName = MediaUtil.createThumbnailSmallFileName(mContext, originalPath);
        if (!thumbnailBigFileName.exists()) {
            BitmapUtils.createThumbnailBig(thumbnailBigFileName,originalPath);
        }
        if (!thumbnailSmallFileName.exists()) {
            BitmapUtils.createThumbnailSmall(thumbnailSmallFileName,originalPath);
        }
        return Result.SUCCESS;
    }
}
