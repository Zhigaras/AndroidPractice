package com.zhigaras.viewsandanimations

import android.graphics.Color

enum class ClockHands(val color: Int, val relativeLength: Float, val handWidth: Float, val turnSize: Int) {
    SECOND(Color.RED, 0.8F, 5F, 60),
    MINUTE(Color.BLUE, 0.75F, 7F, 60),
    HOUR(Color.BLUE, 0.6F, 9F, 12)
}