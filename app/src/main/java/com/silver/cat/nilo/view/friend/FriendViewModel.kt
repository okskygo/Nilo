package com.silver.cat.nilo.view.friend

import android.arch.lifecycle.*
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.firestore.AccountFirestore
import com.silver.cat.nilo.util.Result
import com.silver.cat.nilo.util.SchedulerProvider
import com.silver.cat.nilo.util.toLiveData
import com.silver.cat.nilo.util.toResult
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject


class FriendViewModel @Inject constructor(private val accountFirestore: AccountFirestore,
                                          private val schedulerProvider: SchedulerProvider)
  : ViewModel(), LifecycleObserver {

  private val compositeDisposable: CompositeDisposable = CompositeDisposable()

  val friends: LiveData<Result<List<AccountDto>>> by lazy {
    accountFirestore.friends()
        .toResult(schedulerProvider)
        .toLiveData()
  }

  fun addFriend(friendUid: String): LiveData<Result<Boolean>> {
    return accountFirestore.addFriend(friendUid)
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
    accountFirestore.refreshFriends()
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
