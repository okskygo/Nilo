package com.silver.cat.nilo.view.friend

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.firestore.AccountFirestore
import com.silver.cat.nilo.util.Result
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject


class FriendViewModel @Inject constructor(private val accountFirestore: AccountFirestore) : ViewModel() {

  private val compositeDisposable: CompositeDisposable = CompositeDisposable()

  val friends: LiveData<Result<List<AccountDto>>> by lazy {
    accountFirestore.friends.
  }

  override fun onCleared() {
    super.onCleared()
    compositeDisposable.clear()
  }
}
