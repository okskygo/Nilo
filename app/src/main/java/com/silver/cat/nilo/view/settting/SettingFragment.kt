package com.silver.cat.nilo.view.settting

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.silver.cat.nilo.R
import com.silver.cat.nilo.config.dagger.Injectable
import com.silver.cat.nilo.util.Result
import com.silver.cat.nilo.util.observe
import com.silver.cat.nilo.view.BaseFragment
import com.silver.cat.nilo.view.util.clickThrottleFirst
import com.trello.rxlifecycle2.android.FragmentEvent
import kotlinx.android.synthetic.main.fragment_setting.*
import javax.inject.Inject

/**
 * Created by xiezhenyu on 2017/5/11.
 */

class SettingFragment : BaseFragment(), Injectable {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val settingViewModel: SettingViewModel by lazy {
    ViewModelProviders.of(this, viewModelFactory).get(SettingViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_setting, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    lifecycle.addObserver(settingViewModel)

    addFriend.clickThrottleFirst()
        .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe {
          startActivity(Intent(context, SettingAddFriendActivity::class.java))
        }

    profile.clickThrottleFirst()
        .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe {
          startActivity(Intent(context, SettingProfileActivity::class.java))
        }

    settingViewModel.me.observe(this, { result ->
      when (result) {
        is Result.Success -> {
          val accountDto = result.data
          avatar.setImageUrl(accountDto.avatar)
          nickname.text = accountDto.nickname
        }
      }
    })
  }

}

