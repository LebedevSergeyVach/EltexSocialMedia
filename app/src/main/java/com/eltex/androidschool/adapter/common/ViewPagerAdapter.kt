package com.eltex.androidschool.adapter.common

import androidx.fragment.app.Fragment

import androidx.viewpager2.adapter.FragmentStateAdapter

import com.eltex.androidschool.fragments.events.EventsFragment
import com.eltex.androidschool.fragments.posts.PostsFragment
import com.eltex.androidschool.fragments.users.UserFragment

class ViewPagerAdapter(
    fragment: Fragment
) : FragmentStateAdapter(fragment) {

    private val fragments = listOf(
        PostsFragment(),
        EventsFragment(),
        UserFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}
