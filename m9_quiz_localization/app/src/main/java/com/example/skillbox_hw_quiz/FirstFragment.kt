package com.example.skillbox_hw_quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.skillbox_hw_quiz.databinding.FragmentFirstBinding
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*
import java.util.Locale.US

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val calendar: Calendar = Calendar.getInstance()
    private val locale = Locale.getDefault()
    private val dateFormat = when (locale) {
        US -> SimpleDateFormat("MM-dd-yyyy", locale)
        else -> SimpleDateFormat("dd-MM-yyyy", locale)
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dateOfBirthButton.setOnClickListener {
            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setTitleText(R.string.enter_date_of_birth)
                .build()

            datePicker.addOnPositiveButtonClickListener {
                calendar.timeInMillis = it
                Snackbar.make(
                    binding.dateOfBirthButton,
                    dateFormat.format(calendar.time),
                    Snackbar.LENGTH_LONG
                ).show()
            }
            datePicker.show(childFragmentManager, "DatePicker")
        }

        binding.startButton.setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_InterviewFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
