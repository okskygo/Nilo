package com.silver.cat.nilo.firestore

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.util.*
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import javax.inject.Inject


class AccountFirestore @Inject constructor(firebaseFirestore: FirebaseFirestore,
                                           private val pref: Pref) {

  private val accountCollection = firebaseFirestore.collection("account")
  private val creatorCollection = firebaseFirestore.collection("creator")

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

  fun getFriends(): Single<List<AccountDto>> {
    accountCollection.document(pref.getUid()).get().single<AccountDto>().flatMap {
      creatorCollection.get().listSingle<String>().map {
        return@map if (it is Result.Success) {
          val list:List<Single<Result<AccountDto>>> = it.data.map { accountCollection.document(it).get().single<AccountDto>() }
          Single.zip(list, {

          })
        }else{
          emptyList()
        }
      }
    }
    Single.zip(accountCollection.document(pref.getUid()).get().single<AccountDto>(),
        creatorCollection.get().listSingle<String>(),
        BiFunction<AccountDto, List<AccountDto>, List<AccountDto>> { user, creator ->
          val result: MutableList<AccountDto> = mutableListOf()
          result.addAll(user.friends)
        })

  }


}