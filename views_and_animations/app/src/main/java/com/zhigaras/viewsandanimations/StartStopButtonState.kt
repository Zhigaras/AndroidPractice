package com.zhigaras.viewsandanimations

sealed class StartStopButtonState(open val text: String) {
    data class Running(override val text: String = "Stop") : StartStopButtonState(text)
    data class Waiting(override val text: String = "Start") : StartStopButtonState(text)
}
