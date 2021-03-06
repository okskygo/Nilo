package com.silver.cat.nilo.view.settting

import android.arch.lifecycle.*
import android.net.Uri
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.repository.AccountRepository
import com.silver.cat.nilo.util.*
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject


class SettingProfileViewModel @Inject constructor(private val accountRepository: AccountRepository,
                                                  private val schedulerProvider: SchedulerProvider)

  : ViewModel(), LifecycleObserver {

  private val compositeDisposable: CompositeDisposable = CompositeDisposable()

  private val meMutableLiveData: MutableLiveData<Result<AccountDto>> = MutableLiveData()

  init {
    accountRepository.meObservable
        .subscribeBy(
            onNext = { meMutableLiveData.value = Result.success(it) }
        )
        .addTo(compositeDisposable)
  }

  val me: LiveData<Result<AccountDto>>
    get() = meMutableLiveData

  private fun loadMe() {
    accountRepository.me()
        .toResult(schedulerProvider)
        .subscribeBy(
            onNext = { meMutableLiveData.value = it }
        )
        .addTo(compositeDisposable)
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  fun onCreate() {
    loadMe()
  }

  override fun onCleared() {
    super.onCleared()
    compositeDisposable.clear()
  }

  fun updateAvatar(uri: Uri): LiveData<Result<Optional<Uri>>> {
    return accountRepository.updateAvatar(uri)
        .toResult(schedulerProvider)
        .toLiveData()
  }

  fun updateNickname(nickname: String): LiveData<Result<Boolean>> {
    return accountRepository.updateNickname(nickname)
        .toResult(schedulerProvider)
        .toLiveData()
  }

  fun updateMotto(motto: String): LiveData<Result<Boolean>> {
    return accountRepository.updateMotto(motto)
        .toResult(schedulerProvider)
        .toLiveData()
  }

  fun updateNid(nid: String): LiveData<Result<Boolean>> {
    return accountRepository.updateNid(nid)
        .toResult(schedulerProvider)
        .toLiveData()
  }

}