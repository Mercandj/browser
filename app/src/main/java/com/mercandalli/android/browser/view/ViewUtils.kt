package com.mercandalli.android.browser.view

import android.content.res.Resources

object ViewUtils {

    /**
     * Convert a value from dip to pixel.
     *
     * @param dp the value in dip to convert.
     * @return a value in pixel.
     */
    fun dpToPx(dp: Float) = dp * Resources.getSystem().displayMetrics.density
}
