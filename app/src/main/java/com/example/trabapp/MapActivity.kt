package com.example.trabapp

import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import java.io.IOException
import java.util.Locale
import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.net.Uri
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContextCompat.startActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import com.example.test.MySharePreferences
import com.example.trabapp.databinding.ActivityMapBinding
import com.example.trabapp.databinding.ActivityMyMemoryBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
/*
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode*/
import com.google.android.material.navigation.NavigationView

// 수정 코드(3) - 현재 위치로 이동 버튼(마커 안 찍힘), 검색 가능, 누른 위치에 마커 찍기
class MapActivity : AppCompatActivity(), OnMapReadyCallback{

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var layout: LinearLayout

    lateinit var btnShowBottomSheet : FloatingActionButton

    private val FINE_PERMISSION_CODE : Int = 1
    lateinit var currentLocation : Location
    lateinit var fusedLocationProviderClient : FusedLocationProviderClient

    private lateinit var mMap: GoogleMap // 구글 맵 불러옴
    private var marker: Marker? = null // 마커
    private lateinit var mapSearchView: SearchView // 검색창

    lateinit var supportMapFragment : SupportMapFragment

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarToggle:ActionBarDrawerToggle
    private lateinit var navView: NavigationView
    //private lateinit var toolBar : Toolbar

    private lateinit var binding: ActivityMapBinding // 레이아웃과 연결
    //lateinit var btnLogOut : Button

    override fun onCreate(savedInstanceState: Bundle?) { // initialize SupportMapFragment, SearchView
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_map)

        // 뷰 바인딩
        binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager=DBManager(this)

        // 슬라이드 메뉴
        drawerLayout = findViewById(R.id.drawerLayout)

        // Pass the ActionBarToggle action into the drawerListener
        actionBarToggle = ActionBarDrawerToggle(this, drawerLayout, 0, 0)
        drawerLayout.addDrawerListener(actionBarToggle)
        // Display the hamburger icon to launch the drawer
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // Call syncState() on the action bar so it'll automatically change to the back button when the drawer layout is open
        actionBarToggle.syncState()
        // Call findViewById on the NavigationView
        navView = findViewById(R.id.navView)

        //네비게이션
        val header = navView.getHeaderView(0)
        val btnLogOut = header.findViewById<AppCompatButton>(R.id.btnLogOut)

