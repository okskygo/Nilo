package com.silver.cat.nilo.config.dagger.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.silver.cat.nilo.config.dagger.ViewModelFactory
import com.silver.cat.nilo.config.dagger.ViewModelKey
import com.silver.cat.nilo.view.friend.FriendViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
interface ViewModelModule {

  @Binds
  @IntoMap
  @ViewModelKey(FriendViewModel::class)
  fun bindFriendListViewModel(friendViewModel: FriendViewModel): ViewModel


  @Binds
  fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}