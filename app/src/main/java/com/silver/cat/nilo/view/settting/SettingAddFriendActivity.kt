package com.silver.cat.nilo.view.settting

import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.Toast
import com.silver.cat.nilo.R
import com.silver.cat.nilo.config.dagger.Injectable
import com.silver.cat.nilo.dto.AccountDto
import com.silver.cat.nilo.util.Result
import com.silver.cat.nilo.util.observe
import com.silver.cat.nilo.view.BaseActivity
import com.silver.cat.nilo.view.util.clickThrottleFirst
import com.trello.rxlifecycle2.android.ActivityEvent
import kotlinx.android.synthetic.main.activity_add_friend.*
import javax.inject.Inject


class SettingAddFriendActivity : BaseActivity(), Injectable {

  @Inject
  lateinit var viewModelFactory: ViewModelProvider.Factory

  private val viewModel: SettingAddFriendViewModel by lazy {
    ViewModelProviders.of(this, viewModelFactory).get(SettingAddFriendViewModel::class.java)
  }
  private var accountDto: AccountDto? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_friend)
    setupToolbar()
    searchView.onActionViewExpanded()
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
          findAccount(query)
        }
        return false
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        return false
      }
    })
    action.clickThrottleFirst()
        .compose(bindUntilEvent(ActivityEvent.DESTROY))
        .subscribe {
          accountDto?.let {
            viewModel.inviteFriend(it.uid).observe(this, { result ->
              when (result) {
                is Result.Success -> {
                  action.visibility = View.GONE
                  Toast.makeText(this@SettingAddFriendActivity,
                      R.string.invite_friends,
                      Toast.LENGTH_SHORT).show()
                }
                is Result.Failure -> {
                  Toast.makeText(this@SettingAddFriendActivity,
                      R.string.invite_friends_failure,
                      Toast.LENGTH_SHORT).show()
                }
              }
            })
          }
        }
  }

  private fun findAccount(nid: String) {
    viewModel.findAccount(nid).observe(this, { result ->
      when (result) {
        is Result.Success -> {
          val optional = result.data
          if (optional.isPresent) {
            populateAccount(optional.get())
          } else {
            populateNotFound()
          }
        }
      }
    })
  }

  private fun populateAccount(accountDto: AccountDto) {
    this.accountDto = accountDto
    avatar.visibility = View.VISIBLE
    action.visibility = View.GONE
    nickname.visibility = if (accountDto.nickname != null) View.VISIBLE else View.GONE
    notFound.visibility = View.GONE

    nickname.text = accountDto.nickname
    avatar.setImageUrl(accountDto.avatar)

    motto.visibility = View.VISIBLE
    when {
      viewModel.isMe(accountDto.uid) -> {
        motto.setText(R.string.can_you_find_yourself)
      }
      viewModel.isFriend(accountDto) -> {
        motto.setText(R.string.become_friends)
      }
      viewModel.isInvite(accountDto) -> {
        motto.setText(R.string.invite_friends)
      }
      else -> {
        motto.visibility = if (accountDto.motto != null) View.VISIBLE else View.GONE
        motto.text = accountDto.motto
        action.visibility = View.VISIBLE
      }
    }

  }

  private fun populateNotFound() {
    this.accountDto = null
    avatar.visibility = View.GONE
    action.visibility = View.GONE
    nickname.visibility = View.GONE
    motto.visibility = View.GONE
    notFound.visibility = View.VISIBLE
  }

  private fun setupToolbar() {
    setSupportActionBar(toolbar)
    val actionBar = supportActionBar ?: return
    actionBar.setHomeButtonEnabled(true)
    actionBar.setDisplayHomeAsUpEnabled(true)
    actionBar.setDisplayShowTitleEnabled(true)
    actionBar.title = getString(R.string.invite_friend)
    actionBar.displayOptions = ActionBar.DISPLAY_HOME_AS_UP or ActionBar.DISPLAY_SHOW_TITLE
    toolbar.setNavigationOnClickListener { onBackPressed() }
  }

}