        // Call setNavigationItemSelectedListener on the NavigationView to detect when items are clicked
        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.myMemory -> {
                    val intent:Intent = Intent(this, MyMemory::class.java)
                    startActivity(intent)
                    true
                }
                R.id.myCalender -> {
                    val intent:Intent = Intent(this, MyCalendear::class.java)
                    startActivity(intent)
                    true
                }
                else -> {
                    false
                }
            }
        }


        // 아래로 옮김
        supportMapFragment = supportFragmentManager
            .findFragmentById(R.id.google_map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        //getLastLocation() // 마지막 위치 불러옴

        // 기존 코드
        // Set up the SearchView
        mapSearchView = findViewById(R.id.mapSearch) // mapSearch 가져옴
        mapSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(s: String): Boolean {
                // Implement the logic to search for the location and place the marker on the map
                // Use the query string to search for the location (e.g., using Geocoder)
                // Once you have the LatLng of the location, place a marker on the map

                var location = mapSearchView.getQuery().toString() // 위치 받아옴
                val geocoder = Geocoder(this@MapActivity, Locale.getDefault())
                var addressList: List<Address>? = null // attempt to get list of address - location entered 위해

                if (location.isNotEmpty()) {
                    try {
                        val addressList = geocoder.getFromLocationName(location, 1)

                        // addressList가 비지 않았고 valid location이 있다면
                        if (addressList != null){ //&& addressList.isNotEmpty()) {
                            val address = addressList[0] // take the first address from the list
                            val latLng = LatLng(
                                address.latitude,
                                address.longitude
                            ) // obtain latitude&longitude 그리고 LatLng 만듬

                            // Remove all markers
                            mMap.clear()

                            // Initialize marker options
                            val markerOptions = MarkerOptions()
                                .position(latLng)
                                .title(address.getAddressLine(0))

                            // Add marker on map
                            mMap.addMarker(markerOptions)

                            // Move the camera to the searched location
                            //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                            // 확대 너무 빨리 돼서 로딩 제대로 안되면 바꾸기
                        }
                        // 아;; 이거 수정해야겠다
                        else if(addressList == null) { // 검색어가 없는 경우
                            // Show a toast message indicating that the location is empty
                            Toast.makeText(this@MapActivity, "검색어를 입력하세요", Toast.LENGTH_LONG).show()
                        }else{ // location도

                        }
                    } catch (e: Exception) {
                        e.printStackTrace() // 예외의 스택 트레이스 출력 - 로그 캣에 예외의 상세 정보 출력함
//                            // error occurred during geocoding일 때 토스트 메시지
                        Toast.makeText(this@MapActivity, "정확한 위치를 입력해주세요", Toast.LENGTH_LONG).show()
                    }
                }
                else {
//                        // Show a toast message indicating that the location is empty
                    Toast.makeText(this@MapActivity, "정확한 위치를 입력해주세요", Toast.LENGTH_LONG).show()
                }

                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Implement any real-time search logic if needed
                return false
            }
        })

        var myInfo : Button
        //var logOut : Button
        //val btnLogOut: AppCompatButton

        myInfo = header.findViewById(R.id.btnMyInfo)
        //logOut = header.findViewById(R.id.btnLogOut)

        myInfo.setOnClickListener {
            //화면 전환
            val intent = Intent(applicationContext, MyInfo::class.java)
            startActivity(intent)
        }
        btnLogOut.setOnClickListener {
            // 로그아웃 팝업
            // Dialog
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.logout_popup, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            // 확인 버튼
            val logoutAcceptBtn = mDialogView.findViewById<Button>(R.id.logoutAcceptBtn)
            logoutAcceptBtn.setOnClickListener{
                //저장된 데이터 삭제
                MySharePreferences.clearUser(this)

                //화면 전환
                val intent = Intent(this, MainActivity::class.java)
                Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()
                startActivity(intent)

                //해당 창 닫기
                finish()
                //mAlertDialog.dismiss() // 원래 화면으로 돌아감
            }
            // 취소 버튼
            val cancelBtn = mDialogView.findViewById<Button>(R.id.cancelBtn)
            cancelBtn.setOnClickListener {
                mAlertDialog.dismiss() // 원래 화면으로 돌아감
            }
        }
        //MySharePreferences.id = intent.getStringExtra("id").toString()
    }

    // map이 사용될 준비되면 호출됨
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        //mMap.setOnMarkerClickListener(this) // 마커 여러개 찍을 수 있도록 해줌
        getLastLocation()

        // 지도 클릭했을 때 실행되는 함수
        mMap.setOnMapClickListener { latLng ->
            // When clicked on map
            // Remove all markers
            mMap.clear()

            // Initialize marker options
            val markerOptions = MarkerOptions()
            // Set position of marker
            markerOptions.position(latLng)

            // Convert the latitude and longitude into a human-readable address
            val geocoder = Geocoder(this, Locale.getDefault())
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

    // 슬라이드 메뉴: toggle clicks
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarToggle.onOptionsItemSelected(item)) {
            true // Return true if the toggle click is handled
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    // 사용자의 위치값 가져옴, 권한 요청
    private fun getLastLocation() {
        // ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION가 허용되지 않았을 경우
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한 허용 안되었을 때 권한 요청
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_PERMISSION_CODE
            )
            return
        }
        // 권한 허용이 모두 되었을 경우
        mMap.isMyLocationEnabled = true
//        var task: Task<Location> =
//            fusedLocationProviderClient.getLastLocation() // 마지막으로 알려진 위치 정보 가져옴
//        task.addOnSuccessListener { location ->
        fusedLocationProviderClient.lastLocation.addOnSuccessListener(this){location->
            if (location != null) {
                //currentLocation = location // 위치 정보 저장
                val currentLatLong = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 12f))
            }
        }
    }

    // 마커 찍어줌
    private fun placeMarkerOnMap(currentLatLong:LatLng){
        val markerOptions = MarkerOptions().position(currentLatLong)

        // Convert the latitude and longitude into a human-readable address
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses = geocoder.getFromLocation(currentLatLong.latitude, currentLatLong.longitude, 1)
        val address = addresses?.firstOrNull()?.getAddressLine(0)

        // Set title of marker
        //markerOptions.title("${latLng.latitude} : ${latLng.longitude}")
        markerOptions.title(address ?: "UnKnown Location")
        mMap.addMarker(markerOptions)
    }

    // 마커 눌렀을 경우, 밑에 창 올라오면서 일지 추가할 수 있도록 해줌
    //private fun onMarkerClick(p0:Marker?):Boolean = false
    private fun onMarkerClick(p0:Marker?){

    }

    // 권한 요청 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            FINE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 허용 되었을 때
                    getLastLocation()
                } else {
                    // 권한이 거부되었을 때
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    ) {
                        // 사용자가 "다시 묻지 않기"를 체크한 경우
                        Toast.makeText(
                            this@MapActivity,
                            "위치 권한이 거부되었습니다. 앱 설정으로 이동하여 위치 권한을 허용해주세요.",
                            Toast.LENGTH_LONG
                        ).show()
                        openAppSettings() // 앱 설정으로 이동
                    } else {
                        // 사용자가 "다시 묻지 않기"를 체크하지 않은 경우
                        Toast.makeText(
                            this@MapActivity,
                            "위치에 액세스 하도록 허용해주세요",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    // 앱 설정으로 이동
    private fun openAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.fromParts("package", packageName, null)
        startActivity(intent)
    }
}
