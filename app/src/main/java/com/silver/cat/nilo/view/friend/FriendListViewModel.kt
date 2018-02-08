package com.silver.cat.nilo.view.friend

import android.arch.lifecycle.*
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.repository.FriendListRepository
import com.silver.cat.nilo.util.Result
import com.silver.cat.nilo.util.SchedulerProvider
import com.silver.cat.nilo.util.toLiveData
import com.silver.cat.nilo.util.toResult
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject


class FriendListViewModel @Inject constructor(private val friendListRepository: FriendListRepository,
                                              private val schedulerProvider: SchedulerProvider)
  : ViewModel(), LifecycleObserver {

  private val compositeDisposable: CompositeDisposable = CompositeDisposable()

  val friends: LiveData<Result<List<AccountDto>>> by lazy {
    friendListRepository.friends()
        .toResult(schedulerProvider)
        .toLiveData()
  }

  private val mutableRefreshState: MutableLiveData<Result<Unit>> = MutableLiveData()
  val refreshResult: LiveData<Result<Unit>> = mutableRefreshState

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  fun onResume() {
    refreshFriends()
  }

  private fun refreshFriends() {
    friendListRepository.refreshFriends()
        .toResult<Unit>(schedulerProvider)
        .subscribeBy(
            onNext = { mutableRefreshState.value = it }
        )
        .addTo(compositeDisposable)
  }

  override fun onCleared() {
    super.onCleared()
    compositeDisposable.clear()
  }
}
