package com.example.mcdonalds.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentContainerView
import com.example.mcdonalds.R
import com.example.mcdonalds.databinding.ActivityMapsBinding
import com.example.mcdonalds.fragments.CompleteOrderFragment
import com.example.mcdonalds.model.McLocation
import com.example.mcdonalds.model.McOrder
import com.example.mcdonalds.utils.Constants
import com.example.mcdonalds.utils.FragmentUtils
import com.example.mcdonalds.utils.MessageManager
import com.example.mcdonalds.utils.Permission
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapView : FragmentContainerView
    private lateinit var question : TextView
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var select : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        this.bindComponents()
        this.setAllListener()

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun bindComponents(){
        this.select = findViewById(R.id.btn_select_location)
        this.mapView = findViewById(R.id.map)
        this.question = findViewById(R.id.txt_select_location)
    }

    private fun setAllListener(){
        this.select.setOnClickListener {
            McOrder.setLocationInfo(McLocation("McDonald's Via Flaminia", LatLng(40.00, 12.00), true))
            this.hideAllComponents()
            FragmentUtils.changeToFinishFragment(this@MapsActivity, CompleteOrderFragment(), "CompleteOrderFragment")
            McOrder.sendOrder()
        }
    }

    private fun hideAllComponents(){
        this.select.isVisible = false
        this.mapView.isVisible = false
        this.question.isVisible = false
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getCurrentLocation()

        mMap.setOnMapClickListener {
            Log.d("posizione", it.latitude.toString())
        }

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
                    Manifest.permission.ACCESS_COARSE_LOCATION), Permission.GPS_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == Permission.GPS_PERMISSION_CODE){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("posizione", "Mi prendo la posizione")
                getCurrentLocation()
            }else{
                Log.d("posizione", "Non hai accettato i permessi")
                MessageManager.displayNoGpsPermissionEnabled(this@MapsActivity)
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