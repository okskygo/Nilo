package com.silver.cat.nilo.util

import android.content.Context
import com.google.firebase.iid.FirebaseInstanceId
import com.silver.cat.nilo.config.dagger.ForApplication
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Rex on 2018/1/17.
 */

@Singleton
class ChatDaemon @Inject constructor(@ForApplication private val context: Context,
                                     private val pref: Pref) {

  fun getFcmToken(): String? {
    return pref.getFcmToke() ?: FirebaseInstanceId.getInstance().token?.also {
      pref.setFcmToken(it)
    }
  }

}