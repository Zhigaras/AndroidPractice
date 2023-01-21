package com.example.background_works

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.*
import kotlinx.coroutines.launch
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*

const val KEY_LAT = "keyLatitude"
const val KEY_LON = "keyLongitude"
const val KEY_DATE = "keqDate"
const val KEY_TIME = "keqTime"
private const val CHANNEL_ID: String = "id_of_channel"

class MainViewModel(private val app: Application) : AndroidViewModel(app) {
    
    private var dateInMillis = 0L
    private var delayInMillis = 0L
    
    private val notificationProvider = object : NotificationProvider {}
    private lateinit var timeCalculationRequest: OneTimeWorkRequest
    
    fun saveDate(dateInMillis: Long) {
        this.dateInMillis = dateInMillis
    }
    
    fun saveTime(hour: Int, minute: Int) {
        delayInMillis = timeToMillis(hour, minute)
    }
    
    private fun timeToMillis(hour: Int, minute: Int): Long {
        return (minute * 60_000 + hour * 3_600_000).toLong()
    }
    
    fun startCalculation(location: Location) {
        timeCalculationRequest =
            TimeCalculateWorker.createWorkRequest(createWorkerInputData(location))
        launchWorker()
    }
    
    private fun launchWorker(): Operation {
        return WorkManager.getInstance(app)
            .beginUniqueWork(
                TimeCalculateWorker.UNIQUE_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                timeCalculationRequest
            )
            .enqueue()
    }
    
    private fun createWorkerInputData(location: Location): Data {
        return Data.Builder().apply {
            putDouble(KEY_LAT, location.latitude)
            putDouble(KEY_LON, location.longitude)
            putLong(KEY_DATE, dateInMillis)
            putLong(KEY_TIME, delayInMillis)
        }.build()
    }
    
    fun observeWorkerResult() {
        viewModelScope.launch {
            WorkManager.getInstance(app)
                .getWorkInfoByIdLiveData(timeCalculationRequest.id)
                .asFlow()
                .collect { workInfo ->
                    if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                        val alarmTime = workInfo.outputData
                            .getLong(TimeCalculateWorker.WORK_OUTPUT_KEY, 0L)
                        
                        val notificationText = buildString {
                            append(app.getString(R.string.alarm_set_to))
                            append(" ")
                            append(alarmTime.convertToDateFormat())
                        }
                        notificationProvider.makeChannelAndNotify(
                            app,
                            CHANNEL_ID,
                            notificationText,
                            app.getString(R.string.notify_channel_name),
                            app.getString(R.string.notify_channel_description)
                        )
                        createBackgroundAlarm(alarmTime)
                    }
                }
        }
    }
    
    private fun Long.convertToDateFormat(): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm", Locale.getDefault())
        val instant = Instant.ofEpochSecond(this/1_000)
        Log.d("AAA2", instant.toString())
        val zid = ZoneId.systemDefault()
        Log.d("AAA2", zid.toString())
        return LocalDateTime.ofInstant(instant, zid).format(formatter)
        
        
    }
    
    private fun createBackgroundAlarm(alarmTime: Long) {
        val alarmManager = getSystemService(app, AlarmManager::class.java)!!
//        val alarmTimeAtUTC = calculateTimeForLaunch()
        val alarmType = AlarmManager.RTC_WAKEUP
        val intent = Intent(app, AlarmReceiver::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getBroadcast(
            app,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.setExactAndAllowWhileIdle(
            alarmType,
            alarmTime,
            pendingIntent
        )
    }
    
    private fun calculateTimeForLaunch(): Long {
        return System.currentTimeMillis() + 10 * 1_000L
    }
}