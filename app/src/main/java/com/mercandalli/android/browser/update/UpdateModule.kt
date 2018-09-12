package com.mercandalli.android.browser.update

import android.content.Context
import com.mercandalli.android.browser.version.VersionManager

class UpdateModule {

    fun createUpdateManager(
            context: Context,
            versionManager: VersionManager
    ): UpdateManager {
        val sharedPreferences = context.getSharedPreferences(
                UpdateManagerImpl.PREFERENCE_NAME, Context.MODE_PRIVATE)
        return UpdateManagerImpl(
                sharedPreferences,
                versionManager
        )
    }
}