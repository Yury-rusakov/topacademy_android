package com.example.topacademy_android

import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DictionaryActivity : AppCompatActivity() {

    private var audioUrl: String? = null
    private var searchJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionary)

        val etInput = findViewById<EditText>(R.id.etInput)
        val btnPlay = findViewById<ImageButton>(R.id.btnPlay)
        val tvResult = findViewById<TextView>(R.id.tvDefinition)
        val tvWord = findViewById<TextView>(R.id.tvWord)
        val tvPhonetic = findViewById<TextView>(R.id.tvPhonetic)

        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarDict)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36")
                    .header("Accept", "application/json")
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.dictionaryapi.dev")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(DictionaryApi::class.java)

        etInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val word = s.toString().trim().lowercase()
                searchJob?.cancel()

                if (word.length > 2) {
                    tvResult.text = "Searching..."
                    searchJob = lifecycleScope.launch {
                        delay(700)
                        try {
                            val response = api.getDefinition(word)
                            if (response.isNotEmpty()) {
                                val data = response[0]

                                tvWord.text = data.word ?: word
                                tvPhonetic.text = data.phonetics?.firstOrNull { !it.text.isNullOrBlank() }?.text ?: ""

                                val firstDef = data.meanings?.firstOrNull()?.definitions?.firstOrNull()?.definition
                                tvResult.text = firstDef ?: "No definition found"

                                audioUrl = data.phonetics?.firstOrNull { !it.audio.isNullOrBlank() }?.audio
                                btnPlay.visibility = if (audioUrl.isNullOrEmpty()) View.GONE else View.VISIBLE
                            }
                        } catch (e: Exception) {
                            Log.e("Dict", "REAL ERROR: ", e)
                            tvResult.text = "Word not found"
                            btnPlay.visibility = View.GONE
                        }
                    }
                }
            }
        })

        btnPlay.setOnClickListener {
            audioUrl?.let { url ->
                try {
                    val finalUrl = if (url.startsWith("//")) "https:$url" else url
                    val mediaPlayer = MediaPlayer()
                    mediaPlayer.setDataSource(finalUrl)
                    mediaPlayer.prepareAsync()
                    mediaPlayer.setOnPreparedListener { it.start() }
                } catch (e: Exception) {
                    Toast.makeText(this, "Audio error", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
