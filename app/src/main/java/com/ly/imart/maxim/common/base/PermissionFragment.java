
package com.ly.imart.maxim.common.base;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Description : 基础权限fragment Created by Mango on 2018/11/05.
 */
public class PermissionFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    public boolean hasPermission(String... permission) {
        return false;
    }

    public void requestPermissions(String... permission) {
    }

    public void onPermissionDenied(List<String> permissions) {
    }

    public void onPermissionGranted(List<String> permissions) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
