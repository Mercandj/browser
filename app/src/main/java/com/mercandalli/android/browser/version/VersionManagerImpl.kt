package com.mercandalli.android.browser.version

import android.content.pm.PackageInfo
import android.support.annotation.RequiresApi
import com.mercandalli.android.browser.BuildConfig

class VersionManagerImpl(
        private val delegate: Delegate
) : VersionManager {

    private lateinit var packageInfo: PackageInfo

    override fun getBuildConfigVersionName(): String {
        return BuildConfig.VERSION_NAME
    }

    override fun getBuildConfigVersionCode(): Int {
        return BuildConfig.VERSION_CODE
    }

    override fun getPackageManagerVersionName(): String {
        return getPackageInfo().versionName
    }

    override fun getPackageManagerVersionCode(): Int {
        return getPackageInfo().versionCode
    }

    @RequiresApi(28)
    override fun getPackageManagerLongVersionCode(): Long {
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