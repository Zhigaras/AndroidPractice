package com.example.skillbox_hw_quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.Toast.LENGTH_SHORT
import androidx.core.view.forEach
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.skillbox_hw_quiz.databinding.FragmentInterviewBinding
import com.example.skillbox_hw_quiz.quiz.QuizStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class InterviewFragment : Fragment() {

    private var _binding: FragmentInterviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInterviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun View.alphaAnimate() {
        this.alpha = 0f
        this.animate().apply {
            alpha(1f)
            duration = 1000
        }.start()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainLayout: LinearLayout = binding.questionsArea
        val quiz = QuizStorage.getQuiz(QuizStorage.Locale.Ru)
        val answers = mutableListOf<Int>()
        val bundle = Bundle()
        val scope = CoroutineScope(Dispatchers.Main)

        scope.launch {
            quiz.questions.forEach { question ->
                val textView = TextView(context)
                textView.text = question.question
                textView.setTextAppearance(R.style.Interview_text_style)
                textView.setPadding(40)

                val radioGroup = RadioGroup(context)
                radioGroup.addView(textView)
                question.answers.forEachIndexed { index, answer ->
                    val radioButton = RadioButton(context)
                    radioButton.id = index
                    radioButton.text = answer
                    radioButton.setTextAppearance(R.style.Radio_group_style)
                    radioGroup.addView(radioButton)
                }
                mainLayout.addView(radioGroup, ViewGroup.LayoutParams.MATCH_PARENT)
                radioGroup.alphaAnimate()
                delay(300)
            }
            binding.backButton.apply {
                visibility = View.VISIBLE
                this.alphaAnimate()
            }
            binding.submitButton.apply {
                visibility = View.VISIBLE
                this.alphaAnimate()
            }
        }

        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_InterviewFragment_to_FirstFragment)
        }

        binding.submitButton.setOnClickListener {
            answers.clear()
            mainLayout.forEach {
                it as RadioGroup;
                answers.add(it.checkedRadioButtonId)
            }
            try {
                bundle.putString("param1", QuizStorage.answer(quiz, answers))
                findNavController().navigate(
                    R.id.action_InterviewFragment_to_ResultFragment,
                    bundle
                )
            } catch (_: ArrayIndexOutOfBoundsException) {
                showToast("Please answer all questions.")
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showToast(msg: String) {
        val toast = Toast.makeText(context, msg, LENGTH_SHORT)
        toast.show()
    }
}