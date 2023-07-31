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


//class MapActivity : AppCompatActivity() {
//    // 기존 코드(확정)
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_map)
//
//        // 기존 코드
//        // Initialize fragment
//        val fragment = MapFragment()
//
//        // Open fragment
//        supportFragmentManager.beginTransaction()
//            .replace(R.id.frame_layout, fragment)
//            .commit()
//    }
//}

//    // 수정 코드(2) - MapFragment 사용x
//    class MapActivity : AppCompatActivity(), OnMapReadyCallback {
//        private val FINE_PERMISSION_CODE : Int = 1
//        lateinit var currentLocation : Location
//        lateinit var fusedLocationProviderClient : FusedLocationProviderClient
//
//        private lateinit var mMap: GoogleMap // 구글 맵 불러옴
//        private var marker: Marker? = null // 마커
//        private lateinit var mapSearchView: SearchView // 검색창
//
//        lateinit var supportMapFragment : SupportMapFragment
//        override fun onCreate(savedInstanceState: Bundle?) { // initialize SupportMapFragment, SearchView
//            super.onCreate(savedInstanceState)
//            setContentView(R.layout.activity_map)
//
//            //            // 아래로 옮김
//            supportMapFragment = supportFragmentManager
//                .findFragmentById(R.id.google_map) as SupportMapFragment
//
//            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
//            getLastLocation() // 마지막 위치 불러옴
//
//
////            // Async map -> 아래로 옮김
//            supportMapFragment.getMapAsync(this)
//
//
//            // Set up the SearchView
//            mapSearchView = findViewById(R.id.mapSearch) // mapSearch 가져옴
//            mapSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                override fun onQueryTextSubmit(s: String): Boolean {
//                    // Implement the logic to search for the location and place the marker on the map
//                    // Use the query string to search for the location (e.g., using Geocoder)
//                    // Once you have the LatLng of the location, place a marker on the map
//
//                    var location = mapSearchView.getQuery().toString() // 위치 받아옴
//                    val geocoder = Geocoder(this@MapActivity, Locale.getDefault())
//                    var addressList: List<Address>? = null // attempt to get list of address - location entered 위해
//
//                    if (location.isNotEmpty()) {
//                        try {
//                            val addressList = geocoder.getFromLocationName(location, 1)
//
//                            // addressList가 비지 않았고 valid location이 있다면
//                            if (addressList != null){ //&& addressList.isNotEmpty()) {
//                                val address = addressList[0] // take the first address from the list
//                                val latLng = LatLng(
//                                    address.latitude,
//                                    address.longitude
//                                ) // obtain latitude&longitude 그리고 LatLng 만듬
//
//                                // Remove all markers
//                                mMap.clear()
//
//                                // Initialize marker options
//                                val markerOptions = MarkerOptions()
//                                    .position(latLng)
//                                    .title(address.getAddressLine(0))
//
//                                // Add marker on map
//                                mMap.addMarker(markerOptions)
//
//                                // Move the camera to the searched location
//                                //mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
//                                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
//                                // 확대 너무 빨리 돼서 로딩 제대로 안되면 바꾸기
//                            }
//                            // 아;; 이거 수정해야겠다
//                            else if(addressList == null) { // 검색어가 없는 경우
//                                // Show a toast message indicating that the location is empty
//                                Toast.makeText(this@MapActivity, "검색어를 입력하세요", Toast.LENGTH_LONG).show()
//                            }else{ // location도
//
//                            }
//                        } catch (e: Exception) {
//                            e.printStackTrace() // 예외의 스택 트레이스 출력 - 로그 캣에 예외의 상세 정보 출력함
////                            // error occurred during geocoding일 때 토스트 메시지
//                            Toast.makeText(this@MapActivity, "정확한 위치를 입력해주세요", Toast.LENGTH_LONG).show()
//                        }
//                    }
//                    else {
////                        // Show a toast message indicating that the location is empty
//                        Toast.makeText(this@MapActivity, "정확한 위치를 입력해주세요", Toast.LENGTH_LONG).show()
//                    }
//
//                    return false
//                }
//
//                override fun onQueryTextChange(newText: String): Boolean {
//                    // Implement any real-time search logic if needed
//                    return false
//                }
//            })
//
//            // Async map
//            //supportMapFragment.getMapAsync(this)
//        }
//
//
//        // 사용자의 위치값 가져옴, 권한 요청
//        private fun getLastLocation() {
//            // ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION가 허용되지 않았을 경우
//            if (ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_FINE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//                && ActivityCompat.checkSelfPermission(
//                    this,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                // 권한 허용 안되었을 때 권한 요청
//                ActivityCompat.requestPermissions(
//                    this,
//                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
//                    FINE_PERMISSION_CODE
//                )
//                return
//            }
//            // 권한 허용이 모두 되었을 경우
//            var task: Task<Location> =
//                fusedLocationProviderClient.getLastLocation() // 마지막으로 알려진 위치 정보 가져옴
//            task.addOnSuccessListener { location ->
//                if (location != null) {
//                    //currentLocation = location // 위치 정보 저장
//                    val latLng = LatLng(location.latitude, location.longitude)
////                    supportMapFragment = supportFragmentManager
////                        .findFragmentById(R.id.google_map) as SupportMapFragment // 구글 맵 사용 준비
//
////                    // 구글 맵에 현재 위치에 마커 추가
////                    mMap.addMarker(MarkerOptions().position(SEOUL).title("현재 위치"))
////                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL))
////                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(SEOUL, 10f))
//
//                    // Initialize the map and add the marker
////                    supportMapFragment =
////                        supportFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
////                    supportMapFragment.getMapAsync { googleMap ->
////                        mMap = googleMap
////                        mMap.clear() // Remove any existing markers
////                        mMap.addMarker(MarkerOptions().position(latLng).title("현재 위치"))
////                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
////                    }
//
//                    mMap.addMarker(
//                        // 나중에 자기 위치 기반으로 시작 마커 나오도록 바꾸기, 확대도!
//                        MarkerOptions()
//                            .position(latLng)
//                            .title("현재 위치")
//                    )
//                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
//
//                }
//            }
//        }
//
//            // 권한 요청 처리
//            override fun onRequestPermissionsResult(
//                requestCode: Int,
//                permissions: Array<String>,
//                grantResults: IntArray
//            ) {
//                super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//
//                when (requestCode) {
//                    FINE_PERMISSION_CODE -> {
//                        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                            // 권한 허용 되었을 때
//                            getLastLocation()
//                        } else {
//                            // 권한이 거부되었을 때
//                            if (!ActivityCompat.shouldShowRequestPermissionRationale(
//                                    this,
//                                    Manifest.permission.ACCESS_FINE_LOCATION
//                                )
//                            ) {
//                                // 사용자가 "다시 묻지 않기"를 체크한 경우
//                                Toast.makeText(
//                                    this@MapActivity,
//                                    "위치 권한이 거부되었습니다. 앱 설정으로 이동하여 위치 권한을 허용해주세요.",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                                openAppSettings() // 앱 설정으로 이동
//                            } else {
//                                // 사용자가 "다시 묻지 않기"를 체크하지 않은 경우
//                                Toast.makeText(
//                                    this@MapActivity,
//                                    "위치에 액세스 하도록 허용해주세요",
//                                    Toast.LENGTH_LONG
//                                ).show()
//                            }
//                        }
//                    }
//                }
//            }
//
//            // 앱 설정으로 이동
//            private fun openAppSettings() {
//                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//                intent.data = Uri.fromParts("package", packageName, null)
//                startActivity(intent)
//            }
//
//
//            // map이 사용될 준비되면 호출됨
//            override fun onMapReady(googleMap: GoogleMap) {
//
//                mMap = googleMap
//
////            // 기본 위치 마커(일단은.. 나중에 사용자 위치 중심으로 마커 찍히도록 하기)
////            val SEOUL = LatLng(37.556, 126.97)
////            mMap.addMarker(
////                // 나중에 자기 위치 기반으로 시작 마커 나오도록 바꾸기, 확대도!
////                MarkerOptions()
////                    .position(SEOUL)
////                    .title("Marker in SEOUL")
////                    .snippet("한국 수도")
////            )
////            mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL))
//
////            // 위치 기반으로 기본 마커 찍히도록 - 오류남..
////            val SEOUL = currentLocation.let {
////                LatLng(it.latitude, it.longitude)
////            }
////            mMap.addMarker(
////                // 나중에 자기 위치 기반으로 시작 마커 나오도록 바꾸기, 확대도!
////                MarkerOptions()
////                    .position(SEOUL)
////                    .title("현재 위치")
////                    //.snippet("한국 수도")
////            )
////            mMap.moveCamera(CameraUpdateFactory.newLatLng(SEOUL))
//
//
//
//                // 지도 클릭했을 때 실행되는 함수
//                mMap.setOnMapClickListener { latLng ->
//                    // When clicked on map
//                    // Remove all markers
//                    mMap.clear()
//
//                    // Initialize marker options
//                    val markerOptions = MarkerOptions()
//                    // Set position of marker
//                    markerOptions.position(latLng)
//
//                    // Convert the latitude and longitude into a human-readable address
//                    val geocoder = Geocoder(this, Locale.getDefault())
//                    val addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
//                    val address = addresses?.firstOrNull()?.getAddressLine(0)
//
//                    // Set title of marker
//                    //markerOptions.title("${latLng.latitude} : ${latLng.longitude}")
//                    markerOptions.title(address ?: "UnKnown Location")
//                    // Remove all markers
//                    mMap.clear()
//                    // Animating to zoom the marker
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10f))
//                    // 만약 이렇게 애니메이션 넣었는데 지도 바로 불러와지지 않는 오류 계속 나면
//                    //mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng)) // 이걸로 바꾸기
//
//                    // Add marker on map
//                    mMap.addMarker(markerOptions)
//                }
//            }
//}

