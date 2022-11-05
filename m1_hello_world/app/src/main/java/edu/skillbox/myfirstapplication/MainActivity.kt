package edu.skillbox.myfirstapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import edu.skillbox.myfirstapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var counter = 0
    fun getState() = when (counter) {
        0 -> State.Start
        in (1 until busCapacity) -> State.Progress
        busCapacity -> State.Finish
        else -> State.Error
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.counter.text = counter.toString()

        binding.plusButton.setOnClickListener {
            counter++
            updateView(binding, counter)
        }

        binding.minusButton.setOnClickListener {
            counter--
            updateView(binding, counter)
        }

        binding.resetButton.setOnClickListener {
            counter = 0
            updateView(binding, counter)
        }
    }

    private fun updateView(binding: ActivityMainBinding, counter: Int) {
        binding.counter.text = counter.toString()

        when (getState()) {
            is State.Start -> {
                binding.resetButton.visibility = View.INVISIBLE
                binding.mainTextField.text = getText(R.string.all_seats_are_free)
                binding.mainTextField.setTextColor(Color.GREEN)
                binding.minusButton.isEnabled = false
                binding.plusButton.isEnabled = true
            }
            is State.Progress -> {
                binding.resetButton.visibility = View.INVISIBLE
                binding.mainTextField.text = getString(R.string.free_seats_left, busCapacity - counter)
                binding.mainTextField.setTextColor(Color.BLUE)
                binding.minusButton.isEnabled = true
                binding.plusButton.isEnabled = true
            }
            is State.Finish -> {
                binding.resetButton.visibility = View.VISIBLE
                binding.mainTextField.text = getText(R.string.too_much_passengers)
                binding.mainTextField.setTextColor(Color.RED)
                binding.minusButton.isEnabled = true
                binding.plusButton.isEnabled = false
            }
            is State.Error -> {
                binding.resetButton.visibility = View.VISIBLE
                binding.mainTextField.text = getText(R.string.error)
                binding.mainTextField.isAllCaps = true
                binding.mainTextField.setTextColor(Color.MAGENTA)
                binding.minusButton.isEnabled = false
                binding.plusButton.isEnabled = false
            }
        }
    }
}

const val busCapacity = 10
