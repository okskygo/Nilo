package com.silver.cat.nilo.util;

import android.app.Activity;

import com.silver.cat.nilo.config.dagger.activity.ForActivity;

import javax.inject.Inject;

/**
 * Created by xiezhenyu on 2017/1/17.
 */

public class PermissionUtil {

    private final Activity activity;

    @Inject
    public PermissionUtil(@ForActivity Activity activity) {
        this.activity = activity;
    }

    public void test(){
        System.out.println(">>>>>>> activity = " + activity);
    }
}
