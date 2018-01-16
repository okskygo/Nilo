package com.silver.cat.nilo.view.main.chat

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.silver.cat.nilo.view.main.chat.holder.MessageViewHolder
import com.silver.cat.nilo.view.main.chat.holder.OtherTextMessageViewHolder
import com.silver.cat.nilo.view.main.chat.holder.SelfTextMessageViewHolder

/**
 * Created by Rex on 2018/1/16.
 */

class ChatAdapter : RecyclerView.Adapter<MessageViewHolder>() {

  private val messages: MutableList<MessageDto> = mutableListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    return when (viewType) {
      SELF_MESSAGE -> SelfTextMessageViewHolder(parent)
      OTHER_MESSAGE -> OtherTextMessageViewHolder(parent)
      else -> throw IllegalArgumentException()
    }
  }

  override fun onBindViewHolder(holder: MessageViewHolder?, position: Int) {
    holder?.bind(messages[position])
  }

  override fun getItemCount(): Int = messages.count()

  override fun getItemViewType(position: Int): Int {
    return when (messages[position]) {
      is SelfTextMessageDto -> SELF_MESSAGE
      is OtherTextMessageDto -> OTHER_MESSAGE
    }
  }

  fun addMessages(messages: List<MessageDto>) {
    this.messages.addAll(messages)
  }

  fun addMessage(message: MessageDto) {
    this.messages.add(message)
  }

  companion object {
    private val SELF_MESSAGE = 1
    private val OTHER_MESSAGE = 2
  }

}