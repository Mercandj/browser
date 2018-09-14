package com.mercandalli.android.browser.dialog

import androidx.annotation.StringRes

class DialogManagerImpl(
        private val addOn: AddOn
) : DialogManager {

    private val listeners = ArrayList<DialogManager.Listener>()

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
                false
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
                true
        ))
    }

    override fun onDialogPositiveClicked(dialogId: String, userInput: String) {
        for (listener in listeners) {
            listener.onDialogPositiveClicked(dialogId, userInput)
        }
    }

    override fun onDialogNegativeClicked(dialogId: String) {
        for (listener in listeners) {
            listener.onDialogNegativeClicked(dialogId)
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