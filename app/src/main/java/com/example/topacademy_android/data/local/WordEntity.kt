package com.example.topacademy_android.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_words")
data class WordEntity(
    @PrimaryKey val word: String,
    val definition: String,
    val phonetic: String?
)
