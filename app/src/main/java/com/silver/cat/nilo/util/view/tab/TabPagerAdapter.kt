package com.silver.cat.nilo.util.view.tab

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter


class TabPagerAdapter<T : TabSource>(fragmentManager: FragmentManager, val factory: TabFactory<T>)
  : FragmentStatePagerAdapter(fragmentManager) {

  override fun getItem(position: Int): Fragment? {
    return factory.available[position].fragmentInstance?.apply {
      factory.bundle?.let {
        arguments = it
      }
    }
  }

  override fun getCount(): Int = factory.available.size

  fun getItemPosition(tabSource: T): Int = factory.available.indexOf(tabSource)

  fun initCustomTabs(tabLayout: TabLayout) {
    for ((index, tabSource) in factory.available.withIndex()) {
      tabLayout.getTabAt(index)?.apply {
        when (tabSource) {
          is TabSource.TextTabSource -> setText(tabSource.title)
          is TabSource.ImageTabSource -> {
            if (tabSource.title != 0) {
              setText(tabSource.title)
            }
            setIcon(tabSource.icon)
          }
        }
      }
    }
  }


}

