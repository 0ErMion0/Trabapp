package com.example.trabapp

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.example.trabapp.databinding.ActivityMemRecoredBinding
import java.io.FileNotFoundException


class MemRecored : AppCompatActivity() {
    lateinit var backButton: ImageButton // 뒤로 가기 버튼

    private lateinit var binding:ActivityMemRecoredBinding

    // DB
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    // 변수 선언
    lateinit var imgBtnPic : ImageButton
    lateinit var edtTextTitle:EditText
    lateinit var btnCalenderStart: AppCompatButton
    lateinit var btnCalenderEnd: AppCompatButton
    lateinit var editTextContents:EditText
    lateinit var imgBtnCheck : ImageButton

    private lateinit var memTitleForDi : String
    private lateinit var str_diTitle : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_mem_recored)
        binding = ActivityMemRecoredBinding.inflate(layoutInflater) // Initialize the binding property
        setContentView(binding.root)

        dbManager = DBManager(this)
        memTitleForDi = intent.getStringExtra("intent_memTitleForDi").toString() // 플로팅 버튼을 통한 일지 추가 접근
        // 이 제목과 일치하는걸 가진 행에 다른 내용 추가할 수 있도록 해야 함

        // 일지 목록에서 특정 일지 선택했을 때 어떤 일지 보여줄지 알려주는 용도 가 아니네 여기선 편집만 하니까!
        //str_diTitle = intent.getStringExtra("intent_diTitle").toString()

        // id 연결
        imgBtnPic = findViewById(R.id.imgBtnPic)
        edtTextTitle = findViewById(R.id.edtTextTitle)
        btnCalenderStart = findViewById(R.id.btnCalenderStart)
        btnCalenderEnd = findViewById(R.id.btnCalenderEnd)
        editTextContents = findViewById(R.id.editTextContents)
        imgBtnCheck = findViewById(R.id.imgBtnCheck)

        // 이미지버튼 클릭 시
        binding.imgBtnPic.setOnClickListener {
            // 갤러리 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }

        // 체크 버튼 클릭 시 - 데이터 추가
        imgBtnCheck.setOnClickListener(){
            // db에 들어갈 변수 선언
            str_diTitle = edtTextTitle.text.toString()
            //val str_diTitle :String = edtTextTitle.text.toString()
            val str_diContents :String = editTextContents.text.toString()
            val str_diStartDate : String = "2023 - 07 - 02"
            val str_diEndDate : String = "2023 - 07 - 20"
            val str_diImg : String = "이미지.."
            // 이미지는 어떻게........????

            // DB에 저장
            sqlitedb = dbManager.writableDatabase
////            sqlitedb.execSQL("INSERT INTO diaries VALUES ('"str_title"','$str_contents', '$str_startDate' , '$str_endDate')")
////            sqlitedb.close()
//            sqlitedb.execSQL("INSERT INTO diaries (diTitle, diContents, diStartDate, diEndDate, diImg) VALUES ('"
//                    +str_diTitle+"','"+str_diContents+"', "+str_diStartDate+" , '"+str_diEndDate+"', '"+str_diImg+"')")
//            sqlitedb.close()
            val sql = "INSERT INTO diaries (memTitleForDi, diTitle, diContents, diStartDate, diEndDate, diImg) " +
                    "VALUES (?, ?, ?, ?, ?, ?);"

            val values = arrayOf(memTitleForDi, str_diTitle, str_diContents, str_diStartDate, str_diEndDate, str_diImg)
            sqlitedb.execSQL(sql, values)

            // 추억 상세 페이지로 데이터 전달 및 이동
            val intent: Intent = Intent(this, MemoryInfo::class.java)
            intent.putExtra("intent_diTitle", str_diTitle)
            intent.putExtra("intent_memTitle", memTitleForDi)
            startActivity(intent)

//            //GPT
//            setResult(Activity.RESULT_OK, intent)
//            finish()
        }

        // 뒤로 가기 버튼
        backButton = findViewById<ImageButton>(R.id.imgBtnBack)
        backButton.setOnClickListener{
            // 클릭되면 MapActivity로 이동
            val intent: Intent = Intent(this, MemoryInfo::class.java)
            intent.putExtra("intent_title", memTitleForDi)
            startActivity(intent)
        }
    } // onCreate


    // 결과 가져오기 - 이미지?
    private val activityResult : ActivityResultLauncher<Intent> = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK && it.data != null){
        // 값 담기
        val uri = it.data!!.data

        // 화면에 보여주기
        Glide.with(this)
            .load(uri) // 이미지
            .into(binding.imgBtnPic) // 보여줄 위치
        }
    }
}