package com.example.background_works

import android.content.Context
import android.util.Log
import androidx.work.*
import org.shredzone.commons.suncalc.SunTimes
import java.time.LocalDate
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
    
    private var dateInMillis = 0L
    private var delayTimeInMillis = 0L
    
    override fun doWork(): Result {
        val latitude = inputData.getDouble(KEY_LAT, 0.0)
        val longitude = inputData.getDouble(KEY_LON, 0.0)
        dateInMillis = inputData.getLong(KEY_DATE, 0L)
        delayTimeInMillis = inputData.getLong(KEY_TIME, 0L)
        
        val riseTime = calculateSunriseTime(latitude, longitude).rise
        Log.d("AAA2", "rise = $riseTime")
        val riseTimeInMillis = riseTime?.toEpochSecond()?.times(1_000)
        Log.d("AAA2", "riseInMillis = $riseTimeInMillis")
        Log.d("AAA2", "delayInMillis = $delayTimeInMillis")
        val alarmTime = riseTimeInMillis?.plus(delayTimeInMillis)
        Log.d("AAA2", "alarm = $alarmTime")
        val outputData = workDataOf(WORK_OUTPUT_KEY to alarmTime)
        
        return Result.success(outputData)
    }
    
    private fun calculateSunriseTime(latitude: Double, longitude: Double): SunTimes =
         SunTimes.compute()
            .on(LocalDate.ofEpochDay(dateInMillis / 86_400_000))
            .at(latitude, longitude)
            .execute()
}