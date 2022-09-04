package com.example.umbrella.viewmodel
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.umbrella.R
import com.example.umbrella.databinding.WeatherItemLayoutBinding
import com.example.umbrella.network.WeatherDaily

class WeatherItemAdapter(
    private val items: List<WeatherDaily>
) : RecyclerView.Adapter<WeatherItemAdapter.WeatherItemViewHolder>() {

    class WeatherItemViewHolder(private val binding: WeatherItemLayoutBinding): RecyclerView.ViewHolder(binding.root){

        fun bind(item: WeatherDaily){
            when {
                item.isColdest -> item.colorCode = R.color.cold_weather
                item.isWarmest -> item.colorCode = R.color.hot_weather
                else -> item.colorCode = R.color.black
            }

            binding.dailyWeather = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherItemViewHolder {
        return WeatherItemViewHolder(WeatherItemLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ))
    }

    override fun onBindViewHolder(holder: WeatherItemViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }
}