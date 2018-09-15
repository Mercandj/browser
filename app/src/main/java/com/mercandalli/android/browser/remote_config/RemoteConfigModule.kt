package com.mercandalli.android.browser.remote_config

import com.mercandalli.android.browser.main.ApplicationGraph
import com.mercandalli.android.browser.thread.MainThreadPost
import java.util.*

/**
 * A [Module] for the remote config .
 */
class RemoteConfigModule {

    fun createRemoteConfig(mainThreadPost: MainThreadPost): RemoteConfig {
        val updateManager = ApplicationGraph.getUpdateManager()
        val random = Random()
        return RemoteConfigImpl(
                updateManager,
                mainThreadPost,
                random.nextFloat(),
                random.nextFloat()
        )
    }
}