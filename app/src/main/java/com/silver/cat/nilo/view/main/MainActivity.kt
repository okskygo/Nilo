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

        setupViewPager()
        setupBottomNavigationView()
    }

    private fun setupViewPager() {
        with(viewPager) {
            adapter = MainViewPagerAdapter(supportFragmentManager)
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

                var prevMenuItem: MenuItem? = null

                override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

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

    private fun setupBottomNavigationView(){
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {
                R.id.main_bottom_navigation_friend_list -> {
                    viewPager.setCurrentItem(MainViewFragmentEnum.FRIEND_LIST.ordinal, true)
                }
                R.id.main_bottom_navigation_chat -> {
                    viewPager.setCurrentItem(MainViewFragmentEnum.CHAT.ordinal, true)

                }
                R.id.main_bottom_navigation_setting -> {
                    viewPager.setCurrentItem(MainViewFragmentEnum.SETTING.ordinal, true)
                }
            }
            return@setOnNavigationItemSelectedListener true
        }
    }

}

enum class MainViewFragmentEnum {
    FRIEND_LIST, CHAT, SETTING;

    companion object {
        fun getFragment(enum: MainViewFragmentEnum): Fragment =
                when (enum) {
                    FRIEND_LIST -> FriendListFragment()
                    CHAT -> ChatFragment()
                    SETTING -> SettingFragment()
                }
    }


}

class MainViewPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {
    override fun getCount(): Int {
        return MainViewFragmentEnum.values().size
    }

    override fun getItem(position: Int): Fragment {
        return MainViewFragmentEnum.getFragment(MainViewFragmentEnum.values()[position])
    }

}
