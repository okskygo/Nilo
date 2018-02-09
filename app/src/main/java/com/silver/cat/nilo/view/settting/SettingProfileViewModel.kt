package com.silver.cat.nilo.view.settting

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.repository.SettingProfileRepository
import com.silver.cat.nilo.util.Result
import com.silver.cat.nilo.util.SchedulerProvider
import com.silver.cat.nilo.util.toLiveData
import com.silver.cat.nilo.util.toResult
import javax.inject.Inject


class SettingProfileViewModel @Inject constructor(private val settingProfileRepository: SettingProfileRepository,
                                                  private val schedulerProvider: SchedulerProvider)

  : ViewModel() {

  val me: LiveData<Result<AccountDto>> by lazy {
    settingProfileRepository.me()
        .toResult(schedulerProvider)
        .toLiveData()
  }

}