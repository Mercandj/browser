package com.mercandalli.android.wear.browser.main

import android.os.Bundle
import android.support.wearable.activity.WearableActivity
import android.view.View
import android.widget.EditText
import androidx.annotation.IdRes
import com.mercandalli.android.wear.browser.R

class MainActivity : WearableActivity() {

    private val input: EditText by bind(R.id.activity_main_input)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Enables Always-on
        setAmbientEnabled()
    }

    private fun <T : View> bind(@IdRes res: Int): Lazy<T> {
        @Suppress("UNCHECKED_CAST")
        return lazy(LazyThreadSafetyMode.NONE) { findViewById<T>(res) }
    }
}
