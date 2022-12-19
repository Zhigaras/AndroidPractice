package edu.skillbox.m18_permissions.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import edu.skillbox.m18_permissions.R
import edu.skillbox.m18_permissions.data.database.App
import edu.skillbox.m18_permissions.databinding.FragmentGalleryBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class GalleryFragment : Fragment() {
    
    companion object {
        fun newInstance() = GalleryFragment()
    }
    
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GalleryViewModel by viewModels {
        ViewModelFactory((activity?.applicationContext as App).db.photoDao()) }
    private val photoAdapter = GalleryPhotoAdapter()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.openCameraButton.setOnClickListener {
            findNavController().navigate(R.id.from_gallery_to_camera)
        }
    
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        
        binding.recyclerView.adapter = photoAdapter
        binding.recyclerView.layoutManager =
            GridLayoutManager(
                requireContext(),
                3,
                GridLayoutManager.VERTICAL,
                false
            )
        
        viewModel.photoFlow.onEach {
            photoAdapter.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
}