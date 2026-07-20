package com.example.havadurumu.ui.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test

class SharedViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun `selected city is initially empty`() {
        val viewModel = SharedViewModel()

        assertNull(viewModel.selectedCity.value)
    }

    @Test
    fun `update selected city publishes new value`() {
        val viewModel = SharedViewModel()

        viewModel.updateSelectedCity("Ankara")

        assertEquals("Ankara", viewModel.selectedCity.value)
    }
}
