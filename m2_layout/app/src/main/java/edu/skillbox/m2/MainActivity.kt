package edu.skillbox.m2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.skillbox.m2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.customView.setText(binding.customView.binding.upperString, "Upper string set up from Main code.")
        binding.customView.setText(binding.customView.binding.lowerString, "Lower string set up from Main code.")

    }
}