package com.silver.cat.nilo.view.settting

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.repository.SettingAddFriendRepository
import com.silver.cat.nilo.util.*
import javax.inject.Inject


class SettingAddFriendViewModel @Inject constructor(private val settingAddFriendRepository: SettingAddFriendRepository,
                                                    private val schedulerProvider: SchedulerProvider)
  : ViewModel() {

  fun isMe(nid: String): Boolean = settingAddFriendRepository.isMe(nid)

  fun addFriend(friendUid: String): LiveData<Result<Boolean>> {
    return settingAddFriendRepository.addFriend(friendUid)
        .toResult(schedulerProvider)
        .toLiveData()
  }

  fun findAccount(nid: String): LiveData<Result<Optional<AccountDto>>> {
    return settingAddFriendRepository.findAccount(nid)
        .toResult(schedulerProvider)
        .toLiveData()
  }

}