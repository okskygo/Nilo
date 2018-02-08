package com.silver.cat.nilo.config.dagger.module

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.silver.cat.nilo.config.dagger.ViewModelFactory
import com.silver.cat.nilo.config.dagger.ViewModelKey
import com.silver.cat.nilo.view.friend.FriendListViewModel
import com.silver.cat.nilo.view.settting.SettingAddFriendViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap


@Module
interface ViewModelModule {

  @Binds
  @IntoMap
  @ViewModelKey(FriendListViewModel::class)
  fun bindFriendListViewModel(friendListViewModel: FriendListViewModel): ViewModel

  @Binds
  @IntoMap
  @ViewModelKey(SettingAddFriendViewModel::class)
  fun bindSettingAddFriendViewModel(settingAddFriendViewModel: SettingAddFriendViewModel): ViewModel


  @Binds
  fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}