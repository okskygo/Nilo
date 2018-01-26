package com.silver.cat.nilo.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.util.Pref
import com.silver.cat.nilo.util.completable
import io.reactivex.Completable
import javax.inject.Inject


class AccountFirestore @Inject constructor(private val firebaseFirestore: FirebaseFirestore,
                                           private val pref: Pref) {

  private val accountCollection = firebaseFirestore.collection("account")

  fun updateFcmToken(token: String): Completable {
    pref.setFcmToken(token)
    return accountCollection.document(pref.getUid()).set(AccountDto(pref.getUid(), token),
        SetOptions.merge()).completable()
  }

  fun updateNickname(nickname: String): Completable {
    return accountCollection.document(pref.getUid()).update("nickname", nickname).completable()
  }

  fun updateNID(nid: String): Completable {
    return accountCollection.document(pref.getUid()).update("nid", nid).completable()
  }

}