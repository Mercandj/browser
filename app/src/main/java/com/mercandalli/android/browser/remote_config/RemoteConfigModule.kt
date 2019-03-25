@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.remote_config

import com.mercandalli.android.browser.main.ApplicationGraph
import java.util.Random

/**
 * A [Module] for the remote config .
 */
class RemoteConfigModule {

    fun createRemoteConfig(): RemoteConfig {
        val mainThreadPost = ApplicationGraph.getMainThreadPost()
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
