package com.silver.cat.nilo.dto


data class AccountDto(var uid: String = "",
                      var fcmToken: String = "",
                      var nid: String? = null,
                      var nickname: String? = "無名氏",
                      var motto: String? = null,
                      val creator: Boolean = false,
                      val avatar: String? = null,
                      var friends: Map<String, Boolean> = emptyMap(),
                      var inviteFriends: Map<String, Boolean> = emptyMap()
)