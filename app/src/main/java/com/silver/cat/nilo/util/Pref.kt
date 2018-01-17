package com.silver.cat.nilo.util

import android.content.Context
import android.content.SharedPreferences
import com.silver.cat.nilo.config.dagger.ForApplication
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Rex on 2018/1/17.
 */

@Singleton
class Pref @Inject constructor(@ForApplication private val context: Context) {

  private val curryPreferenceName = "com.silver.cat.nilo.preference"

  private fun getCurryPreferences(): SharedPreferences {
    return context.getSharedPreferences(curryPreferenceName, Context.MODE_PRIVATE)
  }

  fun setFcmToken(token: String) {
    val editor = getCurryPreferences().edit()
    editor.putString("fcmToken", token)
    editor.apply()
  }

  fun getFcmToke(): String? {
    return getCurryPreferences().getString("fcmToken", null)
  }

}