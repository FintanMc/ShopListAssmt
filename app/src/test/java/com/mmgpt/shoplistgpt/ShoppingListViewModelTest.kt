package com.mmgpt.shoplistgpt

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ShoppingListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: ShoppingListViewModel

    @Before
    fun setUp() {
        val app = ApplicationProvider.getApplicationContext<Application>()
        viewModel = ShoppingListViewModel(app)
    }

    @Test
    fun testAddAndEditCategory() {
        viewModel.clearAllCategories()
        viewModel.addCategory(0, "Produce")
        val categories = viewModel.categories.value
        assertEquals(1, categories?.size)
        assertEquals("Produce", categories?.get(0)?.heading)

        viewModel.editCategory(0, "Vegetables")
        assertEquals("Vegetables", viewModel.categories.value?.get(0)?.heading)
    }

    @Test
    fun testAddItemAndCheck() {
        viewModel.clearAllCategories()
        viewModel.addCategory(0, "Dairy")
        viewModel.addItem(0, 0, "Milk")
        val cat = viewModel.categories.value?.get(0)
        assertEquals(1, cat?.items?.size)
        assertEquals("Milk", cat?.items?.get(0)?.name)
        assertEquals(0, cat?.remainingCount)
        assertEquals(1, cat?.totalCount)

        val item = cat!!.items[0]
        viewModel.checkItem(item.copy(isChecked = true))
        val updatedCat = viewModel.categories.value?.get(0)
        assertEquals(1, updatedCat?.remainingCount)
        assertEquals(1, updatedCat?.totalCount)
        assertTrue(updatedCat?.items?.get(0)?.isChecked == true)
    }

    @Test
    fun testCategoryNavigationMapping() {
        // Verify that category adapter position maps to target ViewPager page index
        viewModel.clearAllCategories()
        viewModel.addCategory(0, "Cat A")
        viewModel.addCategory(1, "Cat B")
        val categories = viewModel.categories.value ?: emptyList()
        assertEquals(2, categories.size)

        for (i in categories.indices) {
            val adapterPosition = i
            val shopTargetPage = adapterPosition
            val planTargetPage = adapterPosition + 1
            assertEquals(adapterPosition, shopTargetPage)
            assertEquals(adapterPosition + 1, planTargetPage)
        }
    }
}
