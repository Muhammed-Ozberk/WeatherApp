package com.example.havadurumu.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.havadurumu.R
import com.example.havadurumu.data.WeatherData
import com.squareup.picasso.Picasso

class WeatherAdapter(var mContext: Context, var weatherList: ArrayList<WeatherData>) :
    RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    inner class WeatherViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var day: TextView? = null
        var description: TextView? = null
        var degree: TextView? = null
        var night: TextView? = null
        var image: ImageView? = null

        init {
            day = view.findViewById(R.id.textViewGun)
            description = view.findViewById(R.id.textViewDurum)
            degree = view.findViewById(R.id.textViewDereceGunduz)
            night = view.findViewById(R.id.textViewDereceGece)
            image = view.findViewById(R.id.imageView)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view =
            LayoutInflater.from(mContext).inflate(R.layout.weather_card_view, parent, false)
        return WeatherViewHolder(view)
    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = weatherList[position]

        holder.day?.text = weather.day
        holder.description?.text = weather.description.toLowerCase().capitalize()
        holder.degree?.text = weather.degree.split(".")[0] + "°"
        holder.night?.text = weather.night.split(".")[0] + "°"
        Picasso.get().load(weather.icon).into(holder.image)
    }
}