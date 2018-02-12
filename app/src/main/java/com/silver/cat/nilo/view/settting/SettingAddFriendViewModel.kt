package com.silver.cat.nilo.view.settting

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.repository.AccountRepository
import com.silver.cat.nilo.util.*
import javax.inject.Inject


class SettingAddFriendViewModel @Inject constructor(private val accountRepository: AccountRepository,
                                                    private val schedulerProvider: SchedulerProvider)
  : ViewModel() {

  fun isMe(nid: String): Boolean = accountRepository.isMe(nid)

  fun isFriend(accountDto: AccountDto): Boolean = accountRepository.isFriend(accountDto)

  fun isInvite(accountDto: AccountDto): Boolean = accountRepository.isInvite(accountDto)

  fun inviteFriend(friendUid: String): LiveData<Result<Boolean>> {
    return accountRepository.inviteFriend(friendUid)
        .toResult(schedulerProvider)
        .toLiveData()
  }

  fun findAccount(nid: String): LiveData<Result<Optional<AccountDto>>> {
    return accountRepository.findAccount(nid)
        .toResult(schedulerProvider)
        .toLiveData()
  }

}