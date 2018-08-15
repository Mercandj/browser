package com.mercandalli.android.browser.version

import android.content.Context
import android.content.pm.PackageInfo

class VersionModule {

    fun createVersionManager(context: Context): VersionManager {
        val delegate = object : VersionManagerImpl.Delegate {
            override fun getAppPackageInfo(): PackageInfo {
                return context.packageManager.getPackageInfo(context.packageName, 0)
            }
        }
        return VersionManagerImpl(delegate)
    }
}