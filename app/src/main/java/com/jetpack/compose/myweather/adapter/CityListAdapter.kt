package com.jetpack.compose.myweather.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jetpack.compose.myweather.data.lib.City
import com.jetpack.compose.myweather.databinding.CardCityItemBinding
import com.jetpack.compose.myweather.ui.DetailCityWeatherActivity
import com.jetpack.compose.myweather.utils.Constanta

class CityListAdapter(private val listData: List<City>) :
    RecyclerView.Adapter<CityListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: CardCityItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardCityItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = listData[position]
        holder.binding.apply {
            ivBanner.setImageResource(city.image)
            tvCityName.text = city.name
            root.setOnClickListener {
                val intent = Intent(it.context, DetailCityWeatherActivity::class.java)
                intent.putExtra(Constanta.city, city)
                it.context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = listData.size
}

//class CityListAdapter(private val listData: ArrayList<City>) :
//    RecyclerView.Adapter<CityListAdapter.ViewHolder>() {
//    class ViewHolder(var binding: CardCityItemBinding) :
//        RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
//        ViewHolder(
//            CardCityItemBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        )
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        fun bind(data: City) {
//            with(holder.itemView) {
//                this.setOnClickListener {
//                    val intent = Intent(context, DetailCityWeatherActivity::class.java)
//                    intent.putExtra(Constanta.city, data)
//                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//                    context.startActivity(intent)
//                }
//                holder.binding.apply {
//                    this.ivBanner.setImageResource(data.image)
//                    this.tvCityName.text = data.name
//                }
//
//            }
//            bind(listData[position])
//        }
//    }
//
//    override fun getItemCount() = listData.size
//
//}