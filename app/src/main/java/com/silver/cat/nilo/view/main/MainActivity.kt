package com.silver.cat.nilo.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import com.silver.cat.nilo.R
import com.silver.cat.nilo.view.BaseActivity
import com.silver.cat.nilo.view.widget.tab.TabFactory
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : BaseActivity(), HasSupportFragmentInjector {

  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

  private val tabSources = arrayListOf(MainFriendTabSource(),
      MainChatTabSource(),
      MainSettingTabSource())

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
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

  override fun supportFragmentInjector() = dispatchingAndroidInjector

}
