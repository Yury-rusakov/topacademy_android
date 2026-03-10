package com.example.topacademy_android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.topacademy_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object{
        private const val ON_CREATE = "ON_CREATE"
    }

    private lateinit var binding: ActivityMainBinding

    private var startTime: Long = 0
    private val TAG = "Lifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startTime = System.currentTimeMillis()
        Log.d(TAG, "onCreate вызван")

        binding.button.setOnClickListener {
            binding.twTitle.text = getString(R.string.creatorName)
        }
    }

    override fun onStart() {
        super.onStart()
        val diff = System.currentTimeMillis() - startTime
        Log.d(TAG, "onStart вызван через $diff мс после onCreate")
    }

    override fun onResume() {
        super.onResume()
        val diff = System.currentTimeMillis() - startTime
        Log.d(TAG, "onResume вызван через $diff мс после onCreate")
    }

    override fun onPause() {
        super.onPause()
        val diff = System.currentTimeMillis() - startTime
        Log.d(TAG, "onPause вызван через $diff мс после onCreate")
    }

    override fun onStop() {
        super.onStop()
        val diff = System.currentTimeMillis() - startTime
        Log.d(TAG, "onStop вызван через $diff мс после onCreate")

    }

    override fun onRestart() {
        super.onRestart()
        val diff = System.currentTimeMillis() - startTime
        Log.d(TAG, "onRestart вызван через $diff мс после onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        val diff = System.currentTimeMillis() - startTime
        Log.d(TAG, "onDestroy вызван через $diff мс после onCreate")
    }
}