package com.example.topacademy_android

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CarAdapter(private val carList: List<Car>, private val onClick: (Car) -> Unit) :
    RecyclerView.Adapter<CarAdapter.CarViewHolder>() {

    class CarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivCar: ImageView = view.findViewById(R.id.ivCar)
        val tvBrandModel: TextView = view.findViewById(R.id.tvBrandModel)
        val tvCost: TextView = view.findViewById(R.id.tvCost)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_car, parent, false)
        return CarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarViewHolder, position: Int) {
        val car = carList[position]
        holder.tvBrandModel.text = "${car.brand} ${car.model} (${car.year})"
        holder.tvCost.text = "Цена: ${car.cost} $"
        holder.ivCar.setImageResource(car.imageResId)

        holder.itemView.setOnClickListener { onClick(car) }
    }

    override fun getItemCount() = carList.size
}
