package com.mercandalli.android.browser.dialog

interface DialogContract {

    interface UserAction {

        fun onCreate(dialogInput: DialogActivity.DialogInput)

        fun onPositiveClicked(input: String)

        fun onNegativeClicked(input: String)
    }

    interface Screen {

        fun setTitle(text: String)

        fun setMessage(text: String)

        fun setPositive(text: String)

        fun setNegative(text: String)

        fun showInput()

        fun hideInput()

        fun showSoftInput()

        fun quit()
    }
}