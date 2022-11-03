package edu.skillbox.myfirstapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import edu.skillbox.myfirstapplication.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var counter = 0
    private val busCapacity = 50

    private fun checkState(binding: ActivityMainBinding, counter: Int) {
        binding.counter.text = counter.toString()
        when (counter) {
            0 -> {
                binding.resetButton.visibility = View.INVISIBLE
                binding.mainTextField.text = getText(R.string.all_seats_are_free)
                binding.mainTextField.setTextColor(Color.GREEN)
                binding.minusButton.isEnabled = false
                binding.plusButton.isEnabled = true
            }
            in (1 until busCapacity) -> {
                binding.resetButton.visibility = View.INVISIBLE
                binding.mainTextField.text = getString(R.string.free_seats_left, busCapacity - counter)
                binding.mainTextField.setTextColor(Color.BLUE)
                binding.minusButton.isEnabled = true
                binding.plusButton.isEnabled = true
            }
            busCapacity -> {
                binding.resetButton.visibility = View.VISIBLE
                binding.mainTextField.text = getText(R.string.too_much_passengers)
                binding.mainTextField.setTextColor(Color.RED)
                binding.minusButton.isEnabled = true
                binding.plusButton.isEnabled = false
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.counter.text = counter.toString()

        binding.plusButton.setOnClickListener {
            counter++
            checkState(binding, counter)
        }

        binding.minusButton.setOnClickListener {
            counter--
            checkState(binding, counter)
        }

        binding.resetButton.setOnClickListener {
            counter = 0
            checkState(binding, counter)
        }
    }
}
