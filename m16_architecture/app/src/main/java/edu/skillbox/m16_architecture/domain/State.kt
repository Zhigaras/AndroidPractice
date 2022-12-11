package edu.skillbox.m16_architecture.domain

sealed class State {
    object Progress : State()
    object Success : State()
    data class Error(val message: String = "Some error"): State()
}
