package edu.skillbox.myfirstapplication

sealed class State {

    object Start : State()

    object Progress : State()

    object Finish : State()

    object Error : State()
}
