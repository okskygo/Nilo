package com.silver.cat.nilo.config.dagger;

import com.silver.cat.nilo.NiloApplication;
import com.silver.cat.nilo.config.dagger.activity.ActivityModule;
import com.silver.cat.nilo.config.dagger.activity.ActivitySubComponent;
import com.silver.cat.nilo.view.main.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by xiezhenyu on 2017/1/17.
 */

@Singleton
@Component(modules = NiloModule.class)
public interface NiloComponent {

    void inject(NiloApplication application);

    ActivitySubComponent newActivitySubComponent(ActivityModule activityModule);
}
