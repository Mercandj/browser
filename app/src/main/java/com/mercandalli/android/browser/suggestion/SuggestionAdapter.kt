package com.mercandalli.android.browser.suggestion

import android.os.Build
import android.text.Html
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates4.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter

class SuggestionAdapter(
    listener: SuggestionClickListener
) : ListDelegationAdapter<List<Any>>() {

    init {
        delegatesManager.addDelegate(SuggestionAdapterDelegate(listener) as AdapterDelegate<List<Any>>)
    }

    fun populate(list: List<Any>) {
        setItems(list)
        notifyDataSetChanged()
    }

    //region Suggestion
    private class SuggestionAdapterDelegate(
        private val listener: SuggestionClickListener
    ) : AbsListItemAdapterDelegate<Any, Any, SuggestionViewHolder>() {

        override fun isForViewType(o: Any, list: List<Any>, i: Int): Boolean {
            return o is String
        }

        override fun onCreateViewHolder(viewGroup: ViewGroup): SuggestionViewHolder {
            val context = viewGroup.context
            val textView = SuggestionView(context)
            val layoutParams = RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
            )
            textView.layoutParams = layoutParams
            return SuggestionViewHolder(textView, listener)
        }

        override fun onBindViewHolder(
            model: Any,
            titleViewHolder: SuggestionViewHolder,
            list: List<Any>
        ) {
            titleViewHolder.bind(model as String)
        }
    }

    private class SuggestionViewHolder(
        private val view: SuggestionView,
        private val listener: SuggestionClickListener
    ) : RecyclerView.ViewHolder(view) {

        private var suggestion: String? = null

        init {
            view.setListener(object : SuggestionView.Listener {
                override fun onSuggestionTestClicked() {
                    if (suggestion != null) {
                        listener.onSuggestionClicked(suggestion!!)
                    }
                }

                override fun onSuggestionImageClicked() {
                    if (suggestion != null) {
                        listener.onSuggestionImageClicked(suggestion!!)
                    }
                }
            })
        }

        fun bind(suggestion: String) {
            this.suggestion = suggestion
            val spanned = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(suggestion, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(suggestion)
            }
            view.setText(spanned)
        }
    }

    interface SuggestionClickListener {
        fun onSuggestionClicked(suggestion: String)
        fun onSuggestionImageClicked(suggestion: String)
    }
    //endregion Suggestion
}
