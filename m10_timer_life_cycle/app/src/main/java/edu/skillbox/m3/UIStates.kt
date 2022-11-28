package edu.skillbox.m3

import java.io.Serializable

sealed class UIStates : Serializable {

    object Waiting : UIStates()

    object Progress : UIStates()
}
