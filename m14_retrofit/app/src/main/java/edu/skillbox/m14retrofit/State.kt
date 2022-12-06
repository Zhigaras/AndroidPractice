package edu.skillbox.m14retrofit

sealed class State {
    object Progress : State()
    data class Success(val message: String = "Search complete") : State()
    data class Error(val message: String = "Some error"): State()
}
