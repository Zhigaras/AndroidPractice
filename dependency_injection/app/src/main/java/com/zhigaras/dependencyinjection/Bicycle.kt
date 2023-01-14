package com.zhigaras.dependencyinjection

import android.graphics.Color
import android.os.Build
import androidx.annotation.RequiresApi

class Bicycle(
    val frame: Frame,
    val logo: Logo,
    val wheels: Wheels
) {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun toString(): String {
        return buildString {
            append("Bicycle $logo, color: ${frame.color}.\n")
            append("Frame SN: ${frame.serialNumber}.\n")
            append("Front wheel SN: ${wheels.frontWheel.serialNumber}.\n")
            append("Rear wheel SN: ${wheels.rearWheel.serialNumber}.")
        }
    }
}

class Frame (val serialNumber: String, val color: Int)

class Wheel(val serialNumber: String)

class Wheels(val frontWheel: Wheel, val rearWheel: Wheel)