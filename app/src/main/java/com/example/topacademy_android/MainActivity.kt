package com.example.topacademy_android
import com.example.topacademy_android.CarAdapter
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvCars = findViewById<RecyclerView>(R.id.rvCars)

        // Моковые данные
        val carList = listOf(
            Car("Tesla", "Model S", 2023, "Электрокар", 90000, R.drawable.tesla),
            Car("BMW", "M5", 2022, "Спортивный седан", 110000, R.drawable.bmw),
            Car("Lada", "Vesta", 2021, "Народный выбор", 15000, R.drawable.vesta)
        )

        rvCars.layoutManager = LinearLayoutManager(this)
        rvCars.adapter = CarAdapter(carList) { car ->
            val intent = Intent(this, DetailActivity::class.java)
            startActivity(intent)
        }
    }
}
