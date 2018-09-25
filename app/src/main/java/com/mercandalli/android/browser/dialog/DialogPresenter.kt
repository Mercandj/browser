package com.mercandalli.android.browser.dialog

internal class DialogPresenter(
        private val screen: DialogContract.Screen,
        private val dialogManager: DialogManager
) : DialogContract.UserAction {

    private lateinit var dialogInput: DialogActivity.DialogInput

    override fun onCreate(dialogInput: DialogActivity.DialogInput) {
        this.dialogInput = dialogInput
        screen.setTitle(dialogInput.title)
        screen.setMessage(dialogInput.message)
        screen.setPositive(dialogInput.positive)
        screen.setNegative(dialogInput.negative)
        if (dialogInput.type == DialogActivity.DialogInput.DIALOG_TYPE_PROMPT) {
            screen.showInput()
            screen.showSoftInput()
        } else {
            screen.hideInput()
        }
    }

    override fun onPositiveClicked(input: String) {
        screen.quit()
        dialogManager.onDialogPositiveClicked(
                DialogManager.DialogAction(
                        dialogInput.dialogId,
                        input
                )
        )
    }

    override fun onNegativeClicked(input: String) {
        screen.quit()
        dialogManager.onDialogNegativeClicked(
                DialogManager.DialogAction(
                        dialogInput.dialogId,
                        input
                )
        )
    }
}