package com.mercandalli.android.browser.dialog

import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DialogPresenterTest {

    @Mock
    private lateinit var screen: DialogContract.Screen
    @Mock
    private lateinit var dialogManager: DialogManager

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
        Mockito.verify(screen).setTitle("title")
    }

    private fun createInstanceToTest(): DialogPresenter {
        return DialogPresenter(
                screen,
                dialogManager
        )
    }
}
