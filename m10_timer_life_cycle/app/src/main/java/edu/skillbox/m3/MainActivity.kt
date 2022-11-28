package edu.skillbox.m3

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AppCompatActivity
import edu.skillbox.m3.databinding.ActivityMainBinding
import kotlinx.coroutines.*
import kotlin.math.roundToInt

private const val KEY_COUNTER = "counter"
private const val KEY_PB_PROGRESS = "progressBarProgress"
private const val KEY_SLIDER_STATE = "sliderState"
private const val KEY_START_BUTTON_VIS = "startButtonVisibility"
private const val KEY_STOP_BUTTON_VIS = "stopButtonVisibility"
private const val KEY_CURRENT_STATE = "currentState"
private const val KEY_IS_RUNNING = "isRunning"

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var counter = 10.0f
    private var currentState: UIStates = UIStates.Waiting
    private val scope = CoroutineScope(Dispatchers.Main)
    private var job = scope.coroutineContext.job
    private var isRunning = false


    private fun refreshProgressBar(value: Float) {
        counter = value
        binding.counter.text = counter.roundToInt().toString()
        binding.progressBar.max = (counter * 10).toInt()
        binding.progressBar.progress = (counter * 10).toInt()
    }

    private fun changeButtonTo(buttonState: StartStopButtonState) = when (buttonState) {
        is StartStopButtonState.StartButton -> {
            binding.startButton.visibility = View.VISIBLE
            binding.stopButton.visibility = View.INVISIBLE
        }
        is StartStopButtonState.StopButton -> {
            binding.startButton.visibility = View.INVISIBLE
            binding.stopButton.visibility = View.VISIBLE
        }
    }

    private fun changeUIStateTo(UIState: UIStates): Unit = when (UIState) {
        is UIStates.Waiting -> {
            currentState = UIStates.Waiting
            changeButtonTo(StartStopButtonState.StartButton)
            refreshProgressBar(binding.slider.value)
            binding.slider.isEnabled = true
            binding.slider.addOnChangeListener { _, value, _ ->
                refreshProgressBar(value)
            }
            binding.startButton.setOnClickListener {
                changeUIStateTo(UIStates.Progress)
            }
        }

        is UIStates.Progress -> {
            currentState = UIStates.Progress
            changeButtonTo(StartStopButtonState.StopButton)
            binding.slider.isEnabled = false
            val job = scope.launch(start = CoroutineStart.LAZY) {
                isRunning = true
                while (counter > 0) {
                    delay(100)
                    counter -= 0.1f
                    binding.progressBar.progress--
                    if ((counter * 10).toInt() % 10 == 0) {
                        Log.d(TAG, "counter = $counter")
                        Log.d(TAG, "job = ${scope.coroutineContext.toString()}")
                        binding.counter.text = counter.roundToInt().toString()
                    }
                }
                cancel()
                isRunning = false
                showToast("Finished")
                changeUIStateTo(UIStates.Waiting)
            }
            if (!isRunning) job.start()
            binding.stopButton.setOnClickListener {
                job.cancel()
                isRunning = false
                showToast("Stopped")
                changeUIStateTo(UIStates.Waiting)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "${this::class.java.simpleName} onCreate")
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        savedInstanceState?.let {
            counter = it.getFloat(KEY_COUNTER)
            binding.progressBar.progress = it.getInt(KEY_PB_PROGRESS)
            isRunning = it.getBoolean(KEY_IS_RUNNING)
            currentState = it.getSerializable(KEY_CURRENT_STATE) as UIStates
        }

        changeUIStateTo(currentState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "${this::class.java.simpleName} onSaveInstanceState")
        outState.apply {
            putFloat(KEY_COUNTER, counter)
            putInt(KEY_PB_PROGRESS, binding.progressBar.progress)
            putBoolean(KEY_IS_RUNNING, isRunning)
            putSerializable(KEY_CURRENT_STATE, currentState)
        }
    }

    override fun onResume() {
        super.onResume()
        changeUIStateTo(currentState)
        binding.counter.text = counter.roundToInt().toString()
    }

    private fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, LENGTH_SHORT).show()
    }
}