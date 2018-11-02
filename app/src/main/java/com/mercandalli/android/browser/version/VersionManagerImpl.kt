package com.mercandalli.android.browser.version

import android.content.pm.PackageInfo
import androidx.annotation.RequiresApi
import com.mercandalli.android.browser.BuildConfig

class VersionManagerImpl(
    private val delegate: Delegate
) : VersionManager {

    private lateinit var packageInfo: PackageInfo

    override fun getVersionName() = getBuildConfigVersionName()

    override fun getBuildConfigVersionName() = BuildConfig.VERSION_NAME

    override fun getBuildConfigVersionCode() = BuildConfig.VERSION_CODE

    override fun getPackageManagerVersionName() = getPackageInfo().versionName!!

    override fun getPackageManagerVersionCode() = getPackageInfo().versionCode

    @RequiresApi(28)
    override fun getPackageManagerLongVersionCode() = getPackageInfo().longVersionCode

    override fun getFirstInstallTime() = getPackageInfo().firstInstallTime

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
