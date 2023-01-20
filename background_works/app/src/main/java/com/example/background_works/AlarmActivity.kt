package com.example.background_works

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AlarmActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)
        
        val stopAlarmButton = findViewById<Button>(R.id.stop_alarm_button)
        
//        stopAlarmButton.setOnClickListener {
//            RingtoneManager(applicationContext).stopPreviousRingtone()
//        }
    }
}