package com.silver.cat.nilo.config.dagger.activity;

import android.app.Activity;

import com.silver.cat.nilo.util.PermissionUtil;

import dagger.Module;
import dagger.Provides;

/**
 * Created by xiezhenyu on 2017/1/17.
 */

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    @ForActivity
    Activity provideActivity() {
        return activity;
    }

}
