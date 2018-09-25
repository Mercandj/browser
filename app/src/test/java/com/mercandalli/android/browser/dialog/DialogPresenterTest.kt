package com.mercandalli.android.browser.dialog

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DialogPresenterTest {

    @Mock
    private val screen: DialogContract.Screen? = null
    @Mock
    private val dialogManager: DialogManager? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onCreateSetTitle() {
        // Given
        val dialogPresenter = createInstanceToTest()

        // When
        dialogPresenter.onCreate(DialogActivity.DialogInput(
                "dialogId",
                "title",
                "message",
                "positive",
                "negative",
                DialogActivity.DialogInput.DIALOG_TYPE_ALERT
        ))

        // Then
        Mockito.verify(screen)!!.setTitle("title")
    }

    private fun createInstanceToTest(): DialogPresenter {
        return DialogPresenter(
                screen!!,
                dialogManager!!
        )
    }
}