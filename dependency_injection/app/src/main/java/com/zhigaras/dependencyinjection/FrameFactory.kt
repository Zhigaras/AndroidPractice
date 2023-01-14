package com.zhigaras.dependencyinjection

import javax.inject.Inject
import kotlin.random.Random

class FrameFactory @Inject constructor() {
    
    fun produceFrame(color: Int): Frame {
        return Frame(getFrameSerialNumber(), color)
    }
    
    private fun getFrameSerialNumber() = buildString {
        append("FR")
        append(Random.nextInt(1_000_000, 9_999_999))
    }
}