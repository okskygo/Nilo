package com.silver.cat.nilo.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.util.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.processors.FlowableProcessor
import io.reactivex.processors.PublishProcessor
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AccountFirestore @Inject constructor(private val pref: Pref) {

  private fun accountCollection() = FirebaseFirestore.getInstance().collection("account")

  private val friendsProcessor: FlowableProcessor<List<AccountDto>> = PublishProcessor.create()

  fun friends(): Flowable<List<AccountDto>> = friendsProcessor.toSerialized()

  fun updateFcmToken(token: String): Completable {
    pref.setFcmToken(token)
    return accountCollection()
        .document(pref.getUid())
        .set(AccountDto(pref.getUid(), token), SetOptions.merge())
        .completable()
  }

  fun refreshFriends(): Completable {

    return Single.zip(accountCollection().whereEqualTo("creator", true).get().listSingle()
        ,
        accountCollection().whereEqualTo("friends.${pref.getUid()}", true).whereEqualTo("creator",
            false).get().listSingle()
        ,
        BiFunction<List<AccountDto>, List<AccountDto>, List<AccountDto>> { creators, friends ->
          val result = mutableListOf<AccountDto>()
          result.addAll(creators)
          result.addAll(friends)
          result.sortedByDescending { it.creator }
          result
        }).doOnSuccess { friendsProcessor.onNext(it) }
        .doOnError { friendsProcessor.onError(it) }
        .toCompletable()
  }

  fun updateNickname(nickname: String): Completable {
    return accountCollection()
        .document(pref.getUid())
        .update("nickname", nickname)
        .completable()
  }

  fun updateNid(nid: String): Completable {
    return accountCollection()
        .document(pref.getUid())
        .update("nid", nid)
        .completable()
  }

  @Deprecated("")
  fun getCreators(): Flowable<List<AccountDto>> {
    return accountCollection()
        .whereEqualTo("creator", true)
        .get()
        .listFlowable()
  }

  fun getAccount(uid: String): Flowable<AccountDto> {
    return accountCollection()
        .document(uid)
        .get()
        .flowable()
  }

  fun addFriend(friendUid: String): Flowable<Boolean> {
    return accountCollection()
        .document(friendUid)
        .update("friends.${pref.getUid()}", true)
        .finishFlowable()
  }

}