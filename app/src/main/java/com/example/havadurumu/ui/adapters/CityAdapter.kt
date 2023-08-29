package com.example.havadurumu.ui.adapters

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.example.havadurumu.R
import com.example.havadurumu.databinding.ActivityMainBinding
import com.example.havadurumu.ui.viewModel.SharedViewModel

class CityAdapter(private val mContext: Context, private val cities: List<String>,private val binding:ActivityMainBinding,private val sharedViewModel: SharedViewModel,private val menu: Menu,private val searchView: SearchView) : RecyclerView.Adapter<CityAdapter.CityViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
        return CityViewHolder(view)
    }

    override fun onBindViewHolder(holder: CityViewHolder, position: Int) {
        holder.bind(cities[position])
    }

    override fun getItemCount(): Int {
        return cities.size
    }

    inner class CityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(android.R.id.text1)

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        fun bind(city: String) {
            textView.text = city
            textView.gravity = android.view.Gravity.CENTER

            // Öğeye tıklama işlemini burada tanımla
            itemView.setOnClickListener {
                // Öğeyi yerel depolamaya kaydet
                val sharedPreferences = mContext.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                sharedPreferences.edit().putString("selectedCity", city.toLowerCase()).apply()

                sharedViewModel.updateSelectedCity(city.toLowerCase())

                binding.toolbarTitle.text = city.toLowerCase().capitalize()

                // Arama çubuğunu kapat
                searchView.setQuery("", false)
                searchView.isIconified = true
                menu.findItem(R.id.action_search).collapseActionView()


                binding.citesRv.visibility = View.GONE
                binding.tabs.visibility = View.VISIBLE
                binding.viewPager.visibility = View.VISIBLE

            }
        }
    }
}
