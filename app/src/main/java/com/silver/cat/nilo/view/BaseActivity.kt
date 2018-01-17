package com.silver.cat.nilo.view

import android.os.Bundle
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity

/**
 * Created by xiezhenyu on 2017/5/11.
 */

abstract class BaseActivity : RxAppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onResume() {
    super.onResume()
  }

}
