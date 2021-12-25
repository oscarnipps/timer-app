package com.app.timerz.ui.home

import android.view.View
import android.widget.NumberPicker
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
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.app.timerz.R
import com.app.timerz.launchFragmentInHiltContainer
import com.app.timerz.ui.timerlist.TimerListFragment
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import com.google.common.truth.Truth.*

@HiltAndroidTest
class HomeFragmentTest {

    @get: Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun start_timer_button_with_default_picker_values_is_not_enabled() {
        launchFragmentInHiltContainer {
            HomeFragment().also {}
        }

        onView(withId(R.id.start_timer_button)).perform(click()).check(matches(isNotEnabled()))
    }


    @Test
    fun valid_picker_value_enables_button() {
        launchFragmentInHiltContainer {
            HomeFragment().also {}
        }

        onView(withId(R.id.seconds_picker))
            .perform(
                GeneralSwipeAction(
                    Swipe.SLOW,
                    GeneralLocation.TOP_CENTER,
                    GeneralLocation.BOTTOM_CENTER,
                    Press.FINGER
                ),
                setPickerValue(10))

        onView(withId(R.id.minute_picker))
            .perform(
                GeneralSwipeAction(
                    Swipe.SLOW,
                    GeneralLocation.TOP_CENTER,
                    GeneralLocation.BOTTOM_CENTER,
                    Press.FINGER
                ),
                setPickerValue(30))

        onView(withId(R.id.start_timer_button)).check(matches(isEnabled()))
    }

    @Test
    fun navigate_to_active_timer_fragment_when_start_timer_button_is_clicked() {
        val navController =
            TestNavHostController(ApplicationProvider.getApplicationContext())

       /* launchFragmentInHiltContainer<HomeFragment> {
            navController.setGraph(R.navigation.main_nav_graph)
            Navigation.setViewNavController(requireView(), navController)
        }*/

        launchFragmentInHiltContainer {
            HomeFragment().also {fragment ->
                // In addition to returning a new instance of our Fragment,
                // get a callback whenever the fragment’s view is created
                // or destroyed so that we can set the NavController
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        // The fragment’s view has just been created
                        navController.setGraph(R.navigation.main_nav_graph)
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

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

        onView(withId(R.id.start_timer_button)).perform(click())

        assertThat(navController.currentDestination?.id).isEqualTo(R.id.activeTimerFragment)
    }


    fun setPickerValue(value: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(NumberPicker::class.java)
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