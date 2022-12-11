package edu.skillbox.m16_architecture.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import edu.skillbox.m16_architecture.R
import edu.skillbox.m16_architecture.domain.State
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView = view.findViewById<TextView>(R.id.text_view)
        val refreshButton = view.findViewById<Button>(R.id.button)
        val progressBar =view.findViewById<ProgressBar>(R.id.progress_bar)

        refreshButton.setOnClickListener {
            viewModel.reloadUsefulActivity()
        }

        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.usefulActivityFlow.collect {
                    it?.let {
                        textView.text = it.activity
                    }
                }
            }

        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.stateFlow.collect { state ->
                    when (state) {
                        is State.Error -> {
                            progressBar.visibility = View.INVISIBLE
                            refreshButton.isEnabled = true
                        }
                        is State.Progress -> {
                            progressBar.visibility = View.VISIBLE
                            refreshButton.isEnabled = false
                        }
                        is State.Success -> {
                            progressBar.visibility = View.INVISIBLE
                            refreshButton.isEnabled = true
                        }
                    }
                }
            }

        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.errorChannel.collect { error ->
                    Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
                }
            }
    }
}