package edu.skillbox.m12mvvm.ui.main

sealed class State {
    object Progress : State()
    data class Success(val message: String = "Search result will be displayed here") : State()
    data class Error(val message: String): State()
}
