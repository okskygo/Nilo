package com.silver.cat.nilo.view.main.chat

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.silver.cat.nilo.R

/**
 * Created by Rex on 2018/1/16.
 */

class ChatInputBar @JvmOverloads constructor(context: Context,
                                             attrs: AttributeSet? = null,
                                             defStyleAttr: Int = 0)
  : RelativeLayout(context, attrs, defStyleAttr) {

  init {
    LayoutInflater.from(context).inflate(R.layout.layout_chat_bar, this)
  }

}