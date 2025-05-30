package com.uai.weatherappuai.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.uai.weatherappuai.R
import com.uai.weatherappuai.data.model.ForecastDay
import com.uai.weatherappuai.databinding.ItemForecastBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ForecastAdapter :
    ListAdapter<ForecastDay, ForecastAdapter.ViewHolder>(ForecastDiffCallback()) {

    private val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val outputFormat =
        SimpleDateFormat("EEE, MMM d", Locale.getDefault())

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemForecastBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val forecastItem = getItem(position)
        holder.bind(forecastItem)
    }

    inner class ViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(forecastDay: ForecastDay) {
            binding.apply {
                try {
                    val date = inputFormat.parse(forecastDay.date)
                    tvForecastDate.text =
                        date?.let { outputFormat.format(it) } ?: forecastDay.date
                } catch (e: Exception) {
                    tvForecastDate.text = forecastDay.date // Fallback
                }

                tvForecastTempMaxMin.text = itemView.context.getString(
                    R.string.temp_max_min_format,
                    forecastDay.dayDetails.maxTempC,
                    forecastDay.dayDetails.minTempC
                )

                tvForecastCondition.text = forecastDay.dayDetails.condition.text
                Glide.with(itemView.context)
                    .load("https:${forecastDay.dayDetails.condition.icon}") // WeatherAPI icon URLs often start with //
                    .placeholder(R.drawable.ic_weather_placeholder)
                    .error(R.drawable.ic_error_placeholder)
                    .into(ivForecastIcon)
            }
        }
    }

    class ForecastDiffCallback : DiffUtil.ItemCallback<ForecastDay>() {
        override fun areItemsTheSame(oldItem: ForecastDay, newItem: ForecastDay): Boolean {
            return oldItem.dateEpoch == newItem.dateEpoch
        }

        override fun areContentsTheSame(oldItem: ForecastDay, newItem: ForecastDay): Boolean {
            return oldItem == newItem
        }
    }
}