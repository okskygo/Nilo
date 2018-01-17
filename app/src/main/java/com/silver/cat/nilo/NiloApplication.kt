package com.silver.cat.nilo

import android.app.Application
import com.silver.cat.nilo.config.dagger.DaggerNiloComponent
import com.silver.cat.nilo.config.dagger.NiloComponent
import com.silver.cat.nilo.config.dagger.NiloModule
import com.silver.cat.nilo.util.Pref
import javax.inject.Inject

/**
 * Created by xiezhenyu on 2017/1/17.
 */

class NiloApplication : Application() {

  companion object {
    lateinit var component: NiloComponent
  }

  @Inject
  lateinit var pref: Pref

  override fun onCreate() {
    super.onCreate()

    component = DaggerNiloComponent.builder().niloModule(NiloModule(this)).build()
    component.inject(this)

  }
}
