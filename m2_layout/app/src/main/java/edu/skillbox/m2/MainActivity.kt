package edu.skillbox.m2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import edu.skillbox.m2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customView.setUpperStringText("Upper string set up from main code.")
        binding.customView.setLowerStringText("Lower string set up from main code.")
    }
}