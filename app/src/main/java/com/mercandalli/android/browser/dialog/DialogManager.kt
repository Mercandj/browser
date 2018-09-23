package com.mercandalli.android.browser.dialog

import androidx.annotation.StringRes

interface DialogManager {

    fun alert(
            dialogId: String,
            @StringRes titleStringRes: Int,
            @StringRes messageStringRes: Int,
            @StringRes positiveStringRes: Int,
            @StringRes negativeStringRes: Int
    )

    fun prompt(
            dialogId: String,
            @StringRes titleStringRes: Int,
            @StringRes messageStringRes: Int,
            @StringRes positiveStringRes: Int,
            @StringRes negativeStringRes: Int
    )

    fun consumeDialogActionPositiveClicked(): DialogAction?

    fun onDialogPositiveClicked(dialogAction: DialogAction)

    fun onDialogNegativeClicked(dialogAction: DialogAction)

    fun registerListener(listener: Listener)

    fun unregisterListener(listener: Listener)

    class DialogAction(
            val dialogId: String,
            val userInput: String
    )

    interface Listener {

        /**
         * Return true if consumed
         */
        fun onDialogPositiveClicked(dialogAction: DialogAction): Boolean

        fun onDialogNegativeClicked(dialogAction: DialogAction)
    }
}