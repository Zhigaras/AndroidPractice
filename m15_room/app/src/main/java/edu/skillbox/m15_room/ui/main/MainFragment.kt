package edu.skillbox.m15_room.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import edu.skillbox.m15_room.database.App
import edu.skillbox.m15_room.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val wordDao = (activity?.applicationContext as App).db.wordDao()
                return MainViewModel(wordDao) as T
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.addButton.setOnClickListener {
            viewModel.onAddButton(binding.inputText.text.toString())
            binding.inputText.text?.clear()
        }

        binding.clearButton.setOnClickListener {
            viewModel.onClearButton()
        }

        binding.findButton.setOnClickListener {
            viewModel.onFindButton(binding.inputText.text.toString())
            binding.inputText.text?.clear()
        }

        lifecycleScope.launchWhenStarted {
            viewModel.dictFlow
                .collect {
                    binding.message.text = it.joinToString("\n") { word -> word.toString() }
                }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.searchChannel
                .collect {
                    Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.errorFlow
                .observe(viewLifecycleOwner) {
                    binding.inputLayout.error = it
                }
        }

        binding.inputText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val isValid = viewModel.validateInput(s!!)
                isValid.let {
                    binding.addButton.isEnabled = it
                    binding.findButton.isEnabled = it
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }
}