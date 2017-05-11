package com.silver.cat.nilo.config.dagger

import android.content.Context

import com.silver.cat.nilo.NiloApplication

import javax.inject.Singleton

import dagger.Module
import dagger.Provides

/**
 * Created by xiezhenyu on 2017/1/17.
 */

@Module
class NiloModule(private val application: NiloApplication) {

    /**
     * Allow the application context to be injected but require that it be annotated with
     * [@Annotation][ForApplication] to explicitly differentiate it from an activity context.
     */
    @Provides
    @Singleton
    @ForApplication
    internal fun provideApplicationContext(): Context {
        return application
    }

}
