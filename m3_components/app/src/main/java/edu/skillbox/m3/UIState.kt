package edu.skillbox.m3

sealed class UIState {

    object Waiting : UIState()

    object Progress : UIState()
}
