package cn.igallery.ui;

/**
 * Created by enid on 2017/4/6.
 * the theme of gallery
 */

public class GalleryTheme {
    private int statusBarColor;
    private int toolbarColor;
    private int activityWidgetColor;
    private int toolbarWidgetColor;
    private int cropFrameColor;
    public GalleryTheme(Builder builder){
        this.statusBarColor = builder.statusBarColor;
        this.toolbarColor = builder.toolbarColor;
        this.activityWidgetColor = builder.activityWidgetColor;
        this.toolbarWidgetColor = builder.toolbarWidgetColor;
        this.cropFrameColor = builder.cropFrameColor;
    }

    public int getStatusBarColor() {
        return statusBarColor;
    }

    public int getToolbarColor() {
        return toolbarColor;
    }

    public int getActivityWidgetColor() {
        return activityWidgetColor;
    }

    public int getToolbarWidgetColor() {
        return toolbarWidgetColor;
    }

    public int getCropFrameColor() {
        return cropFrameColor;
    }

    public static class Builder {
        private int statusBarColor;
        private int toolbarColor;
        private int activityWidgetColor;
        private int toolbarWidgetColor;
        private int cropFrameColor;

        public Builder statusBarColor(int statusBarColor){
            this.statusBarColor = statusBarColor;
            return this;
        }

        public Builder toolbarColor(int toolbarColor) {
            this.toolbarColor = toolbarColor;
            return this;
        }

        public Builder activityWidgetColor(int activityWidgetColor) {
            this.activityWidgetColor = activityWidgetColor;
            return this;
        }

        public Builder toolbarWidgetColor(int toolbarWidgetColor) {
            this.toolbarWidgetColor = toolbarWidgetColor;
            return this;
        }

        public Builder cropFrameColor(int cropFrameColor) {
            this.cropFrameColor = cropFrameColor;
            return this;
        }

        public GalleryTheme build(){
            return new GalleryTheme(this);
        }
    }
}
