package com.silver.cat.nilo.view.settting

import android.os.Bundle
import android.support.v7.app.ActionBar
import android.support.v7.widget.SearchView
import com.silver.cat.nilo.R
import com.silver.cat.nilo.config.dagger.Injectable
import com.silver.cat.nilo.view.BaseActivity
import com.silver.cat.nilo.view.friend.FriendViewModel
import kotlinx.android.synthetic.main.activity_add_friend.*
import javax.inject.Inject


class AddFriendActivity : BaseActivity(), Injectable {

  @Inject
  lateinit var friendViewModel: FriendViewModel

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_add_friend)
    setupToolbar()
    searchView.onActionViewExpanded()
    searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
      override fun onQueryTextSubmit(query: String?): Boolean {
        //TODO
        return true
      }

      override fun onQueryTextChange(newText: String?): Boolean {
        //TODO
        return true
      }
    })
  }

  private fun setupToolbar() {
    setSupportActionBar(toolbar)
    val actionBar = supportActionBar ?: return
    actionBar.setHomeButtonEnabled(true)
    actionBar.setDisplayHomeAsUpEnabled(true)
    actionBar.setDisplayShowTitleEnabled(true)
    actionBar.title = getString(R.string.add_friend)
    actionBar.displayOptions = ActionBar.DISPLAY_HOME_AS_UP or ActionBar.DISPLAY_SHOW_TITLE
    toolbar.setNavigationOnClickListener { onBackPressed() }
  }

}