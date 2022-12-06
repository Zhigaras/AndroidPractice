package edu.skillbox.m14retrofit

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import edu.skillbox.m14retrofit.databinding.FragmentMainBinding

private const val TAG = "MainFragment"

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val viewModel: MainViewModel by activityViewModels()
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")
        binding.refreshButton.setOnClickListener {
            viewModel.fetchUser()
        }

        viewModel.user.observe(viewLifecycleOwner) { user ->
            refreshUserCard(user)
        }

        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.stateFlow
                    .collect { state ->
                        when (state) {
                            is State.Progress -> {
                                Log.d(TAG, state::class.java.simpleName)
                                binding.refreshButton.isEnabled = false
                                binding.progressBar.visibility = View.VISIBLE
                            }
                            is State.Success -> {
                                Log.d(TAG, state::class.java.simpleName)
                                binding.refreshButton.isEnabled = true
                                binding.progressBar.visibility = View.INVISIBLE
                            }
                            is State.Error -> {
                                Log.d(TAG, state::class.java.simpleName)
                                binding.refreshButton.isEnabled = true
                                binding.progressBar.visibility = View.INVISIBLE
                            }
                        }
                    }
            }

        viewLifecycleOwner.lifecycleScope
            .launchWhenStarted {
                viewModel.errorChannel
                    .collect { msg ->
                        showToast(msg)
                    }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d(TAG, "onDestroyView")
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    private fun refreshUserCard(user: User) {
        Glide.with(this)
            .load(user.picture.large)
            .into(binding.avatarImage)
        binding.gender.text = user.gender
        binding.name.text = "${user.name.title}. ${user.name.first} ${user.name.last}"
        binding.Dob.text =
            "${user.dob.date.takeWhile { it != 'T' }}, ${user.dob.age} y.o."
        binding.city.text = listOf(user.location.city, user.location.state, user.location.country)
            .joinToString()
        binding.street.text = listOf(user.location.street.name, user.location.street.number)
            .joinToString()
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}