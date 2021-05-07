package com.billyindrai.aplikasigithubuser


import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class PagerAdapter(activity: AppCompatActivity, val user: User) : FragmentStateAdapter(activity) {

    override fun createFragment(position: Int): Fragment {
        return ViewTabFragment.newInstance(position + 1, user)
    }

    override fun getItemCount(): Int {
        return 2
    }

}