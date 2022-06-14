package com.example.mcdonalds.activity

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await


@Suppress("OPT_IN_IS_NOT_ENABLED")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback{

    private lateinit var mMap: GoogleMap
    private lateinit var mapView : FragmentContainerView
    private lateinit var question : TextView
    private lateinit var binding: ActivityMapsBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var select : Button

    companion object{
        const val PRECISION_MODULE = 30
    }

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
            this.hideAllComponents()
            Log.d("finish", "Cambio to finishFragment")
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
        this.mMap = googleMap
        getCurrentLocation()

        this.mMap.setOnMapClickListener {
            this.getPlaceDetailsFromCoordinates(it)
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
                    this.fusedLocationProviderClient.requestLocationUpdates(this.locationRequest(), this.locationCallBack(), Looper.getMainLooper())
                    this.fusedLocationProviderClient.removeLocationUpdates(this.locationCallBack())
                    updateMapView(LatLng(location.latitude, location.longitude))
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

    @OptIn(DelicateCoroutinesApi::class)
    private fun getPlaceDetailsFromCoordinates(point : LatLng){
        GlobalScope.launch(Dispatchers.IO){
            val db = Firebase.firestore
            var mcLocation : McLocation? = null
            var name  = ""
            db.collection("places")
                .get()
                .addOnSuccessListener {
                    for (document in it){
                        name = document["name"] as String
                        val coordinates = LatLng(document["latitude"] as Double,document["longitude"] as Double)
                        if(computeModule(coordinates, point) <= PRECISION_MODULE){
                            mcLocation = McLocation(name, coordinates)

                            Log.d("location", "Setto la location $mcLocation")
                            McOrder.location = mcLocation!!
                        }
                    }
                }
                .addOnFailureListener {
                    MessageManager.displayNoPositionComputable(this@MapsActivity)
                }
                .await()

            withContext(Dispatchers.Main){
                if(mcLocation != null){
                    Snackbar.make(mapView, "$name è stato selezionato", Snackbar.LENGTH_SHORT).show()
                    //Make button select place enabled
                    select.isEnabled = true
                }else{
                    //Make button select place disabled
                    select.isEnabled = false
                    Toast.makeText(this@MapsActivity, "Questo luogo al momento non è disponibile", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun computeModule(p0 : LatLng, p1 : LatLng) : Int{
        val startPoint  = Location("startPoint")
        startPoint.latitude = p0.latitude
        startPoint.longitude = p0.longitude

        val endPoint = Location("endPoint")
        endPoint.latitude = p1.latitude
        endPoint.longitude = p1.longitude

        return startPoint.distanceTo(endPoint).toInt()
    }

    override fun onResume() {
        super.onResume()

        if(McOrder.location != null){
            this.hideAllComponents()
            FragmentUtils.changeToFinishFragment(this@MapsActivity, CompleteOrderFragment(), "CompleteOrderFragment")
        }
    }

    override fun onStop() {
        super.onStop()

        this.fusedLocationProviderClient.removeLocationUpdates(this.locationCallBack())
    }

    override fun onDestroy() {
        super.onDestroy()

        this.fusedLocationProviderClient.removeLocationUpdates(this.locationCallBack())
    }
}