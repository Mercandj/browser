package com.mercandalli.android.browser.main_thread

interface MainThreadPost {

    /**
     * Is the call on main [Thread].
     *
     * @return `true` is call on main [Thread].
     */
    val isOnMainThread: Boolean

    /**
     * Post on main [Thread].
     *
     * @param runnable [Runnable.run] on main [Thread].
     */
    fun post(runnable: Runnable)
}
