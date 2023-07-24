package com.example.trabapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Initialize fragment
        val fragment = MapFragment()

        // Open fragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.frame_layout, fragment)
            .commit()
    }


//    private lateinit var mMap: GoogleMap
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
//        val mapFragment = supportFragmentManager
//            .findFragmentById(R.id.map) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//    }
//
//    /**
//     * Manipulates the map once available.
//     * This callback is triggered when the map is ready to be used.
//     * This is where we can add markers or lines, add listeners or move the camera. In this case,
//     * we just add a marker near Sydney, Australia.
//     * If Google Play services is not installed on the device, the user will be prompted to install
//     * it inside the SupportMapFragment. This method will only be triggered once the user has
//     * installed Google Play services and returned to the app.
//     */
//    override fun onMapReady(googleMap: GoogleMap) {
//        mMap = googleMap
//
//        // Add a marker in Sydney and move the camera
//        val SEOUL  = LatLng(37.556, 126.97)
//        mMap.addMarker(
//            MarkerOptions()
//            .position(SEOUL)
//            .title("Marker in SEOUL")
//                .snippet("한국 수도")
//        )
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL))
//    }
}