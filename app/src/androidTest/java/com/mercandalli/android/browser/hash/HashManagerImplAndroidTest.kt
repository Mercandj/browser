package com.mercandalli.android.browser.hash

import androidx.test.runner.AndroidJUnit4
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HashManagerImplAndroidTest {

    @Test
    fun hash() {
        // Given
        val word = "maison"
        val hashManager = createInstanceToTest()

        // When
        val sha256 = hashManager.sha256(word, 32)

        // Then
        Assert.assertEquals("6bc6e5659ca38f61899f198f9a6d2d09fe6dd461f39f1eeef9f407f93529c9cc", sha256)
    }

    private fun createInstanceToTest(): HashManager {
        return HashManagerImpl()
    }
}
