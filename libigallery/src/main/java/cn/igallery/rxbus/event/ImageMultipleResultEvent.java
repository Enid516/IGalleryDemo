package cn.igallery.rxbus.event;

import java.util.List;

import cn.igallery.model.ImageModel;


/**
 * Created by enid on 2017/1/5.
 *
 * 完成图片选择需要发送的事件类
 *
 *
 */

public class ImageMultipleResultEvent implements BaseResultEvent {
    private List<ImageModel> imageModelList;

    public ImageMultipleResultEvent(List<ImageModel> imageModelList) {
        this.imageModelList = imageModelList;
    }

    public List<ImageModel> getImageModelList() {
        return imageModelList;
    }
}
