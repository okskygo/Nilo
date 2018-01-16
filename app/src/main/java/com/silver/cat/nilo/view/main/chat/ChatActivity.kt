package com.silver.cat.nilo.view.main.chat

import android.os.Bundle
import android.support.v7.app.ActionBar
import com.silver.cat.nilo.R
import com.silver.cat.nilo.view.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Rex on 2018/1/16.
 */

class ChatActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat)
    setupToolbar()
  }

  private fun setupToolbar() {
    setSupportActionBar(toolbar)
    val actionBar = supportActionBar ?: return
    actionBar.setHomeButtonEnabled(true)
    actionBar.setDisplayHomeAsUpEnabled(true)
    actionBar.setDisplayShowTitleEnabled(true)
    //TODO#chat
    actionBar.title = "聊天室"
    actionBar.displayOptions = ActionBar.DISPLAY_HOME_AS_UP or ActionBar.DISPLAY_SHOW_TITLE
    toolbar.setNavigationOnClickListener { onBackPressed() }
  }

}