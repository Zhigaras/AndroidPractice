package edu.skillbox.m3

import android.os.Bundle
import android.view.View
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

        fun useState(state: UIState): Unit = when (state) {
            is UIState.Waiting -> {
                refreshProgressBar(binding.slider.value)
                binding.startButton.visibility = View.VISIBLE
                binding.stopButton.visibility = View.INVISIBLE
                binding.slider.isEnabled = true
                binding.slider.addOnChangeListener { _, value, _ ->
                    refreshProgressBar(value)
                }
            }

            is UIState.Progress -> {
                binding.startButton.visibility = View.INVISIBLE
                binding.stopButton.visibility = View.VISIBLE
                binding.slider.isEnabled = false
                val scope = CoroutineScope(Dispatchers.Main)
                val job = scope.launch {
                    while (counter > 0) {
                        delay(1000)
                        yield()
                        counter--
                        binding.progressBar.progress--
                        binding.counter.text = counter.toString()
                    }
                    showToast("Finished")
                    cancel()
                    useState(UIState.Waiting)
                }
                binding.stopButton.setOnClickListener {
                    job.cancel()
                    showToast("Stopped")
                    useState(UIState.Waiting)
                }
            }
        }

        useState(UIState.Waiting)

        binding.startButton.setOnClickListener {
            useState(UIState.Progress)
        }

    }

    private fun showToast(msg: String) {
        Toast.makeText(applicationContext, msg, LENGTH_SHORT).show()
    }
}