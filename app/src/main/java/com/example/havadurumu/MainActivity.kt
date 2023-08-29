package com.example.havadurumu

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.havadurumu.databinding.ActivityMainBinding
import com.example.havadurumu.ui.adapters.CityAdapter
import com.example.havadurumu.ui.fragments.TodayFragment
import com.example.havadurumu.ui.fragments.WeeklyFragment
import com.example.havadurumu.ui.adapters.ViewPagerAdapter
import com.example.havadurumu.ui.fragments.TomorrowFragment
import com.example.havadurumu.ui.viewModel.SharedViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    private lateinit var binding: ActivityMainBinding
    private val turkishCities = arrayOf(
        "Adana",
        "Adıyaman",
        "Afyon",
        "Ağrı",
        "Amasya",
        "Ankara",
        "Antalya",
        "Artvin",
        "Aydın",
        "Balıkesir",
        "Bilecik",
        "Bingöl",
        "Bitlis",
        "Bolu",
        "Burdur",
        "Bursa",
        "Çanakkale",
        "Çankırı",
        "Çorum",
        "Denizli",
        "Diyarbakır",
        "Edirne",
        "Elazığ",
        "Erzincan",
        "Erzurum",
        "Eskişehir",
        "Gaziantep",
        "Giresun",
        "Gümüşhane",
        "Hakkari",
        "Hatay",
        "Isparta",
        "Mersin",
        "İstanbul",
        "İzmir",
        "Kars",
        "Kastamonu",
        "Kayseri",
        "Kırklareli",
        "Kırşehir",
        "Kocaeli",
        "Konya",
        "Kütahya",
        "Malatya",
        "Manisa",
        "Kahramanmaraş",
        "Mardin",
        "Muğla",
        "Muş",
        "Nevşehir",
        "Niğde",
        "Ordu",
        "Rize",
        "Sakarya",
        "Samsun",
        "Siirt",
        "Sinop",
        "Sivas",
        "Tekirdağ",
        "Tokat",
        "Trabzon",
        "Tunceli",
        "Şanlıurfa",
        "Uşak",
        "Van",
        "Yozgat",
        "Zonguldak",
        "Aksaray",
        "Bayburt",
        "Karaman",
        "Kırıkkale",
        "Batman",
        "Şırnak",
        "Bartın",
        "Ardahan",
        "Iğdır",
        "Yalova",
        "Karabük",
        "Kilis",
        "Osmaniye",
        "Düzce",
    )
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var menu: Menu
    private lateinit var searchView: SearchView
    private lateinit var fusedLocationClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val savedCity = sharedPreferences.getString("selectedCity", null)

        if (savedCity != null) {
            binding.toolbarTitle.text = savedCity.toLowerCase().capitalize()
        }

        setSupportActionBar(binding.toolbar)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (checkLocationPermission()) {
            // İzin verilmişse konum bilgisini alma işlemini başlat
            getLastLocation()
        } else {
            // İzin verilmemişse izin isteği göster
            requestLocationPermission()
        }


        val fragmentList = arrayListOf(
            TodayFragment(),
            TomorrowFragment(),
            WeeklyFragment()
        )

        val adapter = ViewPagerAdapter(this, fragmentList)
        binding.viewPager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "BUGÜN"
                1 -> "YARIN"
                else -> "7 GÜN"
            }
        }.attach()

    }


//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setOnQueryTextListener(this)

        this.menu = menu
        this.searchView = searchView

        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        // Filtreleme işlemini yap
        val filteredCities = turkishCities.filter {
            it.contains(newText.orEmpty(), ignoreCase = true)
        }

        // RecyclerView'ı bul
        val cityRecyclerView = binding.citesRv

        // Eğer metin varsa RecyclerView'ı görünür yap, yoksa gizle
        if (newText.isNullOrEmpty()) {
            cityRecyclerView.visibility = View.GONE
        } else {
            binding.tabs.visibility = View.GONE
            binding.viewPager.visibility = View.GONE
            cityRecyclerView.visibility = View.VISIBLE
        }

        // RecyclerView için özel adaptörü oluşturun ve atayın
        val cityAdapter = CityAdapter(this,filteredCities,binding,sharedViewModel,menu,searchView)
        cityRecyclerView.adapter = cityAdapter

        return true
    }

    private fun checkLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
            REQUEST_LOCATION_PERMISSION
        )
    }

    private fun getLastLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        // Konum bilgisini kullan
                        val latitude = location.latitude
                        val longitude = location.longitude
                        Log.e("MainActivity", "getLastLocation: $latitude, $longitude")
                        // ...
                    }
                }
        } else {
            requestLocationPermission()
        }
    }

    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 123
    }

}