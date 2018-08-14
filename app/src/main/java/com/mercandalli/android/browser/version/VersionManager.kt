package com.mercandalli.android.browser.version

import android.support.annotation.RequiresApi

interface VersionManager {

    fun getVersionName(): String

    @Deprecated("Use {@link #getLongVersionCode()} instead")
    fun getVersionCode(): Int

    @RequiresApi(28)
    fun getLongVersionCode(): Long

    fun getFirstInstallTime(): Long
}