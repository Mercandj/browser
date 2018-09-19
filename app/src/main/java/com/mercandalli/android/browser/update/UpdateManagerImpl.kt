package com.mercandalli.android.browser.update

import android.content.SharedPreferences
import com.mercandalli.android.browser.version.VersionManager
import kotlinx.coroutines.experimental.Dispatchers
import kotlinx.coroutines.experimental.GlobalScope
import kotlinx.coroutines.experimental.launch

class UpdateManagerImpl(
        private val sharedPreferences: SharedPreferences,
        private val versionManager: VersionManager
) : UpdateManager {

    private val lastVersionName: String by lazy { sharedPreferences.getString(KEY_LAST_VERSION_NAME, "1.00.00") }
    private var firstLaunchAfterUpdate: Boolean? = null

    override fun isFirstLaunchAfterUpdate(): Boolean {
        if (firstLaunchAfterUpdate == null) {
            val versionName = versionManager.getVersionName()
            firstLaunchAfterUpdate = versionName != lastVersionName
            GlobalScope.launch(Dispatchers.Default) {
                sharedPreferences.edit().putString(KEY_LAST_VERSION_NAME, versionName).apply()
            }
        }
        return firstLaunchAfterUpdate!!
    }

    companion object {
        @JvmStatic
        val PREFERENCE_NAME = "UpdateManager"
        private const val KEY_LAST_VERSION_NAME = "UpdateManager.KEY_LAST_VERSION_NAME"
    }
}