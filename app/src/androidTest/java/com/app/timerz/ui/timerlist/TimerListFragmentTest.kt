package com.app.timerz.ui.timerlist

import android.view.View
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.app.timerz.R
import com.app.timerz.ToastMatcher
import com.app.timerz.launchFragmentInHiltContainer
import com.app.timerz.ui.home.HomeFragment
import com.google.common.truth.Truth
import com.google.common.truth.Truth.*
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.hamcrest.Matcher
import org.hamcrest.Matchers.any
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class TimerListFragmentTest {

    @get: Rule
    val hiltRule = HiltAndroidRule(this)

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun empty_state_has_gone_visibility() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        launchFragmentInHiltContainer {
            TimerListFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->

                    if (viewLifecycleOwner != null) {
                        navController.setGraph(R.navigation.main_nav_graph)

                        navController.setCurrentDestination(R.id.timerListFragment)

                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        onView(withId(R.id.empty_state)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }

    @Test
    fun show_dialog_fragment_when_add_button_is_clicked() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        launchFragmentInHiltContainer {
            TimerListFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->

                    if (viewLifecycleOwner != null) {
                        navController.setGraph(R.navigation.main_nav_graph)

                        navController.setCurrentDestination(R.id.timerListFragment)

                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        onView(withId(R.id.create_timer)).perform(click())

        assertThat(navController.currentDestination?.id).isEqualTo(R.id.addTimerFragment)
    }

    @Test
    fun navigate_to_timer_fragment_when_start_view_in_recycler_view_list_item_is_clicked() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        launchFragmentInHiltContainer {
            TimerListFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->

                    if (viewLifecycleOwner != null) {
                        navController.setGraph(R.navigation.main_nav_graph)

                        navController.setCurrentDestination(R.id.timerListFragment)

                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        onView(withId(R.id.timer_list_recyclerview))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<TimerListAdapter.TimerListViewHolder>(
                    0,
                    clickRecyclerViewChildViewWithId(R.id.start)
                )
            )

        assertThat(navController.currentDestination?.id).isEqualTo(R.id.activeTimerFragment)
    }

    @Test
    fun delete_item_when_delete_view_in_recycler_view_list_item_is_clicked() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())

        launchFragmentInHiltContainer {
            TimerListFragment().also { fragment ->
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->

                    if (viewLifecycleOwner != null) {
                        navController.setGraph(R.navigation.main_nav_graph)

                        navController.setCurrentDestination(R.id.timerListFragment)

                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }
        }

        onView(withId(R.id.timer_list_recyclerview))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<TimerListAdapter.TimerListViewHolder>(
                    0,
                    clickRecyclerViewChildViewWithId(R.id.delete)
                )
            )

        onView(withText(R.string.timer_deletion_success))
            .inRoot(ToastMatcher())
            .check(matches(isDisplayed()))
    }

    private fun clickRecyclerViewChildViewWithId(id: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return any(View::class.java)
            }

            override fun getDescription(): String {
                return "Click on a child view with specified id."
            }

            override fun perform(uiController: UiController?, view: View) {
                val childView = view.findViewById<View>(id)
                childView.performClick()
            }

        }
    }
}