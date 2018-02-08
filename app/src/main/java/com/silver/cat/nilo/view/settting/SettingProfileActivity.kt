package com.silver.cat.nilo.view.settting

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import com.silver.cat.nilo.R
import com.silver.cat.nilo.view.BaseActivity
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_chat.*
import javax.inject.Inject


class SettingProfileActivity : BaseActivity(), HasSupportFragmentInjector {

  @Inject
  lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_setting_profile)
    setupToolbar()
    if (savedInstanceState == null) {
      supportFragmentManager.beginTransaction()
          .replace(R.id.container,
              SettingProfileFragment::class.java.newInstance(),
              "settingProfileFragment")
          .commit()
    }
  }

  private fun setupToolbar() {
    setSupportActionBar(toolbar)
    val actionBar = supportActionBar ?: return
    actionBar.setHomeButtonEnabled(true)
    actionBar.setDisplayHomeAsUpEnabled(true)
    actionBar.setDisplayShowTitleEnabled(true)
    actionBar.title = getString(R.string.profile_detail)
    actionBar.displayOptions = ActionBar.DISPLAY_HOME_AS_UP or ActionBar.DISPLAY_SHOW_TITLE
    toolbar.setNavigationOnClickListener { onBackPressed() }
  }

  override fun supportFragmentInjector() = dispatchingAndroidInjector
}