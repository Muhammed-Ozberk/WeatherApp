package com.example.havadurumu.ui.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.havadurumu.R
import com.example.havadurumu.data.Repo
import com.example.havadurumu.databinding.FragmentTodayBinding
import com.example.havadurumu.ui.adapters.WeatherAdapter
import com.example.havadurumu.ui.viewModel.SharedViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TodayFragment : Fragment() {
    private lateinit var binding: FragmentTodayBinding
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTodayBinding.inflate(inflater, container, false)

        val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedCity = sharedPreferences?.getString("selectedCity", null)?.toLowerCase()
        Log.e("TodayFragment", "onCreateView: $savedCity")

        updateWeatherData(savedCity ?: "istanbul")

        return binding.root
    }

    private fun updateWeatherData(city: String) {
        val repo = Repo()
        GlobalScope.launch {
            val weatherData = repo.getWeatherData(city)
            if (weatherData.isNotEmpty()) {
                activity?.runOnUiThread {
                    Picasso.get().load(weatherData[0].icon).into(binding.imageViewToday)
                    binding.textViewTodayDegree.text = weatherData[0].degree.split(".")[0] + "°C"
                    binding.textViewTodayDescription.text =
                        weatherData[0].description.toLowerCase().capitalize()
                    binding.textViewTodayDegreeNight.text =
                        weatherData[0].max.split(".")[0] + "° / " + weatherData[0].night.split(".")[0] + "°"
                    binding.textViewTodayHumidity.text = "Nem : " + weatherData[0].humidity + "%"
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences = context?.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedCity = sharedPreferences?.getString("selectedCity", null)?.toLowerCase()
        Log.e("TodayFragment", "onViewCreated: $savedCity")

        // İlk açılışta ve şehir değişikliklerinde verileri güncelle
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Observe the selected city LiveData and update the UI accordingly
        sharedViewModel.selectedCity.observe(viewLifecycleOwner) { city ->
            // Update your UI using the new city information
            updateWeatherData(city)
        }
    }


}