package com.silver.cat.nilo.repository

import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.firestore.AccountFirestore
import com.silver.cat.nilo.util.Optional
import com.silver.cat.nilo.util.Pref
import io.reactivex.Flowable
import javax.inject.Inject


class SettingAddFriendRepository @Inject constructor(private val pref: Pref,
                                                     private val accountFirestore: AccountFirestore) {

  fun isMe(nid: String): Boolean = pref.isMe(nid)

  fun addFriend(friendUid: String): Flowable<Boolean> {
    return accountFirestore.addFriend(pref.getUid(), friendUid)
  }

  fun findAccount(nid: String): Flowable<Optional<AccountDto>> {
    return accountFirestore.findAccount(nid)
  }

}