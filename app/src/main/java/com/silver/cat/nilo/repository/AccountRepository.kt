package com.silver.cat.nilo.repository

import android.net.Uri
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.firestorage.NiloFirebaseStorage
import com.silver.cat.nilo.firestore.AccountFirestore
import com.silver.cat.nilo.util.Optional
import com.silver.cat.nilo.util.Pref
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AccountRepository @Inject constructor(private val pref: Pref,
                                            private val accountFirestore: AccountFirestore,
                                            private val firebaseStorage: NiloFirebaseStorage) {

  private val meSubject = PublishSubject.create<AccountDto>()
  val meObservable: Observable<AccountDto>
    get() = meSubject

  private var cacheMe: AccountDto? = null
    set(value) {
      field = value
      value?.let { meSubject.onNext(it) }
    }

  fun friends(): Flowable<List<AccountDto>> = accountFirestore.friends()

  fun refreshFriends(): Completable = accountFirestore.refreshFriends(pref.getUid())

  fun me(): Flowable<AccountDto> {
    return if (cacheMe == null) {
      accountFirestore.me(pref.getUid()).doOnNext {
        cacheMe = it
      }
    } else {
      Flowable.just(cacheMe)
    }
  }

  fun isMe(nid: String): Boolean = pref.isMe(nid)

  fun inviteFriend(friendUid: String): Flowable<Boolean> {
    return accountFirestore.inviteFriend(pref.getUid(), friendUid)
  }

  fun findAccount(nid: String): Flowable<Optional<AccountDto>> {
    return accountFirestore.findAccount(nid)
  }

  fun updateAvatar(uri: Uri): Flowable<Optional<Uri>> {
    return firebaseStorage.uploadAvatar(pref.getUid(), uri)
        .flatMap { optional ->
          if (optional.isPresent) {
            accountFirestore.updateAvatar(pref.getUid(), optional.get().toString())
                .toSingle { optional }
                .toFlowable()
          } else {
            Flowable.just(optional)
          }
        }
        .doOnNext {
          val cacheMe = cacheMe
          if (it.isPresent && cacheMe != null) {
            this.cacheMe = cacheMe.copy(avatar = it.get().toString())
          }
        }
  }

  fun updateNickname(nickname: String): Flowable<Boolean> {
    return accountFirestore.updateNickname(pref.getUid(), nickname)
        .doOnNext {
          val cacheMe = cacheMe
          if (cacheMe != null) {
            this.cacheMe = cacheMe.copy(nickname = nickname)
          }
        }
  }

  fun updateMotto(motto: String): Flowable<Boolean> {
    return accountFirestore.updateMotto(pref.getUid(), motto)
        .doOnNext {
          val cacheMe = cacheMe
          if (cacheMe != null) {
            this.cacheMe = cacheMe.copy(motto = motto)
          }
        }
  }

  fun updateNid(nid: String): Flowable<Boolean> {
    return accountFirestore.updateNid(pref.getUid(), nid)
        .doOnNext {
          val cacheMe = cacheMe
          if (cacheMe != null) {
            this.cacheMe = cacheMe.copy(nid = nid)
          }
        }
  }

  fun isFriend(accountDto: AccountDto): Boolean {
    return accountDto.friends.containsKey(pref.getUid())
  }

  fun isInvite(accountDto: AccountDto): Boolean {
    return accountDto.inviteFriends.containsKey(pref.getUid())
  }

}