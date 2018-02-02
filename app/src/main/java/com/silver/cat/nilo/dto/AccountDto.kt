package com.silver.cat.nilo.dto


data class AccountDto(var uid: String = "",
                      var fcmToken: String = "",
                      var nid: String? = null,
                      var nickname: String? = null,
                      val creator: Boolean = false,
                      var friends: Map<String, Boolean> = emptyMap()
)