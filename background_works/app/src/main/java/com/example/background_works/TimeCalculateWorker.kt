package com.example.background_works

import android.content.Context
import androidx.work.*
import org.shredzone.commons.suncalc.SunTimes
import java.util.*
import java.util.concurrent.TimeUnit

class TimeCalculateWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    
    companion object {
        
        private const val WORK_TAG = "timeCalculation"
        const val UNIQUE_WORK_NAME = "calculationWork"
        const val WORK_OUTPUT_KEY = "timeCalcOutputData"
        
        fun createWorkRequest(inputData: Data): OneTimeWorkRequest {
            val batteryConstraint = Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .build()
            return OneTimeWorkRequestBuilder<TimeCalculateWorker>()
                .setConstraints(batteryConstraint)
                .setInputData(inputData)
                .setBackoffCriteria(BackoffPolicy.LINEAR, 5, TimeUnit.SECONDS)
                .addTag(WORK_TAG)
                .build()
        }
    }
    
    private val calendar = Calendar.getInstance()
    
    override fun doWork(): Result {
        val latitude = inputData.getDouble(KEY_LAT, 0.0)
        val longitude = inputData.getDouble(KEY_LON, 0.0)
        calendar.timeInMillis = inputData.getLong(KEY_CALENDAR, 0L)
        
        val riseTime = calculateSunriseTime(latitude, longitude).rise
            .toString()
        val delayInMillis = transformToMillis(riseTime)
        val alarmTime = calendar.timeInMillis + delayInMillis
        val outputData = workDataOf(WORK_OUTPUT_KEY to alarmTime)
        
        return Result.success(outputData)
    }
    
    //Transform SunTime to millis
    fun transformToMillis(sunriseTime: String): Long {
        val splitTime = sunriseTime.substringAfter("T")
            .takeWhile { it != '+' }
            .split(":")
            .map { it.toInt() }
        return (splitTime[2] * 1_000 + splitTime[1] * 60_000 + splitTime[0] * 3_600_000).toLong()
    }
    
    private fun calculateSunriseTime(latitude: Double, longitude: Double): SunTimes =
        SunTimes.compute()
            .on(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH)
            )
            .at(latitude, longitude)
            .execute()
}