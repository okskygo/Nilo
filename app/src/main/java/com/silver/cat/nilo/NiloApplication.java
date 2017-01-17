package com.silver.cat.nilo;

import android.app.Application;

import com.silver.cat.nilo.config.dagger.DaggerNiloComponent;
import com.silver.cat.nilo.config.dagger.NiloComponent;
import com.silver.cat.nilo.config.dagger.NiloModule;

/**
 * Created by xiezhenyu on 2017/1/17.
 */

public class NiloApplication extends Application {

    private NiloComponent component;

    @Override
    public void onCreate() {
        super.onCreate();

        component = DaggerNiloComponent.builder().niloModule(new NiloModule(this)).build();
        component.inject(this);

    }

    public NiloComponent getComponent() {
        return component;
    }
}
