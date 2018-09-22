package com.mercandalli.android.browser.store

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.mercandalli.android.browser.in_app.InAppManager

class StoreRowView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var activityContainer: InAppManager.ActivityContainer? = null

    fun setActivityContainer(activityContainer: InAppManager.ActivityContainer) {
        this.activityContainer = activityContainer
    }
}