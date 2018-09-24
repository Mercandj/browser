package com.mercandalli.android.browser.theme

import com.mercandalli.android.browser.common.HashMapSharedPreferences
import org.junit.Assert
import org.junit.Test

class ThemeManagerImplTest {

    @Test
    fun defaultThemeIsNotDark() {
        // Given
        val themeManager = ThemeManagerImpl(HashMapSharedPreferences())

        // When
        val darkEnable = themeManager.isDarkEnable()

        // Then
        Assert.assertFalse(darkEnable)
    }
}