package com.silver.cat.nilo.repository

import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.firestore.AccountFirestore
import com.silver.cat.nilo.util.Pref
import io.reactivex.Completable
import io.reactivex.Flowable
import javax.inject.Inject


class FriendListRepository @Inject constructor(private val pref: Pref,
                                               private val accountFirestore: AccountFirestore) {

  fun friends(): Flowable<List<AccountDto>> = accountFirestore.friends()

  fun refreshFriends(): Completable = accountFirestore.refreshFriends(pref.getUid())
}