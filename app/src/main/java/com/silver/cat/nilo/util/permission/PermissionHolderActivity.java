package com.silver.cat.nilo.util.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

/**
 * Created by xiezhenyu on 2017/1/17.
 */

public class PermissionHolderActivity extends Activity {

    private static Request request;
    private OnResult onResult;
    private int PERMISSION_REQUEST_CODE = 1123;
    private boolean granted;

    public static void setRequest(Request request) {
        PermissionHolderActivity.request = request;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(request == null){
            finish();
            return;
        }
        this.onResult = request.getOnResult();
        String[] permissions = request.getPermissions();
        if (hasPermissions(permissions)) {
            this.granted = true;
            finish();
        } else {
            ActivityCompat.requestPermissions(this, permissions,
                    PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (PERMISSION_REQUEST_CODE == requestCode) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                this.granted = true;
            }
        }

        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.onResult != null) {
            this.onResult.response(this.granted);
        }
        request = null;
    }

    private boolean hasPermissions(String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager
                        .PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
}
