package com.silver.cat.nilo.view.chat

import android.content.Context
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.LinearLayoutManager
import android.view.inputmethod.InputMethodManager
import com.jakewharton.rxbinding2.view.clicks
import com.silver.cat.nilo.R
import com.silver.cat.nilo.view.BaseActivity
import com.trello.rxlifecycle2.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.layout_chat_bar.*

/**
 * Created by Rex on 2018/1/16.
 */

class ChatActivity : BaseActivity() {

  private val adapter = ChatAdapter()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_chat)
    setupToolbar()
    setupRecyclerView()
    setupInputBar()
    testChat()
  }

  private fun setupRecyclerView() {
    recyclerView.layoutManager = LinearLayoutManager(this).apply { reverseLayout = true }
    recyclerView.adapter = adapter
  }

  private fun setupToolbar() {
    setSupportActionBar(toolbar)
    val actionBar = supportActionBar ?: return
    actionBar.setHomeButtonEnabled(true)
    actionBar.setDisplayHomeAsUpEnabled(true)
    actionBar.setDisplayShowTitleEnabled(true)
    //TODO#chat use real chat name
    actionBar.title = "聊天室"
    actionBar.displayOptions = ActionBar.DISPLAY_HOME_AS_UP or ActionBar.DISPLAY_SHOW_TITLE
    toolbar.setNavigationOnClickListener { onBackPressed() }
  }

  private fun setupInputBar() {
    send.clicks()
        .compose(bindUntilEvent(ActivityEvent.DESTROY))
        .subscribe {
          val text = editText.text
          if (text.isNullOrEmpty()) {
            return@subscribe
          }
          editText.setText("")
          adapter.addMessage(SelfTextMessageDto(content = text.toString()))
          adapter.notifyItemInserted(0)
          recyclerView.smoothScrollToPosition(0)
        }
  }

  private fun hideKeyBoard() {
    val inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputManager.hideSoftInputFromWindow(currentFocus.windowToken,
        InputMethodManager.HIDE_NOT_ALWAYS)
  }

  private fun testChat() {
    adapter.addMessages(getTestChatMessages())
    adapter.notifyDataSetChanged()
  }

  private fun getTestChatMessages(): List<MessageDto> {
    return arrayListOf(SelfTextMessageDto(content = "hello"),
        OtherTextMessageDto(content = "hello"),
        SelfTextMessageDto(content = "hello2"),
        SelfTextMessageDto(content = "hello3"),
        SelfTextMessageDto(content = "hello \n awrkwoarkowakroaw"),
        OtherTextMessageDto(content = "asdasdasdjsaidjiasd")
    )
  }

}