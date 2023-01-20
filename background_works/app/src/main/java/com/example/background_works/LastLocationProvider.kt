package com.example.background_works

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Location
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import java.util.*

class LastLocationProvider constructor(private val context: Context) {
    
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    
    @SuppressLint("MissingPermission")
    fun getLastLocation(callback: (Location?) -> Unit) {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                callback(location)
            }.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
            }
    }
    
    fun hasLocationPermission(): Boolean {
        return REQUIRED_PERMISSIONS.any { permission ->
            ContextCompat.checkSelfPermission(context, permission) ==
                    PERMISSION_GRANTED
        }
    }
    
    companion object {
        val REQUIRED_PERMISSIONS: Array<String> = arrayOf(
            ACCESS_COARSE_LOCATION)
    }
}
