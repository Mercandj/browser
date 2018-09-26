package com.mercandalli.android.browser.update

import android.content.SharedPreferences
import com.mercandalli.android.browser.common.HashMapSharedPreferences
import com.mercandalli.android.browser.version.VersionManager
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UpdateManagerImplTest {

    @Mock
    private lateinit var versionManager: VersionManager

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun initialIsFirstLaunchAfterUpdateIsTrue() {
        // Given
        Mockito.`when`(versionManager.getVersionName()).thenReturn("1.00.01")
        val updateManager = createInstanceToTest()

        // When
        val firstLaunchAfterUpdate = updateManager.isFirstLaunchAfterUpdate()

        // Then
        Assert.assertTrue(firstLaunchAfterUpdate)
    }

    private fun createInstanceToTest(
            sharedPreferences: SharedPreferences = HashMapSharedPreferences()
    ): UpdateManager {
        return UpdateManagerImpl(
                sharedPreferences,
                versionManager
        )
    }
}