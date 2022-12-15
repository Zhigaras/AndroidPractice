package edu.skillbox.m17_recyclerview.presentation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import edu.skillbox.m17_recyclerview.R
import edu.skillbox.m17_recyclerview.databinding.FragmentMainBinding
import edu.skillbox.m17_recyclerview.entity.MarsRoversPhoto
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

const val KEY_IMAGE_URL = "image_url"
const val KEY_SOL = "sol"
const val KEY_CAMERA_NAME = "camera_name"
const val KEY_DATE_OF_PHOTO = "date_of_photo"
const val KEY_ROVER_NAME = "rover_name"
const val KEY_LAUNCH_DATE = "launch_date"
const val KEY_LANDING_DATE = "landing_date"

@AndroidEntryPoint
class MainFragment : Fragment() {
    
    companion object {
        fun newInstance() = MainFragment()
    }
    
    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private val marsPhotosAdapter = MarsPhotosAdapter { photo -> onItemClick(photo) }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        
        binding.recyclerView.adapter = marsPhotosAdapter
        binding.recyclerView.layoutManager =
            GridLayoutManager(
                requireContext(),
                2,
                GridLayoutManager.VERTICAL,
                false
            )
        
        viewModel.roversPhotoFlow.onEach {
            marsPhotosAdapter.setData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    
    private fun onItemClick(item: MarsRoversPhoto) {
        val bundle = Bundle().apply {
            putString(KEY_IMAGE_URL, item.imgSrc)
            putString(KEY_SOL, item.sol.toString())
            putString(KEY_CAMERA_NAME, item.camera.fullName)
            putString(KEY_DATE_OF_PHOTO, item.earthDate)
            putString(KEY_ROVER_NAME, item.rover.name)
            putString(KEY_LAUNCH_DATE, item.rover.launchDate)
            putString(KEY_LANDING_DATE, item.rover.landingDate)
        }
        parentFragmentManager.beginTransaction().apply {
            setReorderingAllowed(true)
            addToBackStack(PhotoFragment::class.java.simpleName)
            addSharedElement(binding.root.findViewById(R.id.image_view), "transition_name_image")
            replace<PhotoFragment>(R.id.container, args = bundle)
            commit()
        }
        Log.d(this::class.java.simpleName, "$bundle")
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}