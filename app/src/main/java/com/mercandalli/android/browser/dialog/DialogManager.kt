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

    fun onDialogPositiveClicked(dialogId: String, userInput: String)

    fun onDialogNegativeClicked(dialogId: String)

    fun registerListener(listener: Listener)

    fun unregisterListener(listener: Listener)

    interface Listener {

        fun onDialogPositiveClicked(
                dialogId: String,
                userInput: String
        )

        fun onDialogNegativeClicked(
                dialogId: String
        )
    }
}