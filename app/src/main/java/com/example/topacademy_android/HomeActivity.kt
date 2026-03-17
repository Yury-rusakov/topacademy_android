package com.example.topacademy_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnCalc = findViewById<Button>(R.id.btnCalc)
        val btnList = findViewById<Button>(R.id.btnList)
        val btnWeather = findViewById<Button>(R.id.btnWeather)

        btnCalc.setOnClickListener {
            val intent = Intent(this, CalcActivity::class.java)
            startActivity(intent)
        }

        btnList.setOnClickListener {

        }

        btnWeather.setOnClickListener {

        }
    }
}