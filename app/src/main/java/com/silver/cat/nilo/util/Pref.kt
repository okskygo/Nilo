package com.silver.cat.nilo.util

import android.content.Context
import com.silver.cat.nilo.config.dagger.ForApplication
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Rex on 2018/1/17.
 */

@Singleton
class Pref @Inject constructor(@ForApplication private val context: Context) {

  private val niloPreferenceName = "niloPreferenceName"

  private val niloPreferences by lazy {
    context.getSharedPreferences(niloPreferenceName, Context.MODE_PRIVATE)
  }

  fun getUid(): String {
    val uid = niloPreferences.getString("uid", null)
    if (uid != null) {
      return uid
    }
    val genUid = UUID.randomUUID().toString()
    val editor = niloPreferences.edit()
    editor.putString("uid", genUid)
    editor.apply()
    return genUid
  }

  fun setFcmToken(token: String) {
    val editor = niloPreferences.edit()
    editor.putString("fcmToken", token)
    editor.apply()
  }

  fun getFcmToke(): String? {
    return niloPreferences.getString("fcmToken", null)
  }

}