package com.mercandalli.android.browser.keyboard

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

/**
 * Static methods for dealing with the keyboard.
 */
object KeyboardUtils {

    /**
     * Manage the soft input (keyboard).
     */
    fun showSoftInput(editText: EditText) {
        val context = editText.context
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
    }

    /**
     * Manage the soft input (keyboard).
     */
    fun hideSoftInput(editText: EditText) {
        val context = editText.context
        val inputMethodManager = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(editText.windowToken, 0)
    }
}// Non-instantiable.
