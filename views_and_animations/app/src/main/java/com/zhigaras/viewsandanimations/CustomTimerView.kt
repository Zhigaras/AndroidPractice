package com.zhigaras.viewsandanimations

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Rect
import android.icu.util.Calendar
import android.icu.util.TimeZone
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewTreeViewModelStoreOwner
import androidx.lifecycle.get
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class CustomTimerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    
    private var centerX = 0F
    private var centerY = 0F
    private var clockRadius = 0F
    private val paint = Paint()
    private val rect = Rect()
    private val scope = CoroutineScope(Dispatchers.Main.immediate)
    private val listenersList = mutableListOf<(TimerState) -> Unit>()
    
    private val viewModel by lazy {
        ViewModelProvider(ViewTreeViewModelStoreOwner.get(this)!!).get<MainViewModel>()
    }
    
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        centerX = (height / 2).toFloat()
        centerY = (width / 2).toFloat()
        clockRadius = (minOf(height, width) * 0.4).toFloat()
        
        scope.launch {
            viewModel.timerFlow.collect { timerState ->
                listenersList.forEach { listener -> listener(timerState) }
                invalidate()
                drawCircle(canvas)
                drawCenter(canvas)
                drawNumbers(canvas)
                drawHands(canvas, timerState.time)
            }
        }
    }
    
    fun start() {
        viewModel.startCount()
    }
    
    fun stop() {
        viewModel.stopCount()
    }
    
    fun reset() {
        viewModel.resetCount()
    }
    
    fun currentTime(): Long {
        return viewModel.timerFlow.value.time
    }
    
    fun addUpdateListener(listener: (TimerState) -> Unit) {
        listenersList.add(listener)
    }
    
    fun removeUpdateListener(listener: (TimerState) -> Unit) {
        listenersList.remove(listener)
    }
    
    private fun drawHand(canvas: Canvas?, handType: ClockHands, timeValue: Float) {
        paint.apply {
            color = handType.color
            strokeWidth = handType.handWidth
        }
        val x =
            (centerX + handType.relativeLength * clockRadius * sin(timeValue * 2 * PI / handType.turnSize))
        val y =
            (centerY - handType.relativeLength * clockRadius * cos(timeValue * 2 * PI / handType.turnSize))
        canvas?.drawLine(centerX, centerY, x.toFloat(), y.toFloat(), paint)
    }
    
    private fun drawHands(canvas: Canvas?, timeInMillis: Long) {
        val calendar = Calendar.getInstance()
        calendar.timeZone = TimeZone.GMT_ZONE
        calendar.timeInMillis = timeInMillis
        val milliseconds = calendar.get(Calendar.MILLISECOND)
        val seconds: Float = calendar.get(Calendar.SECOND).toFloat() + milliseconds.toFloat() / 1000
        val minutes: Float = calendar.get(Calendar.MINUTE).toFloat()
        var hours: Float = calendar.get(Calendar.HOUR) + minutes / 60
        if (hours > 12) hours -= 12
        Log.d("draw Hands", seconds.toString())
        val hands = listOf(ClockHands.SECOND, ClockHands.MINUTE, ClockHands.HOUR)
        val timeValues = listOf(seconds, minutes, hours)
        hands.zip(timeValues).forEach { (hand, timeValue) ->
            drawHand(canvas, hand, timeValue)
        }
    }
    
    private fun drawNumbers(canvas: Canvas?) {
        paint.apply {
            textSize = 40F
            strokeWidth = 1F
        }
        for (i in 1..12) {
            val iString = i.toString()
            paint.getTextBounds(iString, 0, iString.length, rect)
            val x = centerX + 0.85 * clockRadius * sin(i * 30F * PI / 180) - rect.width() / 2
            val y = centerY - 0.85 * clockRadius * cos(i * 30F * PI / 180) + rect.height() / 2
            canvas?.drawText(i.toString(), x.toFloat(), y.toFloat(), paint)
        }
    }
    
    private fun drawCircle(canvas: Canvas?) {
        paint.apply {
            style = Style.STROKE
            color = Color.BLACK
            strokeWidth = 5F
        }
        canvas?.drawCircle(centerX, centerY, clockRadius, paint)
    }
    
    private fun drawCenter(canvas: Canvas?) {
        paint.apply {
            color = Color.BLACK
            style = Style.FILL_AND_STROKE
        }
        canvas?.drawCircle(centerX, centerY, 3F, paint)
    }
}