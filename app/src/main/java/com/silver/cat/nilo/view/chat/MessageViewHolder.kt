package com.silver.cat.nilo.view.chat

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.silver.cat.nilo.R
import com.silver.cat.nilo.view.util.inflate
import kotlinx.android.synthetic.main.holder_other_text_message.view.*
import kotlinx.android.synthetic.main.holder_self_text_message.view.*

/**
 * Created by Rex on 2018/1/16.
 */

sealed class MessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  abstract fun bind(messageDto: MessageDto)
}

class OtherTextMessageViewHolder(parent: ViewGroup)
  : MessageViewHolder(parent.inflate(R.layout.holder_other_text_message)) {
  override fun bind(messageDto: MessageDto) {
    itemView.otherText.text = messageDto.content
  }
}

class SelfTextMessageViewHolder(parent: ViewGroup)
  : MessageViewHolder(parent.inflate(R.layout.holder_self_text_message)) {
  override fun bind(messageDto: MessageDto) {
    itemView.selfText.text = messageDto.content
  }
}