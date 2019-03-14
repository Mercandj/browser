package com.mercandalli.android.browser.settings

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.mercandalli.android.browser.in_app.InAppManager
import com.mercandalli.android.browser.settings_about.SettingsAboutView
import com.mercandalli.android.browser.settings_ad_blocker.SettingsAdBlockerView
import com.mercandalli.android.browser.settings_search_engine.SettingsSearchEngineView
import com.mercandalli.android.browser.settings_theme.SettingsThemeView

class SettingsAdapter(
    activityContainer: InAppManager.ActivityContainer
) : ListDelegationAdapter<List<Any>>() {

    init {
        delegatesManager.addDelegate(SettingsThemeAdapterDelegate() as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(SettingsSearchEngineAdapterDelegate(activityContainer) as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(SettingsAdBlockerAdapterDelegate(activityContainer) as AdapterDelegate<List<Any>>)
        delegatesManager.addDelegate(SettingsAboutAdapterDelegate() as AdapterDelegate<List<Any>>)
        populate(listOf(
            SettingsTheme(),
            SettingsSearchEngine(),
            SettingsAdBlocker(),
            SettingsAbout()
        ))
    }

    private fun populate(list: List<Any>) {
        setItems(list)
        notifyDataSetChanged()
    }

    //region SettingsTheme
    class SettingsTheme

    private class SettingsThemeAdapterDelegate :
        AbsListItemAdapterDelegate<Any, Any, SettingsThemeViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int) = o is SettingsTheme

        override fun onCreateViewHolder(viewGroup: ViewGroup): SettingsThemeViewHolder {
            val context = viewGroup.context
            val view = SettingsThemeView(context)
            val layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            view.layoutParams = layoutParams
            return SettingsThemeViewHolder(view)
        }

        override fun onBindViewHolder(
            model: Any,
            titleViewHolder: SettingsThemeViewHolder,
            list: List<Any>
        ) {
        }
    }

    private class SettingsThemeViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view)
    //endregion SettingsTheme

    //region SettingsSearchEngine
    class SettingsSearchEngine

    private class SettingsSearchEngineAdapterDelegate(
        private val activityContainer: InAppManager.ActivityContainer
    ) : AbsListItemAdapterDelegate<Any, Any, SettingsSearchEngineViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int) = o is SettingsSearchEngine

        override fun onCreateViewHolder(viewGroup: ViewGroup): SettingsSearchEngineViewHolder {
            val context = viewGroup.context
            val view = SettingsSearchEngineView(context)
            val layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            view.setActivityContainer(activityContainer)
            view.layoutParams = layoutParams
            return SettingsSearchEngineViewHolder(view)
        }

        override fun onBindViewHolder(
            model: Any,
            titleViewHolder: SettingsSearchEngineViewHolder,
            list: List<Any>
        ) {
        }
    }

    private class SettingsSearchEngineViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view)
    //endregion SettingsSearchEngine

    //region SettingsAdBlocker
    class SettingsAdBlocker

    private class SettingsAdBlockerAdapterDelegate(
        private val activityContainer: InAppManager.ActivityContainer
    ) : AbsListItemAdapterDelegate<Any, Any, SettingsAdBlockerViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int) = o is SettingsAdBlocker

        override fun onCreateViewHolder(viewGroup: ViewGroup): SettingsAdBlockerViewHolder {
            val context = viewGroup.context
            val view = SettingsAdBlockerView(context)
            val layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            view.setActivityContainer(activityContainer)
            view.layoutParams = layoutParams
            return SettingsAdBlockerViewHolder(view)
        }

        override fun onBindViewHolder(
            model: Any,
            titleViewHolder: SettingsAdBlockerViewHolder,
            list: List<Any>
        ) {
        }
    }

    private class SettingsAdBlockerViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view)
    //endregion SettingsAdBlocker

    //region SettingsAbout
    class SettingsAbout

    private class SettingsAboutAdapterDelegate :
        AbsListItemAdapterDelegate<Any, Any, SettingsAboutViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int) = o is SettingsAbout

        override fun onCreateViewHolder(viewGroup: ViewGroup): SettingsAboutViewHolder {
            val context = viewGroup.context
            val view = SettingsAboutView(context)
            val layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            view.layoutParams = layoutParams
            return SettingsAboutViewHolder(view)
        }

        override fun onBindViewHolder(
            model: Any,
            titleViewHolder: SettingsAboutViewHolder,
            list: List<Any>
        ) {
        }
    }

    private class SettingsAboutViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view)
    //endregion SettingsAbout
}
