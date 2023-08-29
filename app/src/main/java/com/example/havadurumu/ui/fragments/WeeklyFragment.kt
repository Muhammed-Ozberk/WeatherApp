package com.example.havadurumu.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.havadurumu.data.Repo
import com.example.havadurumu.databinding.FragmentWeeklyBinding
import com.example.havadurumu.ui.adapters.WeatherAdapter
import com.example.havadurumu.ui.viewModel.SharedViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class WeeklyFragment : Fragment() {
    private lateinit var binding: FragmentWeeklyBinding
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeeklyBinding.inflate(inflater, container, false)

        val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedCity = sharedPreferences?.getString("selectedCity", null)?.toLowerCase()


        updateWeatherData(savedCity ?: "istanbul")

        return binding.root
    }

    private fun updateWeatherData(city: String) {
        val repo = Repo()
        // Coroutine scope içinde çağırma
        GlobalScope.launch {
            val weatherData = repo.getWeatherData(city ?: "istanbul")
            if (weatherData.isNotEmpty()) {
                activity?.runOnUiThread {
                    binding.ondortRV.adapter = WeatherAdapter(requireContext(), weatherData)
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedCity = sharedPreferences?.getString("selectedCity", null)?.toLowerCase()

        // İlk açılışta ve şehir değişikliklerinde verileri güncelle
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Observe the selected city LiveData and update the UI accordingly
        sharedViewModel.selectedCity.observe(viewLifecycleOwner) { city ->
            // Update your UI using the new city information
            updateWeatherData(city)
        }
    }

}