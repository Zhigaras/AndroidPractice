package edu.skillbox.m18_permissions.presentation

import android.net.Uri
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import edu.skillbox.m18_permissions.R
import edu.skillbox.m18_permissions.databinding.FragmentPhotoBinding
import java.util.concurrent.TimeUnit

class PhotoFragment : Fragment() {
    
    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!
    
    private val args: PhotoFragmentArgs by navArgs()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(inflater, container, false)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.grid_transition)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.apply {
            fullscreenPhotoView.transitionName = args.photoData.photoUri
            dateTextView.text = args.photoData.dateOfPhoto
            Glide.with(requireContext())
                .load(Uri.parse(args.photoData.photoUri))
                .into(fullscreenPhotoView)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}