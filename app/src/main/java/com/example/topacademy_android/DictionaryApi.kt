package com.example.topacademy_android

import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface DictionaryApi {
    @GET("api/v2/entries/en/{word}")
    suspend fun getDefinition(@Path("word") word: String): List<WordResponse>
}
