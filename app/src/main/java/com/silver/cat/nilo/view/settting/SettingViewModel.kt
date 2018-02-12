package com.silver.cat.nilo.view.settting

import android.arch.lifecycle.*
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.repository.AccountRepository
import com.silver.cat.nilo.util.Result
import com.silver.cat.nilo.util.SchedulerProvider
import com.silver.cat.nilo.util.toResult
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject


class SettingViewModel @Inject constructor(private val accountRepository: AccountRepository,
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

}