package com.silver.cat.nilo.dto


data class AccountDto(var uid: String = "",
                      var fcmToken: String = "",
                      var nid: String? = null,
                      var nickname: String? = null,
                      var friends: List<AccountDto> = emptyList()
)