package com.zhigaras.dependencyinjection

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import org.koin.android.ext.android.get
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    
    @Inject
    lateinit var daggerBicycleFactory: BicycleFactory
    
    override fun onCreate(savedInstanceState: Bundle?) {
    
        (application as App).daggerComponent.inject(this)
        
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val daggerButton = findViewById<Button>(R.id.dagger_button)
        
        daggerButton.setOnClickListener {
            val bicycle = daggerBicycleFactory.produceBicycle(Logo.BMX, Color.BLUE)
            Toast.makeText(this, bicycle.toString(), Toast.LENGTH_LONG).show()
        }
        val koinButton = findViewById<Button>(R.id.koin_button)
        
        koinButton.setOnClickListener {
            val koinBicycleFactory = get<BicycleFactory>()
            val bicycle = koinBicycleFactory.produceBicycle(Logo.BURTON, Color.GREEN)
            Toast.makeText(this, bicycle.toString(), Toast.LENGTH_LONG).show()
        }
        
    }
}