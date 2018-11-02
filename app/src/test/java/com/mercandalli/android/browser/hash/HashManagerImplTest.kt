package com.mercandalli.android.browser.hash

import org.junit.Assert
import org.junit.Test

class HashManagerImplTest {

    @Test
    fun sha256() {
        // Given
        val hashManager = HashManagerImpl()

        // When
        val sha256 = hashManager.sha256("password", 32)

        Assert.assertEquals("9e6e17cbd2bbd2087778bd6c2b8e91b37d5942dfdc4e086db4472211672301aa", sha256)
    }
}
