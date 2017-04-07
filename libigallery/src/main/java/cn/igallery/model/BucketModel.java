package cn.igallery.model;

import android.text.TextUtils;

/**
 * Created by Enid on 2016/9/8.
 */
public class BucketModel {
    private String bucketId;
    private String bucketName;
    private int imageCount;
    private String cover;

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public int getImageCount() {
        return imageCount;
    }

    public void setImageCount(int imageCount) {
        this.imageCount = imageCount;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof BucketModel)) {
            return false;
        }
        return TextUtils.equals(((BucketModel) o).bucketId,getBucketId());
    }
}
