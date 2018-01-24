package com.silver.cat.nilo.config.dagger.module

import com.silver.cat.nilo.view.chat.ChatListFragment
import com.silver.cat.nilo.view.friend.FriendListFragment
import com.silver.cat.nilo.view.settting.SettingFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
interface FragmentBuilderModule {

  @ContributesAndroidInjector
  fun contributeFriendListFragment(): FriendListFragment

  @ContributesAndroidInjector
  fun contributeChatListFragment(): ChatListFragment

  @ContributesAndroidInjector
  fun contributeSessionsFragment(): SettingFragment

}