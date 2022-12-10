package edu.skillbox.m15_room.ui.main

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.skillbox.m15_room.database.Word
import edu.skillbox.m15_room.database.WordDao
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val regexPattern = """^[A-Za-zа-яА-Я]+(-?[A-Za-zа-яА-Я]+){0,2}"""

class MainViewModel(private val wordDao: WordDao) : ViewModel() {

    private val _searchChannel = Channel<String>()
    val searchChannel = _searchChannel.receiveAsFlow()

    val errorFlow: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val dictFlow = this.wordDao.showDictionaryHead(5)

    fun onAddButton(inputtedText: String) {
        viewModelScope.launch {
            try {
                wordDao.insert(
                    Word(
                        word = inputtedText,
                        counter = 1,
                    )
                )
            } catch (_: SQLiteConstraintException) {
                wordDao.incrementCounter(inputtedText)
            }
        }
    }

    fun onClearButton() {
        viewModelScope.launch {
            wordDao.clear()
        }
    }

    fun onFindButton(inputtedText: String) {
        viewModelScope.launch {
            val searchResult = wordDao.findWordCounter(inputtedText)
            if (searchResult != null) _searchChannel.send(searchResult.toString())
            else _searchChannel.send("Unknown word")
        }
    }

    fun validateInput(input: CharSequence): Boolean {
        val isValid = input.matches(regexPattern.toRegex())
        if (isValid) errorFlow.value = null
        else {
            if (input.isEmpty()) errorFlow.value = null
            else errorFlow.value = "Incorrect input"
        }
        return isValid
    }
}