package com.amsal.githubuser.adapter

import android.os.Bundle
import android.util.Log
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.MutableLiveData
import com.amsal.githubuser.FollowersFragment
import com.amsal.githubuser.FollowingFragment
import com.amsal.githubuser.R
import com.amsal.githubuser.model.User

class SectionsPagerAdapter(
    private val mContext: FragmentActivity?,
    fm: FragmentManager,
    val urlFollowers: String,
    val urlFollowing: String
) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.followers, R.string.following)

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> {
                fragment = FollowersFragment()
                var mBundle = Bundle()
                mBundle.putString(FollowersFragment.EXTRA_USER, urlFollowers)
                fragment.arguments = mBundle
            }
            1 -> {
                fragment = FollowingFragment()
                var mBundle = Bundle()
                mBundle.putString(FollowingFragment.EXTRA_USER, urlFollowing)
                fragment.arguments = mBundle
            }
        }
        return fragment as Fragment
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext?.resources?.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        return 2
    }

}