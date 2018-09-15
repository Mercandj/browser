package com.mercandalli.android.browser.common

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View

/**
 * Static methods for dealing with the [View]s.
 */
object ViewUtils {

    fun getSelectableItemBackground(context: Context): Drawable? {
        // Create an array of the attributes we want to resolve
        // using values from a theme
        // android.R.attr.selectableItemBackground requires API LEVEL 11
        val attrs = intArrayOf(android.R.attr.selectableItemBackground /* index 0 */)

        // Obtain the styled attributes. 'themedContext' is a context with a
        // theme, typically the current Activity (i.e. 'this')
        val ta = context.obtainStyledAttributes(attrs)

        // Now get the value of the 'listItemBackground' attribute that was
        // set in the theme used in 'themedContext'. The parameter is the index
        // of the attribute in the 'attrs' array. The returned Drawable
        // is what you are after
        val drawableFromTheme = ta.getDrawable(0 /* index */)

        // Finally free resources used by TypedArray
        ta.recycle()
        return drawableFromTheme
    }
}