// 수정 코드(3) - 현재 위치로 이동 버튼(마커 안 찍힘), 검색 가능, 누른 위치에 마커 찍기
class MapActivity : AppCompatActivity(), OnMapReadyCallback{

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    //lateinit var layout: LinearLayout // 레이아웃 넣을 바텀시트의 부분

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

    lateinit var btnMyInfo : Button
    lateinit var btnLogOut : Button

    override fun onCreate(savedInstanceState: Bundle?) { // initialize SupportMapFragment, SearchView
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        dbManager=DBManager(this)

        // 바텀 시트 버튼
        btnShowBottomSheet = findViewById(R.id.btmsheetbtn)
        btnShowBottomSheet.setOnClickListener(){

            //loadMemories()

            //layout = findViewById(R.id.memListLayout)

            val dialog = BottomSheetDialog(this)
            val view = layoutInflater.inflate(R.layout.bottom_sheet, null)
            val addmembtn = view.findViewById<Button>(R.id.addMemBtn)

            // 바텀 시트 - 추억 추가 버튼
            addmembtn.setOnClickListener(){
                //Toast.makeText(this, "추억 추가", Toast.LENGTH_SHORT).show()
                // Dialog
                val mDialogView = LayoutInflater.from(this).inflate(R.layout.mem_add_popup, null)
                val mBuilder = AlertDialog.Builder(this)
                    .setView(mDialogView)
                val mAlertDialog = mBuilder.show()

                // ---팝업창 id 연결---
                val newGroupName = mDialogView.findViewById<EditText>(R.id.newGroupName)
                val edtTextMember = mDialogView.findViewById<EditText>(R.id.edtTextMember)
                val startDate = mDialogView.findViewById<TextView>(R.id.startDate)
                val startDateIconBtn = mDialogView.findViewById<ImageView>(R.id.startDateIconBtn)
                val endDate = mDialogView.findViewById<TextView>(R.id.endDate)
                val endDateIconButton = mDialogView.findViewById<ImageView>(R.id.endDateIconBtn)
                val rdoGrpColor = mDialogView.findViewById<RadioGroup>(R.id.rdoGrpColor)
                val rdoRed = mDialogView.findViewById<RadioButton>(R.id.rdoRed)
                val rdoOrange = mDialogView.findViewById<RadioButton>(R.id.rdoOrange)
                val rdoGreen = mDialogView.findViewById<RadioButton>(R.id.rdoGreen)
                val rdoMint = mDialogView.findViewById<RadioButton>(R.id.rdoMint)
                val rdoBlue = mDialogView.findViewById<RadioButton>(R.id.rdoBlue)
                val rdoPurple = mDialogView.findViewById<RadioButton>(R.id.rdoPurple)

                // 확인 버튼
                val okButton = mDialogView.findViewById<Button>(R.id.addGroupAcceptBtn)
                okButton.setOnClickListener {
                    // 확인 버튼 누르면
                    // <<해야됨>> 모든 빈칸이 다 채워졌을 경우
                    // db에 전달 및 저장

                    // db에 보낼 변수들 정의
                    var str_memTitle: String = newGroupName.text.toString() // 추억 제목
                    var str_memMb: String = edtTextMember.text.toString() // 멤버
                    var str_startDate: String = startDate.text.toString() // 시작일
                    var str_endDate: String = endDate.text.toString() // 마감일
                    var str_memColor: String = "" // 기록 색
                    var str_memTitleForDi: String = newGroupName.text.toString() // 일지에서 쓸 추억 제목

                    // 라디오버튼
                    if (rdoGrpColor.checkedRadioButtonId == R.id.rdoRed) {           // 빨강(핑크)
                        str_memColor = "pink"
                    } else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoOrange) {   // 주황
                        str_memColor = "orange"
                    } else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoGreen) {    // 초록
                        str_memColor = "green"
                    } else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoMint) {    // 민트
                        str_memColor = "mint"
                    } else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoBlue) {    // 파랑
                        str_memColor = "blue"
                    } else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoPurple) {  // 보라
                        str_memColor = "purple"
                    }

                    sqlitedb = dbManager.writableDatabase
                    sqlitedb.execSQL("INSERT INTO memories VALUES ('" + str_memTitle + "','" + str_memMb + "', " + str_startDate + " , '" + str_endDate + "', '" + str_memColor + "')")
                    sqlitedb.execSQL("INSERT INTO diaries (memTitleForDi) VALUES ('str_memTitle')")
                    sqlitedb.close()


                    // 내 추억,추억 상세로 데이터 전달
                    val intentMyMemory = Intent(this, MyMemory::class.java)
                    intentMyMemory.putExtra("intent_title", str_memTitle)

                    Toast.makeText(this, "내 추억을 작성했습니다", Toast.LENGTH_SHORT).show()

                    startActivity(intentMyMemory)
                }
                val noButton = mDialogView.findViewById<Button>(R.id.btnCancel)
                noButton.setOnClickListener {
                    mAlertDialog.dismiss() // 원래 화면으로 돌아감
                }
            }
            dialog.setContentView(view)
            dialog.show()
        }

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

        // Async map
        //supportMapFragment.getMapAsync(this)

        //네비게이션
        var header = navView.getHeaderView(0)

        var myInfo : Button
        var logOut : Button

        myInfo = header.findViewById(R.id.btnMyInfo)
        logOut = header.findViewById(R.id.btnLogOut)

        myInfo.setOnClickListener {
            //화면 전환
            val intent = Intent(applicationContext, MyInfo::class.java)
            startActivity(intent)
        }
        logOut.setOnClickListener {
            //저장된 데이터 삭제
            MySharePreferences.clearUser(this)

            //화면 전환
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

            //해당 창 닫기
            finish()
        }
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

