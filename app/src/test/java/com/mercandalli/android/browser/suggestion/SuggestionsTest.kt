package com.mercandalli.android.browser.suggestion

import org.junit.Assert
import org.junit.Test
import java.io.File

class SuggestionsTest {

    @Test
    fun createFromGoogleRolandGarros() {
        // Given
        val classLoader = javaClass.classLoader
        val resource = classLoader!!.getResource("suggestions.json")
        Assert.assertNotNull(resource)
        val json = File(resource.path).readText()

        // When
        val suggestions = Suggestions.createFromGoogle(json)

        // Then
        Assert.assertEquals("roland garros", suggestions.searchInput)
        Assert.assertEquals("roland garros", suggestions.suggestions[0])
        Assert.assertEquals("roland garros<b> tickets</b>", suggestions.suggestions[1])
        Assert.assertEquals(10, suggestions.suggestions.size)
    }
}