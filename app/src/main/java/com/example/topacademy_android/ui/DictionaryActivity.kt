package com.example.topacademy_android.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.lifecycleScope
import com.example.topacademy_android.R
import com.example.topacademy_android.data.DictionaryApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import com.example.topacademy_android.data.local.AppDatabase
import com.example.topacademy_android.data.local.WordEntity
import kotlinx.coroutines.Dispatchers

class DictionaryActivity : AppCompatActivity() {

    private var searchJob: Job? = null
    private lateinit var api: DictionaryApi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dictionary)

        // 1. Инициализация UI
        val etInput = findViewById<EditText>(R.id.etInput)
        val btnFavorite = findViewById<ImageButton>(R.id.btnFavorite)
        val tvResult = findViewById<TextView>(R.id.tvDefinition)
        val tvWord = findViewById<TextView>(R.id.tvWord)
        val tvPhonetic = findViewById<TextView>(R.id.tvPhonetic)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        val toolbar = findViewById<Toolbar>(R.id.toolbarDict)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { finish() }

        // 2. Настройка API (позже это уедет в DI или репозиторий)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.dictionaryapi.dev")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        api = retrofit.create(DictionaryApi::class.java)

        // 3. Динамический поиск
        etInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val word = s.toString().trim().lowercase()
                searchJob?.cancel()

                if (word.isEmpty()) {
                    progressBar.visibility = View.GONE
                    return
                }

                if (word.length > 2) {
                    searchJob = lifecycleScope.launch {
                        delay(600)

                        progressBar.visibility = View.VISIBLE

                        try {
                            val response = api.getDefinition(word)
                            if (response.isNotEmpty()) {
                                val data = response[0]
                                tvWord.text = data.word
                                tvPhonetic.text = data.phonetics?.firstOrNull { !it.text.isNullOrBlank() }?.text ?: ""
                                tvResult.text = data.meanings?.firstOrNull()?.definitions?.firstOrNull()?.definition

                                btnFavorite.visibility = View.VISIBLE
                            }
                        } catch (e: Exception) {
                            tvResult.text = "Word not found"
                            btnFavorite.visibility = View.GONE
                        } finally {
                            progressBar.visibility = View.GONE
                        }
                    }
                }
            }
        })

        // 4. Логика Room
        btnFavorite.setOnClickListener {
            val word = tvWord.text.toString()
            val def = tvResult.text.toString()

            val db = AppDatabase.getInstance(this)
            lifecycleScope.launch(Dispatchers.IO) {
                db.wordDao().insertWord(WordEntity(word, def, null))
            }
            Toast.makeText(this, "Сохранено в избранное!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dictionary_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_favorites -> {
                val intent = Intent(this, FavoritesActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}