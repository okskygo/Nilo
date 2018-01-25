package com.silver.cat.nilo.service

import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.silver.cat.nilo.firestore.AccountFirestore
import dagger.android.AndroidInjection
import javax.inject.Inject

/**
 * Created by Rex on 2018/1/17.
 */

class NiloFirebaseInstanceIDService : FirebaseInstanceIdService() {

  @Inject
  lateinit var accountFirestore: AccountFirestore

  override fun onCreate() {
    AndroidInjection.inject(this)
    super.onCreate()
  }

  override fun onTokenRefresh() {
    FirebaseInstanceId.getInstance().token?.let {
      accountFirestore.updateFcmToken(it)
    }
  }
}