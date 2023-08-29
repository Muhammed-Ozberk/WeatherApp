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
import com.example.havadurumu.databinding.FragmentTomorrowBinding
import com.example.havadurumu.ui.viewModel.SharedViewModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TomorrowFragment : Fragment() {
    private lateinit var binding: FragmentTomorrowBinding
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentTomorrowBinding.inflate(inflater, container, false)

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
            if (weatherData.isNotEmpty()){
                activity?.runOnUiThread {
                    Picasso.get().load(weatherData[1].icon).into(binding.imageViewTomorrow)
                    binding.textViewTomorrowDegree.text = weatherData[1].degree.split(".")[0] + "°C"
                    binding.textViewTomorrowDescription.text = weatherData[1].description.toLowerCase().capitalize()
                    binding.textViewTomorrowDegreeRange.text =
                        weatherData[1].max.split(".")[0] + "° / " + weatherData[1].night.split(".")[0] + "°"
                    binding.textViewTomorrowHumidity.text = "Nem : " + weatherData[1].humidity + "%"
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