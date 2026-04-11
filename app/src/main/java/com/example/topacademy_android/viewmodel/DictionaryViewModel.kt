package com.example.topacademy_android.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.topacademy_android.data.DictionaryApi
import com.example.topacademy_android.data.WordResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DictionaryViewModel : ViewModel() {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.dictionaryapi.dev")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val api = retrofit.create(DictionaryApi::class.java)

    val wordData = MutableLiveData<WordResponse?>()
    val isLoading = MutableLiveData<Boolean>()
    val error = MutableLiveData<String?>()

    private var searchJob: Job? = null

    fun search(word: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(600)
            isLoading.value = true
            try {
                val response = api.getDefinition(word)
                wordData.value = response.firstOrNull()
                error.value = null
            } catch (e: Exception) {
                wordData.value = null
                error.value = "Not found"
            } finally {
                isLoading.value = false
            }
        }
    }
}
