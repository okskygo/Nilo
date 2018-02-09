package com.silver.cat.nilo.view.util.input

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.text.InputFilter
import com.silver.cat.nilo.R
import com.silver.cat.nilo.util.Optional
import com.silver.cat.nilo.view.BaseActivity
import com.silver.cat.nilo.view.util.clickThrottleFirst
import com.trello.rxlifecycle2.android.ActivityEvent
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_input.*
import rx_activity_result2.RxActivityResult


class InputActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_input)
    setupToolbar()

    val hint = intent.getStringExtra("hint")
    val limit = intent.getIntExtra("limit", 30)
    val default = intent.getStringExtra("default")

    input.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(limit))
    input.setText(default)
    input.hint = hint
    save.clickThrottleFirst()
        .compose(bindUntilEvent(ActivityEvent.DESTROY))
        .subscribe {
          save()
        }
  }

  private fun save() {
    val result = input.text?.toString()
    val intent = Intent()
    intent.putExtra("result", result)
    setResult(Activity.RESULT_OK, intent)
    finish()
  }

  private fun setupToolbar() {
    setSupportActionBar(toolbar)
    val actionBar = supportActionBar ?: return
    actionBar.setHomeButtonEnabled(true)
    actionBar.setDisplayHomeAsUpEnabled(true)
    actionBar.setDisplayShowTitleEnabled(true)
    actionBar.title = intent.getStringExtra("title")
    actionBar.displayOptions = ActionBar.DISPLAY_HOME_AS_UP or ActionBar.DISPLAY_SHOW_TITLE
    toolbar.setNavigationOnClickListener { onBackPressed() }
  }


  class Builder(private val limit: Int = 30,
                private val hint: String = "",
                private val title: String = "",
                private val default: String? = "") {


    fun startActivityForResult(activity: Activity): Observable<Optional<String>> {
      val intent = Intent(activity, InputActivity::class.java)
      intent.putExtra("limit", limit)
      intent.putExtra("hint", hint)
      intent.putExtra("title", title)
      intent.putExtra("default", default)
      return RxActivityResult
          .on(activity)
          .startIntent(intent)
          .map {
            val data = it.data()
            if (it.resultCode() == Activity.RESULT_OK && data != null) {
              if (data.extras != null && data.extras.getString("result") != null) {
                Optional.of(data.extras.getString("result"))
              } else {
                Optional.empty()
              }
            } else {
              Optional.empty()
            }
          }
    }

  }
}