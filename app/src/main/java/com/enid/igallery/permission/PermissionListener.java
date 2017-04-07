package com.enid.igallery.permission;

import java.util.List;

/**
 * Created by big_love on 2016/12/29.
 */

public interface PermissionListener {
    void onGranted();
    void onDenied(List<String> permissions);
}
