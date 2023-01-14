package com.zhigaras.dependencyinjection

import javax.inject.Inject

class BicycleFactory @Inject constructor(val frameFactory: FrameFactory, val wheelsDealer: WheelsDealer) {
    
    fun produceBicycle(logo: Logo, color: Int): Bicycle {
        val frame = frameFactory.produceFrame(color)
        val wheels = Wheels(wheelsDealer.produceWheel(), wheelsDealer.produceWheel())
        return Bicycle(frame, logo, wheels)
    }
}
