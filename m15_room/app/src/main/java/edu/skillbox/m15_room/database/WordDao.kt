package edu.skillbox.m15_room.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface WordDao {

    @Insert
    suspend fun insert(word: Word)

    @Query("UPDATE dictionary SET counter = counter + 1 WHERE word LIKE :currentWord")
    suspend fun incrementCounter(currentWord: String)

    @Query("SELECT * FROM dictionary ORDER BY counter DESC LIMIT :headSize")
    fun showDictionaryHead(headSize: Int): Flow<List<Word>>

    @Query("SELECT * FROM dictionary WHERE word LIKE :searchWord")
    suspend fun findWordCounter(searchWord: String): Word?


    @Query("DELETE FROM dictionary")
    suspend fun clear()
}