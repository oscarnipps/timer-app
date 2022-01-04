package com.app.timerz.ui.timerlist

import android.view.View
import android.widget.NumberPicker
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.GeneralSwipeAction
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Swipe
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.app.timerz.R
import com.app.timerz.ToastMatcher
import com.app.timerz.data.local.database.entity.Timer
import com.app.timerz.launchFragmentInHiltContainer
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AddTimerFragmentTest {

    @get: Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun inputs_are_shown() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val arguments = bundleOf(Pair("timerItem", Timer(1, "workout", "00:00:30", "", "")))

        launchFragmentInHiltContainer {
            AddTimerFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->

                    if (viewLifecycleOwner != null) {
                        fragment.arguments = arguments

                        navController.setGraph(R.navigation.main_nav_graph)

                        navController.setCurrentDestination(R.id.addTimerFragment)

                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        onView(withId(R.id.timer_title)).check(matches(isDisplayed()))

        onView(withId(R.id.hour_picker)).check(matches(isDisplayed()))

        onView(withId(R.id.minute_picker)).check(matches(isDisplayed()))

        onView(withId(R.id.seconds_picker)).check(matches(isDisplayed()))
    }

    @Test
    fun create_timer_button_clicked_saves_with_toast_message() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        val timerItem = Timer(1, "cardio workout", "00:20:30", "", "")

        val arguments = bundleOf(Pair("timerItem", null))

        launchFragmentInHiltContainer {
            AddTimerFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->

                    if (viewLifecycleOwner != null) {
                        fragment.arguments = arguments

                        navController.setGraph(R.navigation.main_nav_graph)

                        navController.setCurrentDestination(R.id.addTimerFragment)

                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        onView(withId(R.id.timer_title)).perform(typeText("cardio workout"))

        onView(withId(R.id.seconds_picker))
            .perform(
                GeneralSwipeAction(
                    Swipe.SLOW,
                    GeneralLocation.TOP_CENTER,
                    GeneralLocation.BOTTOM_CENTER,
                    Press.FINGER
                ),
                setPickerValue(30)
            )

        onView(withId(R.id.minute_picker))
            .perform(
                GeneralSwipeAction(
                    Swipe.SLOW,
                    GeneralLocation.TOP_CENTER,
                    GeneralLocation.BOTTOM_CENTER,
                    Press.FINGER
                ),
                setPickerValue(30)
            )

        onView(withId(R.id.create_timer)).perform(click())

        onView(withText(R.string.timer_creation_success)).inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    private fun setPickerValue(value: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return ViewMatchers.isAssignableFrom(NumberPicker::class.java)
            }

            override fun getDescription(): String {
                return "set the value to the number picker"
            }

            override fun perform(uiController: UiController?, view: View?) {
                val numberPicker = view as NumberPicker
                numberPicker.value = value
            }

        }
    }
}