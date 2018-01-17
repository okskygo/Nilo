package com.silver.cat.nilo.view.main

import android.os.Bundle
import com.silver.cat.nilo.NiloApplication
import com.silver.cat.nilo.R
import com.silver.cat.nilo.util.view.tab.TabFactory
import com.silver.cat.nilo.view.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

  private val tabSources = arrayListOf(MainFriendTabSource(),
      MainChatTabSource(),
      MainSettingTabSource())

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    NiloApplication.component.inject(this)
    setContentView(R.layout.activity_main)
    setupToolbar()
    setupTab()
  }

  private fun setupTab() {
    easyTabLayout.setupWithViewPager(viewPager,
        supportFragmentManager,
        TabFactory.Builder().create(tabSources))
  }

  private fun setupToolbar() {
    setSupportActionBar(toolbar)
    val actionBar = supportActionBar ?: return
    actionBar.setTitle(R.string.app_name)
  }

}
