package com.example.background_works

import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.work.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

const val KEY_LAT = "keyLatitude"
const val KEY_LON = "keyLongitude"
const val KEY_CALENDAR = "keyCalendar"
private const val CHANNEL_ID: String = "id_of_channel"

class MainViewModel(private val app: Application) : AndroidViewModel(app) {
    
    private var date = 0L
    private var time = 0L
    
    private val notificationProvider = object : NotificationProvider {}
    private lateinit var timeCalculationRequest: OneTimeWorkRequest
    
    private val calendar: Calendar = Calendar.getInstance()
    
    private val _calendarFlow = MutableStateFlow<Calendar?>(null)
    val calendarFlow = _calendarFlow.asStateFlow()
    
    private fun shareCalendar() {
        calendar.timeInMillis = date + time
        viewModelScope.launch {
            _calendarFlow.value = calendar
        }
        Log.d("AAA", calendar.time.toString())
    }
    
    fun saveDate(timeInMillis: Long) {
        date = timeInMillis
        shareCalendar()
    }
    
    fun saveTime(hour: Int, minute: Int) {
        time = timeToMillis(hour, minute)
        shareCalendar()
    }
    
    private fun timeToMillis(hour: Int, minute: Int): Long {
        val timeZoneOffset = calendar.timeZone.rawOffset
        return (minute * 60_000 + hour * 3_600_000 - timeZoneOffset).toLong()
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
            putLong(KEY_CALENDAR, calendar.timeInMillis)
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
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = this
        return SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).format(calendar.time)
    }
    
    private fun createBackgroundAlarm(alarmTime: Long) {
        val alarmManager = getSystemService(app, AlarmManager::class.java)!!
//        val alarmTimeAtUTC = calculateTimeForLaunch()
        val alarmType = AlarmManager.RTC_WAKEUP
        val intent = Intent(app, AlarmReceiver::class.java)
            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val pendingIntent = PendingIntent.getBroadcast(app, 0, intent, 0)
        alarmManager.setExactAndAllowWhileIdle(
            alarmType,
            alarmTime,
            pendingIntent
        )
    }
    
//    private fun calculateTimeForLaunch(): Long {
//        return System.currentTimeMillis() + 10 * 1_000L
//    }
}