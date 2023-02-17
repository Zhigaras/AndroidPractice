package edu.skillbox.m15_room

import androidx.room.Room
import androidx.room.withTransaction
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import edu.skillbox.m15_room.database.AppDatabase
import edu.skillbox.m15_room.database.Word
import edu.skillbox.m15_room.database.WordDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class WordDaoTest {
    
    private lateinit var database: AppDatabase
    private lateinit var wordDao: WordDao
    
    @Before
    fun setupDatabase() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).allowMainThreadQueries().build()
        
        wordDao = database.wordDao()
    }
    
    @After
    fun closeDatabase() {
        database.close()
    }
    
    @Test
    fun insetWord_insertingCorrectWord() {
        val insertedWord = Word("Test word", 1)
        var foundedWord: Word?
        
        runBlocking {
            wordDao.insert(insertedWord)
            foundedWord = wordDao.findWordCounter(insertedWord.word)
        }
        
        assert(foundedWord!! == insertedWord)
    }
    
    @Test
    fun incrementCounter_incrementsByOne() {
        val wordToIncrement = Word("Test word", 0)
        val incrementedWord: Word?
        runBlocking {
            wordDao.insert(wordToIncrement)
            wordDao.incrementCounter(wordToIncrement.word)
            incrementedWord = wordDao.findWordCounter(wordToIncrement.word)
        }
        assert(incrementedWord?.counter == wordToIncrement.counter + 1)
    }
    
    @Test
    fun observeDatabaseHead_containsInsertedWord() = runBlocking {
        val insertedWord = Word("Test word", 1)
        wordDao.insert(insertedWord)
        
        assert(wordDao.showDictionaryHead(5).first().contains(insertedWord))
    }
    
    @Test
    fun observeDatabaseHead_headSizeIsCorrect() = runBlocking {
        val headSize = 5
        (1..headSize + 1).forEach {
            wordDao.insert(Word("Word #$it", it))
        }
        assert(wordDao.showDictionaryHead(headSize).first().size == headSize)
        
    }
    
    @Test
    fun clearDatabase_deletesAllItemsFromDatabase() = runBlocking {
        val firstWord = Word("First word", 0)
        val secondWord = Word("Second word", 0)
        database.withTransaction {
            wordDao.insert(firstWord)
            wordDao.insert(secondWord)
            wordDao.clear()
        }
        assert(wordDao.showDictionaryHead(5).first() == emptyList<Word>())
    }
}