@file:Suppress("PackageName")

/* ktlint-disable package-name */
package com.mercandalli.android.browser.ad_blocker

import android.content.SharedPreferences
import com.mercandalli.android.browser.common.HashMapSharedPreferences
import com.mercandalli.android.browser.product.ProductManager
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class AdBlockerManagerImplTest {

    @Mock
    private val productManager: ProductManager? = null

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun disableByDefaultForNonFullVersion() {
        // Given
        Mockito.`when`(productManager!!.isFullVersionAvailable()).thenReturn(false)
        val adBlockerManager = createInstanceToTest(HashMapSharedPreferences())

        // When
        val enabled = adBlockerManager.isEnabled()

        // Then
        Assert.assertFalse(enabled)
    }

    @Test
    fun disableByDefaultForFullVersion() {
        // Given
        Mockito.`when`(productManager!!.isFullVersionAvailable()).thenReturn(true)
        val adBlockerManager = createInstanceToTest(HashMapSharedPreferences())

        // When
        val enabled = adBlockerManager.isEnabled()

        // Then
        Assert.assertFalse(enabled)
    }

    private fun createInstanceToTest(
        sharedPreferences: SharedPreferences = HashMapSharedPreferences()
    ): AdBlockerManager {
        return AdBlockerManagerImpl(
            sharedPreferences,
            productManager!!
        )
    }
}
