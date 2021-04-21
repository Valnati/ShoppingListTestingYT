package com.androiddevs.shoppinglisttestingyt.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.filters.MediumTest
import com.androiddevs.shoppinglisttestingyt.R
import com.androiddevs.shoppinglisttestingyt.data.local.ShoppingItem
import com.androiddevs.shoppinglisttestingyt.getOrAwaitValue
import com.androiddevs.shoppinglisttestingyt.launchFragmentInHiltContainer
import com.androiddevs.shoppinglisttestingyt.repositories.FakeShoppingRepositoryAndroidTest
import com.google.common.truth.Truth
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import javax.inject.Inject

@MediumTest
@HiltAndroidTest
@ExperimentalCoroutinesApi
class AddShoppingItemFragmentTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var fragmentFactory: ShoppingFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun pressBackButton_popBackStack() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        //very easy function for going back
        pressBack()

        verify(navController).popBackStack()
    }

    @Test
    fun clickShoppingImage_navigateToImagePickFragment() {
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
        }

        onView(withId(R.id.ivShoppingImage)).perform(click())
        verify(navController.navigate(AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()))
    }

    @Test
    fun pressBackButton_imageUrlIsEmpty() {
        val fakeViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())
        val navController = mock(NavController::class.java)

        launchFragmentInHiltContainer<AddShoppingItemFragment> {
            Navigation.setViewNavController(requireView(), navController)
            viewModel = fakeViewModel
        }
        pressBack()
        Truth.assertThat(fakeViewModel.curImageUrl.getOrAwaitValue()).isEqualTo("")
    }

    @Test
    fun clickInsertIntoDb_shoppingItemInsertedIntoDb() {
        val testViewModel = ShoppingViewModel(FakeShoppingRepositoryAndroidTest())

        launchFragmentInHiltContainer<AddShoppingItemFragment>(
            fragmentFactory = fragmentFactory
        ) {
            viewModel = testViewModel
        }

        onView(withId(R.id.etShoppingItemName)).perform(replaceText("testName"))
        onView(withId(R.id.etShoppingItemAmount)).perform(replaceText("5"))
        onView(withId(R.id.etShoppingItemPrice)).perform(replaceText("800.3"))
        onView(withId(R.id.btnAddShoppingItem)).perform(click())

        Truth.assertThat(testViewModel.shoppingItems.getOrAwaitValue())
            .contains(ShoppingItem("testName", 5, 800.3f, ""))
    }
}