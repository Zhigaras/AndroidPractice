package edu.skillbox.m3

sealed class StartStopButtonState {

    object StartButton : StartStopButtonState()

    object StopButton : StartStopButtonState()
}
