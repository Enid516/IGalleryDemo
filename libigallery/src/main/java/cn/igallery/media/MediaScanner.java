package cn.igallery.media;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;


/**
 * Created by Enid on 2016/9/8.
 * 多媒体文件扫描类
 */
public class MediaScanner {
    private MediaScannerConnection.MediaScannerConnectionClient mediaScannerConnectionClient;
    private MediaScannerConnection mediaScannerConnection;
    private MediaScannerCallBack mediaScannerCallBack;
    private String[] filePaths = null;
    private String fileType = null;

    public MediaScanner(Context context) {
        if (mediaScannerConnectionClient == null) {
            mediaScannerConnectionClient = new ScannerConnectionClient();
        }
        if (mediaScannerConnection == null) {
            mediaScannerConnection = new MediaScannerConnection(context, mediaScannerConnectionClient);
        }
    }

    private class ScannerConnectionClient implements MediaScannerConnection.MediaScannerConnectionClient {

        @Override
        public void onMediaScannerConnected() {
            if (filePaths != null && fileType != null) {
                for (String path : filePaths) {
                    mediaScannerConnection.scanFile(path,fileType);
                }
            }
        }

        @Override
        public void onScanCompleted(String s, Uri uri) {
            mediaScannerConnection.disconnect();
            if (mediaScannerCallBack != null) {
                mediaScannerCallBack.onScanCompleted(new String[]{s});
            }
            filePaths = null;
            fileType = null;
        }
    }

    public void scanImage(MediaScannerCallBack mediaScannerCallBack) {
        scanImage(Environment.getExternalStorageDirectory().getAbsolutePath(), mediaScannerCallBack);
    }

    private void scanImage(String path, MediaScannerCallBack mediaScannerCallBack) {
        scanImage(new String[]{path},mediaScannerCallBack);
    }

    private void scanImage(String[] path, MediaScannerCallBack mediaScannerCallBack) {
        scanFile(path, "image/jpeg",mediaScannerCallBack);
    }

    private void scanFile(String[] path, String fileType, MediaScannerCallBack mediaScannerCallBack) {
        this.filePaths = path;
        this.fileType = fileType;
        this.mediaScannerCallBack = mediaScannerCallBack;
        mediaScannerConnection.connect();
    }

    interface MediaScannerCallBack{
        void onScanCompleted(String[] path);
    }

}
