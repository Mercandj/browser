package com.mercandalli.android.browser.settings

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mercandalli.android.browser.R
import com.mercandalli.android.sdk.purchase.PurchaseManager

class SettingsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val view = LayoutInflater.from(context).inflate(R.layout.view_settings, this)
    private val recyclerView: RecyclerView = view.findViewById(R.id.view_settings_recycler_view)

    init {
        recyclerView.layoutManager = LinearLayoutManager(context)
    }

    fun setActivityContainer(activityContainer: PurchaseManager.ActivityContainer) {
        recyclerView.adapter = SettingsAdapter(activityContainer)
    }
}
