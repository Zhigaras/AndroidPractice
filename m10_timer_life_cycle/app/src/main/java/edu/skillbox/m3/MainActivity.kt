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
private const val KEY_PB_MAX = "progressBarMax"
private const val KEY_CURRENT_STATE = "currentState"
private const val KEY_IS_RUNNING = "isRunning"

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var counter = 10.0f
    private var isRunning = false
    private val sliderAvailability get() = !isRunning
    private var currentState: UIStates = UIStates.Waiting
    private val scope = CoroutineScope(Dispatchers.Main)
    private var job = scope.coroutineContext.job


    private fun refreshPbAndCounter(value: Float) {
        counter = value
        binding.progressBar.max = (value * 10).toInt()
        binding.counter.text = value.roundToInt().toString()
        binding.progressBar.progress = (value * 10).toInt()
    }

    private fun changeButtonTo(UIState: UIStates) = when (UIState) {
        is UIStates.Waiting -> {
            binding.startButton.visibility = View.VISIBLE
            binding.stopButton.visibility = View.INVISIBLE
        }
        is UIStates.Progress -> {
            binding.startButton.visibility = View.INVISIBLE
            binding.stopButton.visibility = View.VISIBLE
        }
    }

    private fun changeUIStateTo(UIState: UIStates): Unit {
        binding.slider.isEnabled = sliderAvailability
        changeButtonTo(UIState)
        currentState = UIState
        when (UIState) {
            is UIStates.Waiting -> {
                refreshPbAndCounter(binding.slider.value)
                binding.slider.addOnChangeListener { _, value, _ ->
                    refreshPbAndCounter(value)
                }
            }
            is UIStates.Progress -> {
                binding.counter.text = counter.roundToInt().toString()
                binding.progressBar.progress = (counter * 10).toInt()
            }
        }
    }

    private fun startCount() {
        isRunning = true
        job = scope.launch {
            while (counter > 0) {
                delay(100)
                counter -= 0.1f
                binding.progressBar.progress--
                if ((counter * 10).toInt() % 10 == 0) {
                    Log.d(TAG, "counter = $counter")
                    binding.counter.text = counter.roundToInt().toString()
                }
            }
            cancel()
            isRunning = false
            showToast("Finished")
            changeUIStateTo(UIStates.Waiting)
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
            binding.progressBar.max = it.getInt(KEY_PB_MAX)
            isRunning = it.getBoolean(KEY_IS_RUNNING)
            currentState = it.getSerializable(KEY_CURRENT_STATE) as UIStates
        }

        changeUIStateTo(currentState)

        if (isRunning) startCount()

        binding.startButton.setOnClickListener {
            startCount()
            changeUIStateTo(UIStates.Progress)
        }

        binding.stopButton.setOnClickListener {
            job.cancel()
            isRunning = false
            showToast("Stopped")
            changeUIStateTo(UIStates.Waiting)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "${this::class.java.simpleName} onSaveInstanceState")
        outState.apply {
            putFloat(KEY_COUNTER, counter)
            putInt(KEY_PB_PROGRESS, binding.progressBar.progress)
            putInt(KEY_PB_MAX, binding.progressBar.max)
            putBoolean(KEY_IS_RUNNING, isRunning)
            putSerializable(KEY_CURRENT_STATE, currentState)
        }
        job.cancel()
    }

    private fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, LENGTH_SHORT).show()
    }
}