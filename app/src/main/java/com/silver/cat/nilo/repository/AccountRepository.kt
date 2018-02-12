package com.silver.cat.nilo.repository

import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.firestore.AccountFirestore
import com.silver.cat.nilo.util.Optional
import com.silver.cat.nilo.util.Pref
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject


class AccountRepository @Inject constructor(private val pref: Pref,
                                            private val accountFirestore: AccountFirestore) {

  fun friends(): Flowable<List<AccountDto>> = accountFirestore.friends()

  fun refreshFriends(): Completable = accountFirestore.refreshFriends(pref.getUid())

  fun me(): Flowable<AccountDto> = accountFirestore.me(pref.getUid())

  fun isMe(nid: String): Boolean = pref.isMe(nid)

  fun inviteFriend(friendUid: String): Flowable<Boolean> {
    return accountFirestore.inviteFriend(pref.getUid(), friendUid)
  }

  fun findAccount(nid: String): Flowable<Optional<AccountDto>> {
    return accountFirestore.findAccount(nid)
  }

}