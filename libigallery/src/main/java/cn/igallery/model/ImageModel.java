package cn.igallery.model;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Enid on 2016/9/8.
 * Image类，从数据库读取到的图片信息
 */
public class ImageModel implements Serializable{
    private static final long serialVersionUID = -1;
    private String id;
    private String bucketId;
    private String title;
    private String originalPath;



    //大缩略图
    private String thumbnailBigPath;
    //小缩略图
    private String thumbnailSmallPath;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(String originalPath) {
        this.originalPath = originalPath;
    }

    public String getThumbnailBigPath() {
        return thumbnailBigPath;
    }

    public void setThumbnailBigPath(String thumbnailBigPath) {
        this.thumbnailBigPath = thumbnailBigPath;
    }

    public String getThumbnailSmallPath() {
        return thumbnailSmallPath;
    }

    public void setThumbnailSmallPath(String thumbnailSmallPath) {
        this.thumbnailSmallPath = thumbnailSmallPath;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof ImageModel)) {
            return false;
        }
        return TextUtils.equals(((ImageModel)o).originalPath,getOriginalPath());
    }
}
