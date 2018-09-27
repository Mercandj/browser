package com.mercandalli.android.browser.screenshot

import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.mercandalli.android.browser.main.MainActivity
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy
import tools.fastlane.screengrab.locale.LocaleTestRule
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.locale.LocaleUtils
import com.mercandalli.android.browser.view_action.ClearFocus
import org.junit.*
import org.junit.runner.RunWith
import android.content.Context.MODE_PRIVATE
import android.os.SystemClock
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso
import com.mercandalli.android.browser.main.ApplicationGraph
import com.mercandalli.android.browser.monetization.MonetizationGraph
import com.mercandalli.android.browser.on_boarding.OnBoardingRepositoryImpl
import java.io.File
import androidx.test.espresso.action.ViewActions.*
import androidx.test.filters.FlakyTest
import androidx.test.filters.LargeTest

@FlakyTest
@LargeTest
@RunWith(AndroidJUnit4::class)
class ScreenshotAndroidTest {

    @get:Rule
    val activityRule = ActivityTestRule(
            MainActivity::class.java,
            false,
            false
    )

    @Test
    fun screenshotFreeVersionLightTheme() {
        screenshot(false)
    }

    @Test
    fun screenshotFreeVersionDarkTheme() {
        screenshot(true)
    }

    private fun screenshot(darkTheme: Boolean) {
        clearSharedPreferences()
        disableAnalytics()
        activityRule.launchActivity(null)
        val theme = if (darkTheme) "dark" else "light"
        val localeCode = LocaleUtils.getLocaleCode()
        val screenshotSuffix = "${theme}_$localeCode"
        val screenShooter = ScreenShooter(screenshotSuffix)
        onView(withId(R.id.view_on_boarding_next)).check(matches(isDisplayed()))
        onView(withId(R.id.view_on_boarding_view_pager)).perform(ClearFocus())
        if (darkTheme) {
            onView(withId(R.id.view_on_boarding_next)).perform(click())
            onView(withId(R.id.view_on_boarding_page_theme_dark)).perform(click())
            onView(withId(R.id.view_on_boarding_view_pager)).perform(swipeRight())
        }
        onView(withId(R.id.view_on_boarding_view_pager)).perform(ClearFocus())
        screenShooter.shoot("on_boarding_page_1")
        onView(withId(R.id.view_on_boarding_next)).perform(click())
        screenShooter.shoot("on_boarding_page_2")
        onView(withId(R.id.view_on_boarding_next)).perform(click())
        screenShooter.shoot("on_boarding_page_3")
        ApplicationGraph.getProductManager().setIsAppDeveloperEnabled(true)
        onView(withId(R.id.view_on_boarding_store_skip)).perform(click())
        screenShooter.shoot("main")
        onView(withId(R.id.activity_main_more)).perform(click())
        screenShooter.shoot("main_more")
        onView(withText(R.string.home)).perform(click())
        SystemClock.sleep(900)
        screenShooter.shoot("main_home")
        Espresso.pressBack()
        onView(withId(R.id.activity_main_search)).perform(typeText("Android"))
        SystemClock.sleep(600)
        screenShooter.shoot("main_suggestion")
        onView(withId(R.id.activity_main_more)).perform(click())
        onView(withText(R.string.settings)).perform(click())
        screenShooter.shoot("settings")
        Espresso.pressBack()
        onView(withId(R.id.activity_main_floating_check_box)).perform(click())
        onView(withId(R.id.activity_main_more)).perform(click())
        onView(withText(R.string.home)).perform(click())
        SystemClock.sleep(600)
        screenShooter.shoot("floating_home")
        ApplicationGraph.getFloatingManager().stop()
        clearSharedPreferences()
        activityRule.finishActivity()
    }

    private fun clearSharedPreferences() {
        val targetContext = InstrumentationRegistry.getTargetContext()
        val root = targetContext.filesDir.parentFile
        val sharedPreferencesFileNames = File(root, "shared_prefs").list()
        for (fileName in sharedPreferencesFileNames) {
            val fileNameWithoutExt = fileName.replace(".xml", "")
            val sharedPreferences = targetContext.getSharedPreferences(
                    fileNameWithoutExt, MODE_PRIVATE
            )
            sharedPreferences.edit().clear().commit()
        }
        targetContext.getSharedPreferences(
                OnBoardingRepositoryImpl.PREFERENCE_NAME,
                Context.MODE_PRIVATE
        ).edit().clear().commit()
        MonetizationGraph.getOnBoardingRepository().clear()
    }

    private fun disableAnalytics() {
        ApplicationGraph.getAnalyticsManager().disable()
    }

    companion object {

        @get:ClassRule
        @JvmStatic
        val localeTestRule = LocaleTestRule()

        @get:ClassRule
        @JvmStatic
        val demoModeRule = DemoModeRule()

        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            Screengrab.setDefaultScreenshotStrategy(UiAutomatorScreenshotStrategy())
        }
    }
}