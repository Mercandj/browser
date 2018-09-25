package com.mercandalli.android.browser.dialog

import androidx.annotation.StringRes

interface DialogManager {

    /**
     * Start an alert dialog.
     *
     * @see [DialogManager.registerListener] and [DialogManager.unregisterListener]
     */
    fun alert(
            dialogId: String,
            @StringRes titleStringRes: Int,
            @StringRes messageStringRes: Int,
            @StringRes positiveStringRes: Int,
            @StringRes negativeStringRes: Int
    )

    /**
     * Start a prompt dialog.
     *
     * @see [DialogManager.registerListener] and [DialogManager.unregisterListener]
     */
    fun prompt(
            dialogId: String,
            @StringRes titleStringRes: Int,
            @StringRes messageStringRes: Int,
            @StringRes positiveStringRes: Int,
            @StringRes negativeStringRes: Int
    )

    /**
     * Check if a dialog action hasn't be consumed by listener, return and consume.
     */
    fun consumeDialogActionPositiveClicked(): DialogAction?

    /**
     * /!\ Internal to the feature.
     */
    fun onDialogPositiveClicked(dialogAction: DialogAction)

    /**
     * /!\ Internal to the feature.
     */
    fun onDialogNegativeClicked(dialogAction: DialogAction)

    fun registerListener(listener: Listener)

    fun unregisterListener(listener: Listener)

    class DialogAction(
            val dialogId: String,
            val userInput: String
    )

    interface Listener {

        /**
         * Return true if consumed.
         */
        fun onDialogPositiveClicked(dialogAction: DialogAction): Boolean

        fun onDialogNegativeClicked(dialogAction: DialogAction)
    }
}