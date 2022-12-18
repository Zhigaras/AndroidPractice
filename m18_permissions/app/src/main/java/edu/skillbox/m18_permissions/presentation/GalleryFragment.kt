package edu.skillbox.m18_permissions.presentation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.skillbox.m18_permissions.R
import edu.skillbox.m18_permissions.databinding.FragmentGalleryBinding

@AndroidEntryPoint
class GalleryFragment : Fragment() {
    
    companion object {
        fun newInstance() = GalleryFragment()
    }
    
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GalleryViewModel by viewModels()
    
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
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
}