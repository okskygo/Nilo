package com.silver.cat.nilo

import android.app.Application
import com.silver.cat.nilo.config.dagger.DaggerNiloComponent
import com.silver.cat.nilo.config.dagger.NiloComponent

/**
 * Created by xiezhenyu on 2017/1/17.
 */

class NiloApplication : Application() {

  lateinit var component: NiloComponent

  override fun onCreate() {
    super.onCreate()

    component = DaggerNiloComponent.builder().build()
    component.inject(this)

  }
}
