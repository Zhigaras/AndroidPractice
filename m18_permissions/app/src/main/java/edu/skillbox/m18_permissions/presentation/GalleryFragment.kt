package edu.skillbox.m18_permissions.presentation

import android.content.res.Configuration
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import edu.skillbox.m18_permissions.R
import edu.skillbox.m18_permissions.data.database.App
import edu.skillbox.m18_permissions.databinding.FragmentGalleryBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.util.concurrent.TimeUnit

class GalleryFragment : Fragment() {
    
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GalleryViewModel by viewModels {
        ViewModelFactory((activity?.applicationContext as App).db.photoDao())
    }
    private var spanCount = 0
    private lateinit var photoAdapter: GalleryPhotoAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryBinding.inflate(inflater, container, false)
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.grid_transition)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        val width = requireActivity().resources.displayMetrics.widthPixels
        val orientation = requireActivity().resources.configuration.orientation
    
        spanCount = if (orientation == Configuration.ORIENTATION_PORTRAIT) 4
                    else 8
        
        val elementWidth = (width - 15) / spanCount
        
        photoAdapter = GalleryPhotoAdapter(photoDataListener, elementWidth)
    
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        
        binding.recyclerView.adapter = photoAdapter
        binding.recyclerView.layoutManager =
                GridLayoutManager(
                    requireContext(),
                    spanCount,
                    GridLayoutManager.VERTICAL,
                    false
                )
                
        viewModel.photoFlow.onEach {
            photoAdapter.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    
        binding.openCameraButton.setOnClickListener {
            findNavController().navigate(R.id.from_gallery_to_camera)
        }
    
        postponeEnterTransition()
        binding.recyclerView.doOnPreDraw {
            startPostponedEnterTransition()
        }
    }
    
    private val photoDataListener = GalleryPhotoAdapter.OnClickListener { data, photoImageView ->
        val extras = FragmentNavigatorExtras(
            photoImageView to data.photoUri
        )
        val direction = GalleryFragmentDirections.fromGalleryToFullScreenPhoto(data)
        findNavController().navigate(direction, extras)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}