package com.example.topacademy_android.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.topacademy_android.databinding.ActivityFavoritesBinding
import com.example.topacademy_android.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoritesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoritesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvFavorites.layoutManager = LinearLayoutManager(this)

        val db = AppDatabase.getInstance(this)

        lifecycleScope.launch(Dispatchers.IO) {
            val list = db.wordDao().getAllFavorites()
            withContext(Dispatchers.Main) {
                binding.rvFavorites.adapter = FavoritesAdapter(list)
            }
        }
    }
}
