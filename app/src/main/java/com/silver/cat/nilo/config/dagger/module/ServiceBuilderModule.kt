package com.silver.cat.nilo.config.dagger.module

import com.silver.cat.nilo.service.NiloFirebaseInstanceIDService
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface ServiceBuilderModule {

  @ContributesAndroidInjector
  fun contributeNiloFirebaseInstanceIDService(): NiloFirebaseInstanceIDService

}