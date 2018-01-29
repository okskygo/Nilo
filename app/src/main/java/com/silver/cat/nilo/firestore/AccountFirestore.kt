package com.silver.cat.nilo.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.dto.CreatorDto
import com.silver.cat.nilo.util.Pref
import com.silver.cat.nilo.util.completable
import com.silver.cat.nilo.util.toFlowable
import com.silver.cat.nilo.util.toListFlowable
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject


class AccountFirestore @Inject constructor(private val firebaseFirestore: FirebaseFirestore,
                                           private val pref: Pref) {

  private fun getAccountCollection() = firebaseFirestore.collection("account")
  private fun getCreatorCollection() = firebaseFirestore.collection("creator")

  fun updateFcmToken(token: String): Completable {
    pref.setFcmToken(token)
    return getAccountCollection().document(pref.getUid()).set(AccountDto(pref.getUid(), token),
        SetOptions.merge()).completable()
  }

  fun updateNickname(nickname: String): Completable {
    return getAccountCollection().document(pref.getUid()).update("nickname", nickname).completable()
  }

  fun updateNID(nid: String): Completable {
    return getAccountCollection().document(pref.getUid()).update("nid", nid).completable()
  }

  fun getCreators(): Flowable<List<AccountDto>> {
    return getCreatorCollection().get().toListFlowable<CreatorDto>().flatMap { creators ->
      if (creators.isNotEmpty()) {
        Flowable.zipIterable(creators.map { getAccount(it.uid) },
            { accounts -> accounts.toList() as List<AccountDto> },
            false,
            creators.count())
      } else {
        Flowable.just(emptyList())
      }
    }
  }

  fun getAccount(uid: String): Flowable<AccountDto> {
    return getAccountCollection().document(uid).get().toFlowable()
  }

//  fun getFriends(): Single<List<AccountDto>> {
//    accountCollection.document(pref.getUid()).get().toFlowable<AccountDto>().flatMap {
//      creatorCollection.get().toListFlowable<String>().map {
//        return@map if (it is Result.Success) {
//          val list: List<Single<Result<AccountDto>>> = it.data.map { accountCollection.document(it).get().toFlowable<AccountDto>() }
//          Single.zip(list, {
//
//          })
//        } else {
//          emptyList()
//        }
//      }
//    }
//    Single.zip(accountCollection.document(pref.getUid()).get().toFlowable<AccountDto>(),
//        creatorCollection.get().toListFlowable<String>(),
//        BiFunction<AccountDto, List<AccountDto>, List<AccountDto>> { user, creator ->
//          val result: MutableList<AccountDto> = mutableListOf()
//          result.addAll(user.friends)
//        })
//
//  }


}