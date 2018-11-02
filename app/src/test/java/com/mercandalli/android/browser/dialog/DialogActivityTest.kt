package com.mercandalli.android.browser.dialog

import org.junit.Assert

class DialogActivityTest {

    companion object {

        fun assertDialogInputEquals(
            reference: DialogActivity.DialogInput,
            toCompare: DialogActivity.DialogInput
        ) {
            Assert.assertEquals(reference.dialogId, toCompare.dialogId)
            Assert.assertEquals(reference.title, toCompare.title)
            Assert.assertEquals(reference.message, toCompare.message)
            Assert.assertEquals(reference.positive, toCompare.positive)
            Assert.assertEquals(reference.negative, toCompare.negative)
            Assert.assertEquals(reference.type, toCompare.type)
        }
    }
}
