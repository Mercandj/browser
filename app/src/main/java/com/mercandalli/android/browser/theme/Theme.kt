package com.mercandalli.android.browser.theme

import androidx.annotation.ColorRes

open class Theme(
        @ColorRes
        val windowBackgroundColorRes: Int,
        @ColorRes
        val windowSettingsBackgroundColorRes: Int,
        @ColorRes
        val statusBarBackgroundColorRes: Int,
        @ColorRes
        val toolbarBackgroundColorRes: Int,
        @ColorRes
        val textPrimaryColorRes: Int,
        @ColorRes
        val textSecondaryColorRes: Int,
        @ColorRes
        val textDarkColorRes: Int,
        @ColorRes
        val cardBackgroundColorRes: Int
)