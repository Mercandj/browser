package com.mercandalli.android.browser.version

import android.support.annotation.RequiresApi

interface VersionManager {

    fun getBuildConfigVersionName(): String

    fun getBuildConfigVersionCode(): Int

    fun getPackageManagerVersionName(): String

    @Deprecated("Use {@link #getLongVersionCode()} instead")
    fun getPackageManagerVersionCode(): Int

    @RequiresApi(28)
    fun getPackageManagerLongVersionCode(): Long

    fun getFirstInstallTime(): Long
}