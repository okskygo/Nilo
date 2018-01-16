package com.silver.cat.nilo.view.main.chat

/**
 * Created by Rex on 2018/1/16.
 */

sealed class MessageDto(open val senderId: String, open val content: String)

//TODO#chat remove default value
class SelfTextMessageDto(override val senderId: String = "1", override val content: String)
  : MessageDto(senderId, content)

class OtherTextMessageDto(override val senderId: String = "2", override val content: String)
  : MessageDto(senderId, content)