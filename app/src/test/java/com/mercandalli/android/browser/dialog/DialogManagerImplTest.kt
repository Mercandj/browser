package com.mercandalli.android.browser.dialog

import androidx.annotation.StringRes
import org.junit.Before
import org.junit.Test
import org.mockito.*

class DialogManagerImplTest {

    private val dialogId = "dialogId"
    @StringRes
    private val titleStringRes = 1
    @StringRes
    private val messageStringRes = 2
    @StringRes
    private val positiveStringRes = 3
    @StringRes
    private val negativeStringRes = 4
    private val titleString = "titleString"
    private val messageString = "messageString"
    private val positiveString = "positiveString"
    private val negativeString = "negativeString"
    @Captor
    private lateinit var dialogInputCaptor: ArgumentCaptor<DialogActivity.DialogInput>

    @Mock
    private lateinit var addOn: DialogManagerImpl.AddOn

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun alertStartDialog() {
        // Given
        val dialogManager = createInstanceToTest()
        val referenceDialogInput = DialogActivity.DialogInput(
                dialogId,
                titleString,
                messageString,
                positiveString,
                negativeString,
                DialogActivity.DialogInput.DIALOG_TYPE_ALERT
        )

        // When
        dialogManager.alert(
                dialogId,
                titleStringRes,
                messageStringRes,
                positiveStringRes,
                negativeStringRes
        )

        // Then
        Mockito.verify(addOn).startDialogActivity(capture(dialogInputCaptor))
        val dialogInput = dialogInputCaptor.value
        DialogActivityTest.assertDialogInputEquals(referenceDialogInput, dialogInput)
    }

    @Test
    fun promptStartDialog() {
        // Given
        val dialogManager = createInstanceToTest()
        val referenceDialogInput = DialogActivity.DialogInput(
                dialogId,
                titleString,
                messageString,
                positiveString,
                negativeString,
                DialogActivity.DialogInput.DIALOG_TYPE_PROMPT
        )

        // When
        dialogManager.prompt(
                dialogId,
                titleStringRes,
                messageStringRes,
                positiveStringRes,
                negativeStringRes
        )

        // Then
        Mockito.verify(addOn).startDialogActivity(capture(dialogInputCaptor))
        val dialogInput = dialogInputCaptor.value
        DialogActivityTest.assertDialogInputEquals(referenceDialogInput, dialogInput)
    }

    private fun createInstanceToTest(): DialogManager {
        Mockito.`when`(addOn.getString(titleStringRes)).thenReturn(titleString)
        Mockito.`when`(addOn.getString(messageStringRes)).thenReturn(messageString)
        Mockito.`when`(addOn.getString(positiveStringRes)).thenReturn(positiveString)
        Mockito.`when`(addOn.getString(negativeStringRes)).thenReturn(negativeString)
        return DialogManagerImpl(
                addOn
        )
    }

    /**
     * Returns ArgumentCaptor.capture() as nullable type to avoid java.lang.IllegalStateException
     * when null is returned.
     * <p>
     * @linkplain https://github.com/googlesamples/android-architecture-components/blob/master/BasicRxJavaSampleKotlin/app/src/test/java/com/example/android/observability/MockitoKotlinHelpers.kt
     */
    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
}