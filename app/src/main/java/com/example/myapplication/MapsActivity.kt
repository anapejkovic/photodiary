package com.example.myapplication

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var store: FirebaseFirestore
    private  lateinit var documentReference: DocumentReference
    private  var currentLatLng : LatLng? = null

     // lateinit var mapFragment: SupportMapFragment

    private lateinit var saveBtn : com.google.android.material.floatingactionbutton.FloatingActionButton
    private var flag = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        btnSaveLocation.setOnClickListener {
            if(currentLatLng == null)
                Toast.makeText(this, "We are searching for location", Toast.LENGTH_SHORT).show()
            else {
                val gcd = Geocoder(this, Locale.getDefault())
                val addresses: List<Address> =
                    gcd.getFromLocation(currentLatLng!!.latitude, currentLatLng!!.longitude, 1)
                if (addresses.size > 0) {
                    System.out.println(addresses[0].getLocality())
                    Toast.makeText(this, addresses[0].getLocality(), Toast.LENGTH_SHORT).show()

                    val resultIntent = Intent()

                    resultIntent.putExtra("location_key", addresses[0].getLocality())
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()

                } else {
                    // do your stuff
                }
            }
        }

        getPlaces()

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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
    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
//        mMap.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->

            if (location != null) {
                lastLocation = location
                currentLatLng = LatLng(location.latitude, location.longitude)


               /** if(flag){

                    saveLocation(location.latitude, location.longitude)

                } */
                placeMarkerOnMap(currentLatLng!!)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))

            }
        }
    }
    private fun placeMarkerOnMap(location: LatLng) {

        val markerOptions = MarkerOptions().position(location)
        mMap.addMarker(markerOptions)
    }

    override fun onMarkerClick(p0: Marker?) = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        setUpMap()
    }


    private fun getPlaces(){
        store= FirebaseFirestore.getInstance()
        store.collection("location").get() .addOnSuccessListener { document ->
            for (documents in document){
                var date = documents.data["date"].toString()
                var latitude = documents.data["latitude"].toString().toDouble()
                var longitude = documents.data["longitude"].toString().toDouble()
                var name = documents.data["name"].toString()


            }
        }
    }


    private fun getDaysAgo(daysAgo: Int): Date {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -daysAgo)
        return calendar.time
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}