//    @SuppressLint("Range")
//    private fun loadMemories() {
//        //dbManager = DBManager(this, "memories", null, 1)
//        sqlitedb = dbManager.readableDatabase
//
//        val cursor = dbManager.getAllMemories()
//
//        var num: Int = 0
//        while (cursor.moveToNext()) {
//            // 데이터베이스에 저장된 값 가져옴
//            val str_diTitle = cursor.getString(cursor.getColumnIndex("memTitle")).toString()
//            val str_diContents = cursor.getString(cursor.getColumnIndex("diContents")).toString()
//
//            // Inflate memory_item_layout.xml for each memory item
//            val memoryItemView = layoutInflater.inflate(R.layout.memory_item_layout, null)
//
//            // Find views in the memory_item_layout
//            //val groupButton = memoryItemView.findViewById<CardView>(R.id.groupButton)
//            val groupColor = memoryItemView.findViewById<ImageView>(R.id.groupColor)
//            val groupName = memoryItemView.findViewById<TextView>(R.id.groupName)
//            val groupDate = memoryItemView.findViewById<TextView>(R.id.groupDate)
//
//            memoryItemView.id = num // 목록 번호
//
//            // Set data from the database to the views
//            groupName.text = str_memTitle // 그룹 이름 설정
//            groupDate.text = "$str_startDate ~ $str_endDate" // 날짜 설정
//            //groupName.setText(str_memTitle) // 그룹 이름 설정
//            //groupDate.setText("$str_startDate ~ $str_endDate") // 날짜 설정
//
//            // Set the color of ImageView based on the value from the database
//            when (str_memColor) {
//                "pink" -> groupColor.setImageResource(R.drawable.circle_red)
//                "orange" -> groupColor.setImageResource(R.drawable.circle_orange)
//                "green" -> groupColor.setImageResource(R.drawable.circle_green)
//                "mint" -> groupColor.setImageResource(R.drawable.circle_mint)
//                "blue" -> groupColor.setImageResource(R.drawable.circle_blue)
//                "purple" -> groupColor.setImageResource(R.drawable.circle_purple)
//                // Add more cases for other colors if needed
//                //else -> groupColor.setImageResource(R.drawable.default_circle)
//            }
//
//            // 레이아웃 클릭하면 추억 기록으로 이동 및 데이터 전달
//            memoryItemView.setOnClickListener {
//                val intent = Intent(this, MemoryInfo::class.java)
//                intent.putExtra("intent_memTitle", str_memTitle)
//                startActivity(intent)
//            }
//
//            // Add the memory item view to LinearLayout
//            layout.addView(memoryItemView)
//            num++;
//        }
//    }
}
