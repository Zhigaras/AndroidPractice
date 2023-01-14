package com.zhigaras.dependencyinjection

import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class WheelsDealer @Inject constructor() {
    
    fun produceWheel(): Wheel {
        return Wheel(getWheelSerialNumber())
    }
    
    private fun getWheelSerialNumber(): String = buildString {
        append("WH")
        append(Random.nextInt(1_000_000, 9_999_999))
    }
}