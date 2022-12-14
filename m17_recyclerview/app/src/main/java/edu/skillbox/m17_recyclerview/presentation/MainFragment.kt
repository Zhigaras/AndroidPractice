package edu.skillbox.m17_recyclerview.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import dagger.hilt.android.AndroidEntryPoint
import edu.skillbox.m17_recyclerview.databinding.FragmentMainBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class MainFragment : Fragment() {
    
    companion object {
        fun newInstance() = MainFragment()
    }
    
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val marsPhotosAdapter = MarsPhotosAdapter()
        binding.recyclerView.adapter = marsPhotosAdapter
        binding.recyclerView.layoutManager =
            GridLayoutManager(
                requireContext(),
                3,
                GridLayoutManager.VERTICAL,
                false
            )
        
//        viewLifecycleOwner.lifecycleScope
//            .launchWhenStarted {
//                viewModel.roversPhotoFlow.collect { marsRoversPhoto ->
//                    marsRoversPhoto?.let {
//                        marsPhotosAdapter.setData(it.photos)
//                    }
//                }
//            }
        viewModel.roversPhotoFlow.onEach {
            marsPhotosAdapter.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}