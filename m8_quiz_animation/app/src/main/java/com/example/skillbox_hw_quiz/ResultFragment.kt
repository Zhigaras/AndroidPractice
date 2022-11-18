package com.example.skillbox_hw_quiz

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.navigation.fragment.findNavController
import com.example.skillbox_hw_quiz.databinding.FragmentResultBinding

/**
 * A simple [Fragment] subclass as the third destination in the navigation.
 */
class ResultFragment : Fragment() {

    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private fun getScaleAnim(xScale: Float, yScale: Float): List<PropertyValuesHolder>{
        return listOf(
            PropertyValuesHolder.ofFloat(View.SCALE_X, xScale, yScale),
            PropertyValuesHolder.ofFloat(View.SCALE_Y, xScale, yScale)
        )
    }

    private val textViewScaleAnim = getScaleAnim(0f, 1f)

    private val buttonColorAnim = PropertyValuesHolder.ofInt(
        "textColor",
        Color.parseColor("#FF000000"),
        Color.parseColor("#FFFFFFFF")
    ).apply {
        setEvaluator(ArgbEvaluator())
    }
    private val buttonScaleAnim = getScaleAnim(1f,1.4f)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView = binding.resultTextView
        val button = binding.startAgainButton

        textView.text = arguments?.getString("param1")

        ObjectAnimator.ofPropertyValuesHolder(
            textView,
            textViewScaleAnim[0],
            textViewScaleAnim[1]
        ).apply {
            duration = 1500
            interpolator = AccelerateDecelerateInterpolator()
            start()
        }

        ObjectAnimator.ofPropertyValuesHolder(
            button,
            buttonScaleAnim[0],
            buttonScaleAnim[1],
            buttonColorAnim
        ).apply {
            duration = 1500
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
            start()
        }

        binding.startAgainButton.setOnClickListener {
            findNavController().navigate(R.id.action_ResultFragment_to_InterviewFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}