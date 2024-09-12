package com.shadypines.radio.view.tab_adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.shadypines.radio.view.fragment.ChatFragment
import com.shadypines.radio.view.fragment.HomeFragment
import com.shadypines.radio.view.fragment.ScheduleFragment


class MyPagerAdapter(fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val COUNT = 3

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> HomeFragment()  // New instance
            1 -> ScheduleFragment()  // New instance
            2 -> ChatFragment()  // New instance
            else -> HomeFragment()  // New instance
        }
    }

    override fun getCount(): Int {
        return COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Tab " + (position + 1)
    }
}
