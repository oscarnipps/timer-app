package com.app.timerz.ui.timerlist

import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.app.timerz.R
import com.app.timerz.launchFragmentInHiltContainer
import com.app.timerz.ui.home.HomeFragment
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Assert.*
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
                // In addition to returning a new instance of our Fragment,
                // get a callback whenever the fragment’s view is created
                // or destroyed so that we can set the NavController
                fragment.viewLifecycleOwnerLiveData.observeForever { viewLifecycleOwner ->
                    if (viewLifecycleOwner != null) {
                        // The fragment’s view has just been created
                        navController.setGraph(R.navigation.main_nav_graph)
                        navController.setCurrentDestination(R.id.timerListFragment)
                        Navigation.setViewNavController(fragment.requireView(), navController)
                    }
                }
            }

        }
        onView(withId(R.id.empty_state)).check(matches(withEffectiveVisibility(Visibility.GONE)))
    }
}