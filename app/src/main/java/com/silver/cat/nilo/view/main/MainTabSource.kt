package com.silver.cat.nilo.view.main

import com.silver.cat.nilo.R
import com.silver.cat.nilo.util.view.tab.TabSource
import com.silver.cat.nilo.view.friend.FriendListFragment
import com.silver.cat.nilo.view.chat.ChatListFragment
import com.silver.cat.nilo.view.settting.SettingFragment

/**
 * Created by Rex on 2018/1/16.
 */

sealed class MainTabSource : TabSource.ImageTabSource()

class MainFriendTabSource : MainTabSource() {
    override val icon = R.drawable.ic_person_pin_white_24dp
    override val title = 0
    override val fragmentClass = FriendListFragment::class.java
}

class MainChatTabSource : MainTabSource() {
    override val icon = R.drawable.ic_chat_white_24dp
    override val title = 0
    override val fragmentClass = ChatListFragment::class.java
}

class MainSettingTabSource : MainTabSource() {
    override val icon = R.drawable.ic_settings_white_24dp
    override val title = 0
    override val fragmentClass = SettingFragment::class.java
}
