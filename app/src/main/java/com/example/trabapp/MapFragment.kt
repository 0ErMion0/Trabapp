package com.example.trabapp

/*


사용하지 않는 코드입니다.



 */


import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.util.Locale

class MapFragment : Fragment() {

    private lateinit var mMap: GoogleMap // 수정 코드(1)
    private var marker: Marker? = null
    private lateinit var mapSearchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.fragment_map)


    }
    // 기존 코드
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 지도 프래그먼트 불러옴
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        val supportMapFragment =
            childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment

        // 수정 코드(1)
        // Async map
        supportMapFragment.getMapAsync { googleMap ->
            mMap = googleMap

            // Add a marker in Sydney and move the camera
            val SEOUL = LatLng(37.556, 126.97)
            mMap.addMarker(
                // 나중에 자기 위치 기반으로 시작 마커 나오도록 바꾸기, 확대도!
                MarkerOptions()
                    .position(SEOUL)
                    .title("Marker in SEOUL")
                    .snippet("한국 수도")
            )
            mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL))

            // Set up the OnMapClickListener
            mMap.setOnMapClickListener { latLng ->
                // When clicked on map
                // Remove all markers
                mMap.clear()

                // Initialize marker options
                val markerOptions = MarkerOptions()
                // Set position of marker
                markerOptions.position(latLng)

                // Convert the latitude and longitude into a human-readable address
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                val address = addresses?.firstOrNull()?.getAddressLine(0)

                // Set title of marker
                //markerOptions.title("${latLng.latitude} : ${latLng.longitude}")
                markerOptions.title(address ?: "UnKnown Location")
                // Remove all markers
                mMap.clear()
                // Animating to zoom the marker
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                // 만약 이렇게 애니메이션 넣었는데 지도 바로 불러와지지 않는 오류 계속 나면
                //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng)) // 이걸로 바꾸기

                // Add marker on map
                mMap.addMarker(markerOptions)
            }
        }

        // Return view
        return view
    }
}