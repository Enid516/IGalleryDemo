package cn.igallery.rxbus.event;


import cn.igallery.model.ImageModel;

/**
 * Created by big_love on 2017/1/9.
 */

public class ImageCropResultEvent implements BaseResultEvent {
    private ImageModel imageModel;

    public ImageCropResultEvent(ImageModel imageModel) {
        this.imageModel = imageModel;
    }

    public ImageModel getImageModel() {
        return imageModel;
    }
}
