package com.silver.cat.nilo.view.friend

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.firestore.AccountFirestore
import com.silver.cat.nilo.util.Result
import com.silver.cat.nilo.util.SchedulerProvider
import com.silver.cat.nilo.util.toLiveData
import com.silver.cat.nilo.util.toResult
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class FriendListViewModel @Inject constructor(private val accountFirestore: AccountFirestore,
                                              private val schedulerProvider: SchedulerProvider) : ViewModel() {

  private val compositeDisposable: CompositeDisposable = CompositeDisposable()

//  val friends: LiveData<Result<List<AccountDto>>> by lazy {
//    accountFirestore.friends.
//  }

  val creators: LiveData<Result<List<AccountDto>>> by lazy {
    accountFirestore.getCreators().toResult(schedulerProvider).toLiveData()
  }

  override fun onCleared() {
    super.onCleared()
    compositeDisposable.clear()
  }
}
