package com.silver.cat.nilo.view.main

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.MenuItem
import com.silver.cat.nilo.R
import com.silver.cat.nilo.view.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    setupToolbar()
    setupViewPager()
    setupBottomNavigationView()
  }

  private fun setupToolbar() {
    setSupportActionBar(toolbar)
    val actionBar = supportActionBar ?: return
    actionBar.setTitle(R.string.app_name)
  }

  private fun setupViewPager() {
    with(viewPager) {
      adapter = MainViewPagerAdapter(supportFragmentManager)
      addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

        var prevMenuItem: MenuItem? = null

        override fun onPageScrolled(position: Int,
                                    positionOffset: Float,
                                    positionOffsetPixels: Int) {

        }

        override fun onPageSelected(position: Int) {
          if (prevMenuItem != null) {
            prevMenuItem?.isChecked = false
          } else {
            bottomNavigationView.menu.getItem(0).isChecked = false
          }

          bottomNavigationView.menu.getItem(position).isChecked = true
          prevMenuItem = bottomNavigationView.menu.getItem(position)
        }

        override fun onPageScrollStateChanged(state: Int) {

        }
      })
    }
  }

  private fun setupBottomNavigationView() {
    bottomNavigationView.setOnNavigationItemSelectedListener { item ->
      when (item.itemId) {
        R.id.main_bottom_navigation_friend_list -> {
          viewPager.setCurrentItem(MainTab.FRIEND_LIST.ordinal, true)
        }
        R.id.main_bottom_navigation_chat -> {
          viewPager.setCurrentItem(MainTab.CHAT.ordinal, true)

        }
        R.id.main_bottom_navigation_setting -> {
          viewPager.setCurrentItem(MainTab.SETTING.ordinal, true)
        }
      }
      true
    }
  }

}

enum class MainTab(val fragmentClazz: Class<out Fragment>) {
  FRIEND_LIST(FriendListFragment::class.java), CHAT(ChatFragment::class.java), SETTING(
      SettingFragment::class.java);

  companion object {
    fun getAvailable(): List<MainTab> {
      return arrayListOf(FRIEND_LIST, CHAT, SETTING)
    }
  }


}

class MainViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
  override fun getCount(): Int {
    return MainTab.getAvailable().size
  }

  override fun getItem(position: Int): Fragment {
    return MainTab.getAvailable()[position].fragmentClazz.newInstance()
  }

}
