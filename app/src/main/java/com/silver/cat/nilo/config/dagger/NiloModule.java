package com.silver.cat.nilo.config.dagger;

import android.content.Context;

import com.silver.cat.nilo.NiloApplication;
import com.silver.cat.nilo.util.PermissionUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by xiezhenyu on 2017/1/17.
 */

@Module
public class NiloModule {

    private final NiloApplication application;

    public NiloModule(NiloApplication application){
        this.application = application;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    @ForApplication
    Context provideApplicationContext() {
        return application;
    }

}
