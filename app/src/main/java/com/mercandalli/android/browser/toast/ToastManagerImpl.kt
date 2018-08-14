package com.mercandalli.android.browser.toast

import android.content.Context
import android.widget.Toast
import com.mercandalli.android.browser.thread.MainThreadPost

class ToastManagerImpl(
        private val context: Context,
        private val mainThreadPost: MainThreadPost
) : ToastManager {

    override fun toast(message: Int) {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable {
                toast(message)
            })
            return
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun toast(message: String) {
        if (!mainThreadPost.isOnMainThread) {
            mainThreadPost.post(Runnable {
                toast(message)
            })
            return
        }
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}