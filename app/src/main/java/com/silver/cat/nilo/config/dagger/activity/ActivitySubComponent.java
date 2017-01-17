package com.silver.cat.nilo.config.dagger.activity;

import com.silver.cat.nilo.view.main.MainActivity;

import dagger.Subcomponent;

/**
 * Created by xiezhenyu on 2017/1/17.
 */

@ActivityScope
@Subcomponent(modules = ActivityModule.class)
public interface ActivitySubComponent {

    void inject(MainActivity mainActivity);
}
