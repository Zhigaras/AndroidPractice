package edu.skillbox.m3

import android.content.ContentValues.TAG
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import edu.skillbox.m3.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private var counter = 10
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun refreshProgressBar(value: Float) {
            counter = value.toInt()
            binding.counter.text = counter.toString()
            binding.progressBar.max = counter
            binding.progressBar.setProgress(counter, true)
        }

        fun changeButtonStateTo(buttonState: StartStopButtonState): Unit = when (buttonState) {
            is StartStopButtonState.StartButton -> {
                binding.startStopButton.text = getText(R.string.start)
                binding.startStopButton.setTextColor(Color.GREEN)
            }

            is StartStopButtonState.StopButton -> {
                binding.startStopButton.text = getText(R.string.stop)
                binding.startStopButton.setTextColor(Color.RED)
            }
        }

        fun changeUIStateTo(UIState: UIStates): Unit = when (UIState) {
            is UIStates.Waiting -> {
                changeButtonStateTo(StartStopButtonState.StartButton)
                refreshProgressBar(binding.slider.value)
                binding.slider.isEnabled = true
                binding.slider.addOnChangeListener { _, value, _ ->
                    refreshProgressBar(value)
                }
                binding.startStopButton.setOnClickListener {
                    changeUIStateTo(UIStates.Progress)
                }
            }

            is UIStates.Progress -> {
                changeButtonStateTo(StartStopButtonState.StopButton)
                binding.slider.isEnabled = false
                val scope = CoroutineScope(Dispatchers.Main)
                val job = scope.launch {
                    while (counter > 0) {
                        delay(1000)
                        yield()
                        counter--
                        Log.d(TAG, "counter = $counter")
                        binding.progressBar.progress--
                        binding.counter.text = counter.toString()
                    }
                    cancel()
                    showToast("Finished")
                    changeUIStateTo(UIStates.Waiting)
                }
                binding.startStopButton.setOnClickListener {
                    job.cancel()
                    showToast("Stopped")
                    changeUIStateTo(UIStates.Waiting)
                }
            }
        }
        changeUIStateTo(UIStates.Waiting)
    }

    private fun showToast(msg: String) {
        Toast.makeText(applicationContext,msg, LENGTH_SHORT).show()
    }
}