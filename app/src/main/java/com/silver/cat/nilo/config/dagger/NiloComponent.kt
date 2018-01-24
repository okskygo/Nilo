package com.silver.cat.nilo.config.dagger

import android.app.Application
import com.silver.cat.nilo.NiloApplication
import com.silver.cat.nilo.config.dagger.module.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

/**
 * Created by xiezhenyu on 2017/1/17.
 */

@Singleton
@Component(modules = [
  (AndroidInjectionModule::class),
  (NiloModule::class),
  (ActivityBuilderModule::class),
  (ServiceBuilderModule::class),
  (FragmentBuilderModule::class)
])
interface NiloComponent {
  @Component.Builder
  interface Builder {
    @BindsInstance
    fun application(application: Application): Builder
    fun build(): NiloComponent
  }

  fun inject(application: NiloApplication)
}
