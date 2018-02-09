package com.silver.cat.nilo.dto


data class AccountDto(var uid: String = "",
                      var fcmToken: String = "",
                      var nid: String? = null,
                      var nickname: String? = null,
                      var motto: String? = null,
                      val creator: Boolean = false,
                      var friends: Map<String, Boolean> = emptyMap(),
                      var inviteFriends: Map<String, Boolean> = emptyMap()
)