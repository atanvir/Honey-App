package com.frzah.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.frzah.R

class SwitchFragment {
    companion object {
        fun changeFragment(fragmentManager: FragmentManager, fragment: Fragment, saveInBackstack: Boolean, animate: Boolean) {
            val backStateName = fragment.javaClass.name
            try {
                val transaction = fragmentManager.beginTransaction()
                if (animate) {
                    transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
                }
                transaction.replace(R.id.frameLayout, fragment, backStateName)
                if (saveInBackstack) {
                    transaction.addToBackStack(backStateName)
                }
                transaction.commitAllowingStateLoss()
            } catch (e: IllegalStateException) {
            }
        }
    }

}