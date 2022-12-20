package edu.skillbox.m18_permissions.presentation

import android.Manifest
import android.content.ContentResolver
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import edu.skillbox.m18_permissions.R
import edu.skillbox.m18_permissions.data.database.App
import edu.skillbox.m18_permissions.data.database.PhotoModel
import edu.skillbox.m18_permissions.databinding.FragmentCameraBinding
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executor

private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss"
private const val DATE_FORMAT = "yyyy-MM-dd"

class CameraFragment : Fragment() {
    
    private val name
        get() = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
    private val dateOfPhoto
        get() = SimpleDateFormat(DATE_FORMAT, Locale.US)
            .format(System.currentTimeMillis())
    private var imageCapture: ImageCapture? = null
    private lateinit var executor: Executor
    private lateinit var contentResolver: ContentResolver
    private var _binding: FragmentCameraBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CameraViewModel by viewModels {
        ViewModelFactory((activity?.applicationContext as App).db.photoDao())
    }
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        sharedElementEnterTransition = TransitionInflater.from(context)
            .inflateTransition(R.transition.grid_transition)
        executor = ContextCompat.getMainExecutor(requireContext())
        contentResolver = requireActivity().contentResolver
        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    
        postponeEnterTransition()
        binding.previewView.doOnPreDraw {
            startPostponedEnterTransition()
        }
        
        checkPermission()
        
        binding.closeCameraButton.setOnClickListener {
            findNavController().navigate(R.id.from_camera_to_gallery)
        }
        binding.takePhotoButton.setOnClickListener {
            takePhoto()
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.lastPhotoFlow.collect { lastPhoto ->
                lastPhoto?.let {
                    Glide.with(requireContext())
                        .load(Uri.parse(lastPhoto.photoUri))
                        .centerCrop()
                        .into(binding.previewView)
                }
            }
        }
        
        binding.previewView.setOnClickListener {
            val lastPhoto = viewModel.lastPhotoFlow.value!!
            binding.previewView.transitionName = lastPhoto.photoUri
            val extras = FragmentNavigatorExtras(
                it to lastPhoto.photoUri
            )
            val direction = CameraFragmentDirections.fromCameraToFullScreenPhoto(lastPhoto)
            findNavController().navigate(direction, extras)
        }
    
        
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    private val launcher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { map ->
            if (map.values.all { it }) {
                startCamera()
            } else {
                showToast("Permission is not Granted.")
            }
        }
    
    private fun takePhoto() {
        val imageCapture = imageCapture ?: return
        
        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        }
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(
                contentResolver,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues
            )
            .build()
        
        imageCapture.takePicture(
            outputOptions,
            executor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    showToast("Photo saved on ${outputFileResults.savedUri}")
                    viewModel.savePhoto(
                        PhotoModel(
                            photoUri = outputFileResults.savedUri.toString(),
                            dateOfPhoto = dateOfPhoto
                        )
                    )
                }
                
                override fun onError(exception: ImageCaptureException) {
                    showToast("Photo failed ${exception.message}")
                    exception.printStackTrace()
                }
            }
        )
    }
    
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(requireContext())
        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            
            val preview = Preview.Builder().build()
            preview.setSurfaceProvider(binding.cameraView.surfaceProvider)
            imageCapture = ImageCapture.Builder().build()
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                this,
                CameraSelector.DEFAULT_BACK_CAMERA,
                preview,
                imageCapture
            )
        }, executor)
    }
    
    private fun checkPermission() {
        val isAllGranted = REQUEST_PERMISSIONS.all { permission ->
            ContextCompat.checkSelfPermission(requireContext(), permission) ==
                    PackageManager.PERMISSION_GRANTED
        }
        if (isAllGranted) {
            startCamera()
            showToast("permission is granted.")
        } else {
            launcher.launch(REQUEST_PERMISSIONS)
        }
    }
    
    companion object {
        private val REQUEST_PERMISSIONS: Array<String> = buildList {
            add(Manifest.permission.CAMERA)
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }
        }.toTypedArray()
    }
    
    private fun showToast(msg: String) =
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT)
}