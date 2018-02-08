package com.silver.cat.nilo.view.settting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.silver.cat.nilo.R
import com.silver.cat.nilo.view.BaseFragment
import com.silver.cat.nilo.view.util.clickThrottleFirst
import com.trello.rxlifecycle2.android.FragmentEvent
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * Created by xiezhenyu on 2017/5/11.
 */

class SettingFragment : BaseFragment() {

  override fun onCreateView(inflater: LayoutInflater,
                            container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {
    return inflater.inflate(R.layout.fragment_setting, container, false)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    addFriend.clickThrottleFirst()
        .compose(bindUntilEvent(FragmentEvent.DESTROY_VIEW))
        .subscribe {
          startActivity(Intent(context, SettingAddFriendActivity::class.java))
        }
  }

}

