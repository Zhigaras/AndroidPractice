package edu.skillbox.m3

sealed class StartStopButtonState {

    object StartButton : StartStopButtonState()

    object StopButton : StartStopButtonState()
}


//package edu.skillbox.m3
//
//import android.content.ContentValues.TAG
//import android.graphics.Color
//import android.os.Bundle
//import android.os.PersistableBundle
//import android.util.Log
//import android.widget.Toast
//import android.widget.Toast.LENGTH_SHORT
//import androidx.appcompat.app.AppCompatActivity
//import edu.skillbox.m3.databinding.ActivityMainBinding
//import kotlinx.coroutines.*
//import kotlin.math.roundToInt
//
//private const val KEY_COUNTER = "counter"
//private const val KEY_SLIDER_STATE = "sliderState"
//private const val KEY_START_STOP_BUTTON = "startStopButtonState"
//
//class MainActivity : AppCompatActivity() {
//
//    private var _binding: ActivityMainBinding? = null
//    private val binding get() = _binding!!
//
//    lateinit var bundle: Bundle
//
//    private var counter = 10.0f
//
//    private var startStopButtonState: StartStopButtonState = StartStopButtonState.StartButton
//
//    private fun refreshProgressBar(value: Float) {
//        counter = value
//        binding.counter.text = counter.roundToInt().toString()
//        binding.progressBar.max = (counter * 10).toInt()
//        binding.progressBar.progress = (counter * 10).toInt()
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.d(TAG, "${this::class.java.simpleName} onCreate")
//        _binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        fun changeButtonStateTo(buttonState: StartStopButtonState): Unit = when (buttonState) {
//            is StartStopButtonState.StartButton -> {
//                binding.startStopButton.text = getText(R.string.start)
//                binding.startStopButton.setTextColor(Color.GREEN)
//                startStopButtonState = StartStopButtonState.StartButton
//            }
//
//            is StartStopButtonState.StopButton -> {
//                binding.startStopButton.text = getText(R.string.stop)
//                binding.startStopButton.setTextColor(Color.RED)
//                startStopButtonState = StartStopButtonState.StopButton
//            }
//        }
//
//        fun changeUIStateTo(UIState: UIStates): Unit = when (UIState) {
//            is UIStates.Waiting -> {
//                changeButtonStateTo(StartStopButtonState.StartButton)
//                refreshProgressBar(binding.slider.value)
//                binding.slider.isEnabled = true
//                binding.slider.addOnChangeListener { _, value, _ ->
//                    refreshProgressBar(value)
//                }
//                binding.startStopButton.setOnClickListener {
//                    changeUIStateTo(UIStates.Progress)
//                }
//            }
//
//            is UIStates.Progress -> {
//                changeButtonStateTo(StartStopButtonState.StopButton)
//                binding.slider.isEnabled = false
//                val scope = CoroutineScope(Dispatchers.Main)
//                val job = scope.launch {
//                    while (counter > 0) {
//                        delay(100)
//                        counter -= 0.1f
//                        binding.progressBar.progress--
//                        if ((counter * 10).toInt() % 10 == 0) {
//                            Log.d(TAG, "поток ${Thread.currentThread()}")
//                            binding.counter.text = counter.roundToInt().toString()
//                        }
//                    }
//                    cancel()
//                    showToast("Finished")
//                    changeUIStateTo(UIStates.Waiting)
//                }
//                binding.startStopButton.setOnClickListener {
//                    job.cancel()
//                    showToast("Stopped")
//                    changeUIStateTo(UIStates.Waiting)
//                }
//            }
//        }
//        changeUIStateTo(UIStates.Waiting)
//    }
//
//    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
//        super.onSaveInstanceState(outState, outPersistentState)
//        Log.d(TAG, "${this::class.java.simpleName} onSaveInstanceState")
//        bundle.putBoolean(KEY_SLIDER_STATE, binding.slider.isEnabled)
//        bundle.putFloat(KEY_COUNTER, counter)
//        bundle.putSerializable(KEY_START_STOP_BUTTON, startStopButtonState)
//        outState.putAll(bundle)
//    }
//
//    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
//        super.onRestoreInstanceState(savedInstanceState)
//        Log.d(TAG, "${this::class.java.simpleName} onRestoreInstanceState")
//        binding.slider.isEnabled = savedInstanceState.getBoolean(KEY_SLIDER_STATE)
//        counter = savedInstanceState.getFloat(KEY_COUNTER)
//
//    }
//
//    private fun showToast(msg: String) {
//        Toast.makeText(applicationContext, msg, LENGTH_SHORT)
//            .show()
//    }
//}