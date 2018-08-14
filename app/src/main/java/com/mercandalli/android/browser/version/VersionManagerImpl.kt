package com.mercandalli.android.browser.version

import android.content.pm.PackageInfo
import android.support.annotation.RequiresApi

class VersionManagerImpl(
        private val delegate: Delegate
) : VersionManager {

    private lateinit var packageInfo: PackageInfo

    override fun getVersionName(): String {
        return getPackageInfo().versionName
    }

    override fun getVersionCode(): Int {
        return getPackageInfo().versionCode
    }

    @RequiresApi(28)
    override fun getLongVersionCode(): Long {
        return getPackageInfo().longVersionCode
    }

    override fun getFirstInstallTime(): Long {
        return getPackageInfo().firstInstallTime
    }

    private fun getPackageInfo(): PackageInfo {
        if (!::packageInfo.isInitialized) {
            packageInfo = delegate.getAppPackageInfo()
        }
        return packageInfo
    }

    interface Delegate {
        fun getAppPackageInfo(): PackageInfo
    }
}