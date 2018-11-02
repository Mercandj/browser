package com.mercandalli.android.browser.version

import androidx.annotation.RequiresApi

interface VersionManager {

    fun getVersionName(): String

    fun getBuildConfigVersionName(): String

    fun getBuildConfigVersionCode(): Int

    fun getPackageManagerVersionName(): String

    @Deprecated("Use {@link #getLongVersionCode()} instead")
    fun getPackageManagerVersionCode(): Int

    @RequiresApi(28)
    fun getPackageManagerLongVersionCode(): Long

    fun getFirstInstallTime(): Long
}
