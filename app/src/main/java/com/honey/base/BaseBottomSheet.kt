package com.honey.base

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.honey.utils.SharedPreferenceUtil

abstract class BaseBottomSheet : BottomSheetDialogFragment() {
    val prefs: SharedPreferenceUtil by lazy { SharedPreferenceUtil.getInstance(requireActivity()) }
    abstract fun init()
    abstract fun initControl()
}