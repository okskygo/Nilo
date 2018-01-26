package com.silver.cat.nilo.dto


data class AccountDto(val uid: String,
                      val fcmToken: String,
                      val nid: String? = null,
                      val nickname: String? = null)