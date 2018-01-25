package com.silver.cat.nilo.config.dagger.module

import android.app.Application
import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.silver.cat.nilo.config.dagger.ForApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by xiezhenyu on 2017/1/17.
 */

@Module
class NiloModule {

  @Provides
  @Singleton
  @ForApplication
  fun provideContext(application: Application): Context = application

  @Provides
  fun provideFirebaseFirestore(): FirebaseFirestore = FirebaseFirestore.getInstance()
}
