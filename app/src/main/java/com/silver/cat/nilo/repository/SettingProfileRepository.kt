package com.silver.cat.nilo.repository

import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.firestore.AccountFirestore
import com.silver.cat.nilo.util.Pref
import io.reactivex.Flowable
import javax.inject.Inject


class SettingProfileRepository @Inject constructor(private val pref: Pref,
                                                   private val accountFirestore: AccountFirestore) {

  fun me(): Flowable<AccountDto> = accountFirestore.me(pref.getUid())
}