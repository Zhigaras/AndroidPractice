package com.zhigaras.m19_location_new

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.zhigaras.m19_location_new.databinding.ActivityMapsBinding
import com.zhigaras.m19_location_new.model.Feature
import kotlinx.coroutines.launch

private const val CAMERA_POSITION = "camera_position"
private const val TAG_DEBUG = "My_debug"

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    
    private var mMap: GoogleMap? = null
    
    private var placeList: List<Feature> = emptyList()
    
    private val viewModel: MapsViewModel by viewModels()
    
    private lateinit var fusedClient: FusedLocationProviderClient
    private var locationListener: LocationSource.OnLocationChangedListener? = null
    private var needMoveCamera = true
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0
    private val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { map ->
        if (map.values.isNotEmpty() && map.values.all { it }) {
            startLocation()
        }
    }
    
    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(result: LocationResult) {
            result.lastLocation?.let { location ->
                locationListener?.onLocationChanged(location)
                currentLatitude = location.latitude
                currentLongitude = location.longitude
                
                val cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                    LatLng(location.latitude, location.longitude),
                    18f
                )
                
                if (needMoveCamera) {
                    needMoveCamera = false
                    mMap?.moveCamera(cameraUpdate)
                }
            }
        }
    }
    
    @SuppressLint("MissingPermission")
    private fun startLocation() {
        mMap?.isMyLocationEnabled = true
        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            1_000
        ).build()
        
        fusedClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }
    
    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val binding: ActivityMapsBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_maps)
        
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        /** Obtain the SupportMapFragment and get notified when the map is ready to be used.*/
        
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            mMap = googleMap
            checkPermissions()
            with(googleMap.uiSettings) {
                isZoomControlsEnabled = true
                isZoomGesturesEnabled = true
                isScrollGesturesEnabledDuringRotateOrZoom = true
            }
            addMarkersToMap(placeList)
            googleMap.setLocationSource(object : LocationSource {
                override fun activate(p0: LocationSource.OnLocationChangedListener) {
                    locationListener = p0
                }
                
                override fun deactivate() {
                    locationListener = null
                }
            })
        }
        
        if (savedInstanceState != null) {
            val cameraPosition = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                savedInstanceState.getParcelable(CAMERA_POSITION, CameraPosition::class.java)
            } else {
                @Suppress("DEPRECATION")
                savedInstanceState.getParcelable(CAMERA_POSITION)
            }
            Log.d("OnCreate", cameraPosition.toString())
            cameraPosition?. let {
                mMap?.moveCamera(CameraUpdateFactory.newCameraPosition(it))
            }
            needMoveCamera = false
        }
        fusedClient = LocationServices.getFusedLocationProviderClient(this)
        
        binding.showSightsBtn.setOnClickListener {
            Log.d(TAG_DEBUG, "Btn Click")
            mMap?.clear()
            viewModel.getPlaces(currentLongitude, currentLatitude)
        }
        
        lifecycleScope.launch {
            Log.d(TAG_DEBUG, "launch coroutine")
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.placesFlow.collect {
                    Log.d(TAG_DEBUG, "collect")
                    placeList = it
                    addMarkersToMap(it)
                }
            }
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.errorChannel.collect {
                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    
    override fun onStart() {
        super.onStart()
        checkPermissions()
    }
    
    override fun onStop() {
        super.onStop()
        fusedClient.removeLocationUpdates(locationCallback)
    }
    
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(CAMERA_POSITION, mMap?.cameraPosition)
    }
    
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        
        //Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap?.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
    }
    
    private fun addMarkersToMap(placeList: List<Feature>) {
        Log.d(TAG_DEBUG, "adding places")
        placeList.forEach {
            val snippet = buildString {
                append(it.properties.country)
                append(", ${it.properties.countryCode}\n")
                append(it.properties.state)
                append(it.properties.city)
            }
            mMap?.addMarker(
                MarkerOptions()
                    .position(LatLng(it.properties.lat, it.properties.lon))
                    .title(it.properties.name)
                    .snippet(snippet)
            )
        }
    }

//    private fun addMarkersToMap() {
//        Log.d("Debug", "adding places")
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.placesFlow.collect { placeList ->
//                    placeList.forEach {
//                        mMap?.addMarker(
//                            MarkerOptions()
//                                .position(LatLng(it.properties.lat, it.properties.lon))
//                                .title(it.properties.name)
//                        )
//                    }
//                }
//            }
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.errorChannel.collect {
//                    Toast.makeText(applicationContext, it, Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
    
    private fun checkPermissions() {
        if (REQUIRED_PERMISSIONS.all { permission ->
                ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }) {
            startLocation()
        } else {
            launcher.launch(REQUIRED_PERMISSIONS)
        }
    }
    
    companion object {
        private val REQUIRED_PERMISSIONS: Array<String> = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    }
}