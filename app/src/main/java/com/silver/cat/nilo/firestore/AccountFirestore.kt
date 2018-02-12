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
class AccountFirestore @Inject constructor() {

  private fun accountCollection() = FirebaseFirestore.getInstance().collection("account")

  private val friendsProcessor: FlowableProcessor<List<AccountDto>> = PublishProcessor.create()

  fun friends(): Flowable<List<AccountDto>> = friendsProcessor.toSerialized()

  fun updateFcmToken(uid: String, token: String): Completable {
    return accountCollection()
        .document(uid)
        .set(AccountDto(uid, token), SetOptions.merge())
        .completable()
  }

  fun refreshFriends(uid: String): Completable {

    return Single.zip(accountCollection().whereEqualTo("creator", true).get().listSingle()
        ,
        accountCollection().whereEqualTo("friends.$uid", true).whereEqualTo("creator",
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

  fun updateAvatar(uid: String, url: String): Completable {
    return accountCollection()
        .document(uid)
        .update("avatar", url)
        .completable()
  }

  fun updateNickname(uid: String, nickname: String): Flowable<Boolean> {
    return accountCollection()
        .document(uid)
        .update("nickname", nickname)
        .finishFlowable()
  }

  fun updateMotto(uid: String, motto: String): Flowable<Boolean> {
    return accountCollection()
        .document(uid)
        .update("motto", motto)
        .finishFlowable()
  }

  fun updateNid(uid: String, nid: String): Flowable<Boolean> {
    return accountCollection()
        .document(uid)
        .update("nid", nid)
        .finishFlowable()
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

  fun inviteFriend(uid: String, friendUid: String): Flowable<Boolean> {
    return accountCollection()
        .document(friendUid)
        .update("inviteFriends.$uid", true)
        .finishFlowable()
  }

  fun findAccount(nid: String): Flowable<Optional<AccountDto>> {
    return accountCollection()
        .whereEqualTo("nid", nid)
        .get()
        .listFlowable<AccountDto>()
        .map {
          if (it.isEmpty()) {
            Optional.empty()
          } else {
            Optional.of(it[0])
          }
        }
  }

  fun me(uid: String): Flowable<AccountDto> {
    return accountCollection()
        .document(uid)
        .get()
        .flowable()
  }

}