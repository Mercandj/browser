package com.mercandalli.android.browser.suggestion

import android.os.Build
import android.text.Html
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.hannesdorfmann.adapterdelegates3.AbsListItemAdapterDelegate
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import com.mercandalli.android.browser.R

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
            val textView = SuggestionTextView(context)
            val layoutParams = RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT
            )
            val marginHorizontal = context.resources.getDimensionPixelSize(R.dimen.default_space_2)
            val marginTop = context.resources.getDimensionPixelSize(R.dimen.default_space)
            val marginBottom = context.resources.getDimensionPixelSize(R.dimen.default_space)
            layoutParams.setMargins(marginHorizontal, marginTop, marginHorizontal, marginBottom)
            textView.layoutParams = layoutParams
            textView.setTextColor(ContextCompat.getColor(context, R.color.color_text_title_1))
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.resources.getDimension(R.dimen.text_size_xl))
            return SuggestionViewHolder(textView, listener)
        }

        override fun onBindViewHolder(
                model: Any, titleViewHolder: SuggestionViewHolder, list: List<Any>
        ) {
            titleViewHolder.bind(model as String)
        }
    }

    private class SuggestionViewHolder(
            private val view: TextView,
            private val listener: SuggestionClickListener
    ) : RecyclerView.ViewHolder(view) {

        private var suggestion: String? = null

        init {
            view.setOnClickListener {
                if (suggestion != null) {
                    listener.onSuggestionClicked(suggestion!!)
                }
            }
        }

        fun bind(suggestion: String) {
            this.suggestion = suggestion
            view.text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Html.fromHtml(suggestion, Html.FROM_HTML_MODE_COMPACT)
            } else {
                Html.fromHtml(suggestion)
            }
        }
    }

    interface SuggestionClickListener {
        fun onSuggestionClicked(suggestion: String)
    }
    //endregion Suggestion
}