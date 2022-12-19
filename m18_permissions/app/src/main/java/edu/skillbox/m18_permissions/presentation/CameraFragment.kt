package edu.skillbox.m18_permissions.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import edu.skillbox.m18_permissions.R
import edu.skillbox.m18_permissions.databinding.FragmentCameraBinding


class CameraFragment : Fragment() {
    
    companion object {
        fun newInstance() = CameraFragment()
    }
    
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CameraViewModel by viewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.closeCameraButton.setOnClickListener {
            findNavController().navigate(R.id.from_camera_to_gallery)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.all { it }) {
//                getContacts()
//                startCamera()
            } else {
                Toast
                    .makeText(
                        requireContext(),
                        "Permission is not Granted.",
                        Toast.LENGTH_SHORT
                    )
                    .show()
            }
        }
    
}