package com.zhigaras.dependencyinjection

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.emptyParametersHolder
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    
    private var koinBicycleFactory = object : KoinComponent {
        val value: BicycleFactory by inject()
    }.value
    
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val daggerButton = findViewById<Button>(R.id.dagger_button)
        
        daggerButton.setOnClickListener {
            val daggerBicycleFactory = (application as App).daggerComponent.bicycleFactory()
            val bicycle = daggerBicycleFactory.produceBicycle(Logo.BMX, Color.BLUE)
            Toast.makeText(this, bicycle.toString(), Toast.LENGTH_LONG).show()
        }
        val koinButton = findViewById<Button>(R.id.koin_button)
        
        koinButton.setOnClickListener {
            val bicycle = koinBicycleFactory.produceBicycle(Logo.BURTON, Color.GREEN)
            Toast.makeText(this, bicycle.toString(), Toast.LENGTH_LONG).show()
        }
    }
}