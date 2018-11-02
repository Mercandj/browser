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

    @Test
    fun setDarkEnablePersisted() {
        // Given
        val sharedPreferences = HashMapSharedPreferences()
        val themeManager = ThemeManagerImpl(sharedPreferences)

        // When
        themeManager.setDarkEnable(true)
        val darkEnable = ThemeManagerImpl(sharedPreferences).isDarkEnable()

        // Then
        Assert.assertTrue(darkEnable)
    }
}
