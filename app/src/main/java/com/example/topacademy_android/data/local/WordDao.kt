package com.example.topacademy_android.data.local

import androidx.room.*

@Dao
interface WordDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordEntity)

    @Query("SELECT * FROM favorite_words")
    suspend fun getAllFavorites(): List<WordEntity>

    @Delete
    suspend fun deleteWord(word: WordEntity)
}
