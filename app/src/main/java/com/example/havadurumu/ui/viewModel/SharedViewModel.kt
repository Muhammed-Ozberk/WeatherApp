package com.example.havadurumu.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _selectedCity = MutableLiveData<String>()
    val selectedCity: LiveData<String>
        get() = _selectedCity

    fun updateSelectedCity(city: String) {
        _selectedCity.value = city
    }
}
