package com.mercandalli.android.browser.main

import com.mercandalli.android.browser.floating.FloatingManager
import com.mercandalli.android.browser.product.ProductManager
import com.mercandalli.android.browser.search_engine.SearchEngineManager
import com.mercandalli.android.browser.suggestion.SuggestionManager
import com.mercandalli.android.browser.theme.ThemeManager
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MainActivityPresenterTest {

    @Mock
    private val screen: MainActivityContract.Screen? = null
    @Mock
    private val themeManager: ThemeManager? = null
    @Mock
    private val searchEngineManager: SearchEngineManager? = null
    @Mock
    private val suggestionManager: SuggestionManager? = null
    @Mock
    private val floatingManager: FloatingManager? = null
    @Mock
    private val productManager: ProductManager? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun onClearDataClickedShowClearDataMessage() {
        // Given
        val userAction = createInstanceToTest()

        // When
        userAction.onClearDataClicked()

        // Then
        Mockito.verify(screen)!!.showClearDataMessage()
    }

    private fun createInstanceToTest(): MainActivityContract.UserAction {
        return MainActivityPresenter(
                screen!!,
                themeManager!!,
                searchEngineManager!!,
                suggestionManager!!,
                floatingManager!!,
                productManager!!
        )
    }
}