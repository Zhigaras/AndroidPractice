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
private const val KEY_PB_MAX = "progressBarMax"
private const val KEY_IS_RUNNING = "isRunning"

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private var counter = 10.0f
    private var isRunning = false
    private val sliderAvailability get() = !isRunning
    private val scope = CoroutineScope(Dispatchers.Main)
    private var job = scope.coroutineContext.job

    private fun refreshPbAndCounter(value: Float) = value.let {
            counter = it
            binding.progressBar.max = (it * 10).toInt()
            binding.progressBar.progress = (it * 10).toInt()
            binding.counter.text = it.roundToInt().toString()
        }

    private fun changeUIStateWith(isRunning: Boolean): Unit {
        binding.slider.isEnabled = sliderAvailability
        when (isRunning) {
            false -> {
                binding.startButton.visibility = View.VISIBLE
                binding.stopButton.visibility = View.INVISIBLE
                refreshPbAndCounter(binding.slider.value)
                binding.slider.addOnChangeListener { _, value, _ ->
                    refreshPbAndCounter(value)
                }
            }
            true -> {
                binding.startButton.visibility = View.INVISIBLE
                binding.stopButton.visibility = View.VISIBLE
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
            changeUIStateWith(isRunning)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "${this::class.java.simpleName} onCreate")
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        savedInstanceState?.let {
            counter = it.getFloat(KEY_COUNTER)
            binding.progressBar.max = it.getInt(KEY_PB_MAX)
            isRunning = it.getBoolean(KEY_IS_RUNNING)
        }

        changeUIStateWith(isRunning)

        if (isRunning) startCount()

        binding.startButton.setOnClickListener {
            startCount()
            changeUIStateWith(isRunning)
        }

        binding.stopButton.setOnClickListener {
            job.cancel()
            isRunning = false
            showToast("Stopped")
            changeUIStateWith(isRunning)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "${this::class.java.simpleName} onSaveInstanceState")
        outState.apply {
            putFloat(KEY_COUNTER, counter)
            putInt(KEY_PB_MAX, binding.progressBar.max)
            putBoolean(KEY_IS_RUNNING, isRunning)
        }
        job.cancel()
    }

    private fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, LENGTH_SHORT).show()
    }
}