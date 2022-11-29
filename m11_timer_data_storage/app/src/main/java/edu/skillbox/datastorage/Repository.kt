package edu.skillbox.datastorage

import android.content.Context
import android.content.Context.MODE_PRIVATE

private const val TEST_PREFS = "testPrefs"
private const val KEY_TEXT = "text"

class Repository(context: Context) {

    private var localeVariable: String? = null
    private val prefs = context.getSharedPreferences(TEST_PREFS, MODE_PRIVATE)

    fun saveText(text: String) {
        localeVariable = text
        prefs.edit().putString(KEY_TEXT, text).apply()
    }

    fun clearText() {
        localeVariable = null
        prefs.edit().clear().apply()
    }

    fun getText(): String {
        return when {
            getDataFromLocalVariable() != null -> getDataFromLocalVariable()!!
            getDataFromSharedPreference() != null -> getDataFromSharedPreference()!!
            else -> ""
        }
    }

    private fun getDataFromSharedPreference(): String? {
        return prefs.getString(KEY_TEXT, null)
    }

    private fun getDataFromLocalVariable(): String? {
        return localeVariable
    }
}