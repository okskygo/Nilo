package com.silver.cat.nilo.config.dagger.module

import com.silver.cat.nilo.view.chat.ChatActivity
import com.silver.cat.nilo.view.main.MainActivity
import com.silver.cat.nilo.view.settting.SettingAddFriendActivity
import com.silver.cat.nilo.view.settting.SettingProfileActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBuilderModule {

  @ContributesAndroidInjector
  fun contributeMainActivity(): MainActivity

  @ContributesAndroidInjector
  fun contributeChatActivity(): ChatActivity

  @ContributesAndroidInjector
  fun contributeSettingAddFriendActivity(): SettingAddFriendActivity

  @ContributesAndroidInjector
  fun contributeSettingProfileActivity(): SettingProfileActivity

}

