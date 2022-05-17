package com.example.mcdonalds.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.mcdonalds.R
import com.example.mcdonalds.databinding.ActivityMapsBinding
import com.example.mcdonalds.utils.Constants
import com.example.mcdonalds.utils.MessageManager
import com.example.mcdonalds.utils.Permission
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object{
        const val PERMISSION_CODE = 100
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getCurrentLocation()
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        Log.d("marker", "title : ${p0.title}, position : ${p0.position}")
        return false
    }

    private fun checkPermission() : Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this@MapsActivity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION), PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("posizione", "Mi prendo la posizione")
                getCurrentLocation()
            }else{
                Log.d("posizione", "Non hai accettato i permessi")
                //TODO Permission Denied Notification
            }
        }
    }

    private fun getCurrentLocation() {
        if(checkPermission()){
            if(Permission.locationIsEnabled(this)){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    val location = it.result
                    if(location == null){
                        Log.d("posizione", "Richiedo una request update")
                        this.fusedLocationProviderClient.requestLocationUpdates(this.locationRequest(), this.locationCallBack(), Looper.getMainLooper())
                        this.fusedLocationProviderClient.removeLocationUpdates(this.locationCallBack())
                    }else{
                        updateMapView(LatLng(location.latitude, location.longitude))
                    }
                }
            }else{
                MessageManager.displayNoGpsEnableMessage(this@MapsActivity)
            }
        }else{
            requestPermission()
        }
    }

    private fun locationRequest(): LocationRequest {
        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 20 * 1000
        return locationRequest
    }

    private fun locationCallBack() : LocationCallback{
        return object : LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                for(location in p0.locations){
                    //Update MapView
                    Log.d("posizione", "Richiedo Posizione")
                    updateMapView(LatLng(location.latitude, location.longitude))
                }
            }
        }
    }

    private fun updateMapView(point : LatLng) {
        val currentPosition = LatLng(point.latitude, point.longitude)
        val cameraPos = CameraPosition.builder()
            .target(currentPosition)
            .zoom(Constants.ZOOM)
            .bearing(Constants.BEARING)
            .tilt(Constants.TILT)
            .build()

        mMap.addMarker(
            MarkerOptions()
                .position(currentPosition)
                .title("Sono qui")
                .draggable(false)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        )

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPos))

        Log.d("posizione", "Setto la posizione")
    }
}