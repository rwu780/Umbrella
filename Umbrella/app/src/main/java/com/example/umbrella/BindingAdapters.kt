package com.example.umbrella

import android.graphics.Color
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.umbrella.network.WeatherDaily
import com.example.umbrella.network.WeatherItem
import com.example.umbrella.viewmodel.WeatherListAdapter
import com.squareup.picasso.Picasso


@BindingAdapter("listWeather")
fun bindWeatherRecyclerView(recyclerView: RecyclerView, data: List<WeatherDaily>?){
    val adapter = recyclerView.adapter as WeatherListAdapter
    adapter.updateItems(data)
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?){

    imgUrl?.let{
        Picasso.get().load(imgUrl)
            .placeholder(R.drawable.ic_baseline_downloading_24)
            .error(R.drawable.ic_baseline_running_with_errors_24)
            .into(imgView)
    }

}