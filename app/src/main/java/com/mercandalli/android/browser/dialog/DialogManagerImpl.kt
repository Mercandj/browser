package com.mercandalli.android.browser.dialog

import androidx.annotation.StringRes

internal class DialogManagerImpl(
    private val addOn: AddOn
) : DialogManager {

    private val listeners = ArrayList<DialogManager.Listener>()
    private var unconsumedDialogActionPositiveClicked: DialogManager.DialogAction? = null

    override fun alert(
        dialogId: String,
        @StringRes titleStringRes: Int,
        @StringRes messageStringRes: Int,
        @StringRes positiveStringRes: Int,
        @StringRes negativeStringRes: Int
    ) {
        val title = addOn.getString(titleStringRes)
        val message = addOn.getString(messageStringRes)
        val positive = addOn.getString(positiveStringRes)
        val negative = addOn.getString(negativeStringRes)
        addOn.startDialogActivity(DialogActivity.DialogInput(
            dialogId,
            title,
            message,
            positive,
            negative,
            DialogActivity.DialogInput.DIALOG_TYPE_ALERT
        ))
    }

    override fun prompt(
        dialogId: String,
        @StringRes titleStringRes: Int,
        @StringRes messageStringRes: Int,
        @StringRes positiveStringRes: Int,
        @StringRes negativeStringRes: Int
    ) {
        val title = addOn.getString(titleStringRes)
        val message = addOn.getString(messageStringRes)
        val positive = addOn.getString(positiveStringRes)
        val negative = addOn.getString(negativeStringRes)
        addOn.startDialogActivity(DialogActivity.DialogInput(
            dialogId,
            title,
            message,
            positive,
            negative,
            DialogActivity.DialogInput.DIALOG_TYPE_PROMPT
        ))
    }

    override fun consumeDialogActionPositiveClicked(): DialogManager.DialogAction? {
        val tmp = unconsumedDialogActionPositiveClicked
        unconsumedDialogActionPositiveClicked = null
        return tmp
    }

    override fun onDialogPositiveClicked(dialogAction: DialogManager.DialogAction) {
        unconsumedDialogActionPositiveClicked = dialogAction
        for (listener in listeners) {
            if (listener.onDialogPositiveClicked(dialogAction)) {
                unconsumedDialogActionPositiveClicked = null
                return
            }
        }
    }

    override fun onDialogNegativeClicked(dialogAction: DialogManager.DialogAction) {
        for (listener in listeners) {
            listener.onDialogNegativeClicked(dialogAction)
        }
    }

    override fun registerListener(listener: DialogManager.Listener) {
        if (listeners.contains(listener)) {
            return
        }
        listeners.add(listener)
    }

    override fun unregisterListener(listener: DialogManager.Listener) {
        listeners.remove(listener)
    }

    interface AddOn {

        fun getString(@StringRes stringRes: Int): String

        fun startDialogActivity(dialogInput: DialogActivity.DialogInput)
    }
}
