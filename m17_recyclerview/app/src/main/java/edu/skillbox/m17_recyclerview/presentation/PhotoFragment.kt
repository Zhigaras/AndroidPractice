package edu.skillbox.m17_recyclerview.presentation

import android.os.Bundle
import android.transition.TransitionInflater
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import edu.skillbox.m17_recyclerview.R
import edu.skillbox.m17_recyclerview.databinding.FragmentPhotoBinding

class PhotoFragment : Fragment() {
    private var paramSol: String? = null
    private var paramCameraName: String? = null
    private var paramDateOfPhoto: String? = null
    private var paramRoverName: String? = null
    private var paramLaunchDate: String? = null
    private var paramLandingDate: String? = null
    private var paramPhotoUrl: String? = null
    
    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { bundle ->
            Log.d(this::class.java.simpleName, "$bundle")
            paramSol = bundle.getString(KEY_SOL)
            paramCameraName = bundle.getString(KEY_CAMERA_NAME)
            paramDateOfPhoto = bundle.getString(KEY_DATE_OF_PHOTO)
            paramRoverName = bundle.getString(KEY_ROVER_NAME)
            paramLaunchDate = bundle.getString(KEY_LAUNCH_DATE)
            paramLandingDate = bundle.getString(KEY_LANDING_DATE)
            paramPhotoUrl = bundle.getString(KEY_IMAGE_URL)
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        sharedElementEnterTransition = TransitionInflater.from(requireContext())
            .inflateTransition(R.transition.grid_transition)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            sol.text = buildString {
        append("Sol: ")
        append(paramSol)
    }
            cameraName.text = buildString {
        append("Camera: ")
        append(paramCameraName)
    }
            dateOfPhoto.text = buildString {
        append("Date of photo: ")
        append(paramDateOfPhoto)
    }
            roverName.text = buildString {
        append("Rover: ")
        append(paramRoverName)
    }
            launchDate.text = buildString {
        append("Launch date: ")
        append(paramLaunchDate)
    }
            landingDate.text = buildString {
        append("Landing date: ")
        append(paramLandingDate)
    }
            Glide
                .with(requireContext())
                .load(paramPhotoUrl)
                .into(photo)
        }
    }
    
    companion object {
        
        fun newInstance() = PhotoFragment()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}