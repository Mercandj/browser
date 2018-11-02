package com.mercandalli.android.browser.screenshot

import android.app.Activity
import android.content.Context
import androidx.test.espresso.Espresso.onView
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.mercandalli.android.browser.main.MainActivity
import tools.fastlane.screengrab.Screengrab
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy
import tools.fastlane.screengrab.locale.LocaleTestRule
import com.mercandalli.android.browser.R
import com.mercandalli.android.browser.locale.LocaleUtils
import org.junit.runner.RunWith
import android.content.Context.MODE_PRIVATE
import android.os.SystemClock
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.test.InstrumentationRegistry
import androidx.test.InstrumentationRegistry.getInstrumentation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.typeText
import com.mercandalli.android.browser.main.ApplicationGraph
import com.mercandalli.android.browser.monetization.MonetizationGraph
import com.mercandalli.android.browser.on_boarding.OnBoardingRepositoryImpl
import java.io.File
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.filters.FlakyTest
import androidx.test.filters.LargeTest
import androidx.test.runner.lifecycle.ActivityLifecycleMonitorRegistry
import androidx.test.runner.lifecycle.Stage
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiSelector
import org.junit.BeforeClass
import org.junit.ClassRule
import org.junit.Rule
import org.junit.Test

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
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        clearSharedPreferences()
        val theme = if (darkTheme) "dark" else "light"
        val localeCode = LocaleUtils.getLocaleCode()
        val screenshotSuffix = "${theme}_$localeCode"
        val screenShooter = ScreenShooter(screenshotSuffix)
        instrumentation.runOnMainSync {
            ApplicationGraph.getAnalyticsManager().disable()
            ApplicationGraph.getProductManager().setIsAppDeveloperEnabled(true)
            MonetizationGraph.setOnBoardingStorePageAvailable(true)
            if (darkTheme) {
                ApplicationGraph.getThemeManager().setDarkEnable(true)
            }
        }
        activityRule.launchActivity(null)
        screenShooter.shoot("on_boarding_page_1")
        click(R.id.view_on_boarding_next)
        screenShooter.shoot("on_boarding_page_2")
        click(R.id.view_on_boarding_next)
        screenShooter.shoot("on_boarding_page_3")
        click(R.id.view_on_boarding_store_skip)
        screenShooter.shoot("main")
        click(R.id.activity_main_more)
        screenShooter.shoot("main_more")
        clickWithText(R.string.home)
        SystemClock.sleep(400)
        screenShooter.shoot("main_home")
        back()
        onView(withId(R.id.activity_main_search)).perform(typeText("Android"))
        SystemClock.sleep(200)
        screenShooter.shoot("main_suggestion")
        click(R.id.activity_main_more)
        onView(withText(R.string.settings)).perform(click())
        screenShooter.shoot("settings")
        back()
        click(R.id.activity_main_floating_check_box)
        click(R.id.activity_main_more)
        clickWithText(R.string.home)
        SystemClock.sleep(200)
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

    private fun getResources() = InstrumentationRegistry.getTargetContext().resources

    private fun click(@IdRes id: Int) {
        uiAutomatorClick(id)
    }

    private fun clickWithText(@StringRes id: Int) {
        uiAutomatorClickWithText(id)
    }

    private fun back() {
        uiAutomatorBack()
    }

    private fun espressoClick(@IdRes id: Int) {
        onView(withId(id)).perform(click())
    }

    private fun espressoClickWithText(@StringRes id: Int) {
        onView(withText(id)).perform(click())
    }

    private fun espressoBack() {
        Espresso.pressBack()
    }

    private fun uiAutomatorClick(@IdRes id: Int) {
        val device = UiDevice.getInstance(getInstrumentation())
        val resourceName = getResources().getResourceName(id)
        device.findObject(UiSelector().resourceId(resourceName)).click()
    }

    private fun uiAutomatorClickWithText(@IdRes id: Int) {
        val device = UiDevice.getInstance(getInstrumentation())
        val string = getResources().getString(id)
        device.findObject(UiSelector().text(string)).click()
    }

    private fun uiAutomatorBack() {
        val device = UiDevice.getInstance(getInstrumentation())
        device.pressBack()
    }

    private fun getCurrentActivity(): Activity {
        var activity: Activity? = null
        getInstrumentation().runOnMainSync {
            val resumedActivity = ActivityLifecycleMonitorRegistry.getInstance().getActivitiesInStage(Stage.RESUMED)
            val it = resumedActivity.iterator()
            activity = it.next()
        }
        return activity!!
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
