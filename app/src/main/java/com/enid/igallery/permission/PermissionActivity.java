package com.enid.igallery.permission;

import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by enid on 2016/12/29.
 */

public class PermissionActivity extends AppCompatActivity {
    private PermissionListener mPermissionListener;
    private static final int CODE_REQUEST_PERMISSION = 1;

    public void requestRuntimePermission(String[] permissions,PermissionListener listener) {
        this.mPermissionListener = listener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        if (permissionList.isEmpty()) {
            mPermissionListener.onGranted();
        } else {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), CODE_REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_REQUEST_PERMISSION) {
            List<String> permissionList = new ArrayList<>();
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permissions[i]);
                }
            }
            if (permissionList.isEmpty()) {
                mPermissionListener.onGranted();
            } else {
                mPermissionListener.onDenied(permissionList);
            }
        }
    }
}
