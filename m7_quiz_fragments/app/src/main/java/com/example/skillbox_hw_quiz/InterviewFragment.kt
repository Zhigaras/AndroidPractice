package com.example.skillbox_hw_quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.navigation.fragment.findNavController
import com.example.skillbox_hw_quiz.databinding.FragmentInterviewBinding
import com.example.skillbox_hw_quiz.quiz.QuizStorage

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class InterviewFragment : Fragment() {

    private var _binding: FragmentInterviewBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentInterviewBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mainLayout: LinearLayout = binding.questionsArea

        QuizStorage.getQuiz(QuizStorage.Locale.Ru).questions.forEach { question ->
            val textView = TextView(context)
            textView.text = question.question
            val radioGroup = RadioGroup(context)
            question.answers.forEach { answer ->
                val radioButton = RadioButton(context)
                radioButton.text = answer
                radioButton.setTextAppearance(R.style.Radio_group_style)
                radioGroup.addView(radioButton)
            }
            // setTextAppearance применяет не все параметры, не могу разобраться почему
            // в данном случае из стиля Interview_text_style применятся только textSize
            textView.setTextAppearance(R.style.Interview_text_style)

            // применяю padding вручную
            textView.setPadding(40)

            // цвет тоже вручную. Нашел только такой способ:
            textView.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))

            mainLayout.addView(textView, ViewGroup.LayoutParams.MATCH_PARENT)
            mainLayout.addView(radioGroup, ViewGroup.LayoutParams.MATCH_PARENT)
        }


        binding.backButton.setOnClickListener {
            findNavController().navigate(R.id.action_InterviewFragment_to_FirstFragment)
        }

        binding.submitButton.setOnClickListener {
            findNavController().navigate(R.id.action_InterviewFragment_to_ResultFragment)

            onDestroyView()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}