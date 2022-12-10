package edu.skillbox.m12mvvm.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import edu.skillbox.m12mvvm.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }
    private lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val searchRequest: String get() = binding.searchBar.text.toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchButton.setOnClickListener {
            viewModel.onButtonClick(searchRequest)
        }

        binding.searchBar.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.searchButton.isEnabled = s!!.length >= 3
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.state
                    .collect { state ->
                        when (state) {
                            is State.Progress -> {
                                binding.progressBar.isVisible = true
                                binding.searchButton.isEnabled = false
                            }
                            is State.Success -> {
                                binding.progressBar.isVisible = false
                                binding.message.text = state.message
                                if (binding.searchBar.text.length > 2)
                                    binding.searchButton.isEnabled = true
                            }
                            is State.Error -> {
                                binding.progressBar.isVisible = false
                                binding.message.text = state.message
                                binding.searchButton.isEnabled = true
                            }
                        }
                    }
            }
    }
}