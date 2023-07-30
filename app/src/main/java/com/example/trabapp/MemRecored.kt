package com.example.trabapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.trabapp.databinding.ActivityMemRecoredBinding
import java.io.ByteArrayOutputStream


class MemRecored : AppCompatActivity() {
    lateinit var backButton: ImageButton // 뒤로 가기 버튼

    private lateinit var binding:ActivityMemRecoredBinding

    // DB
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    // 변수 선언
    lateinit var imgPic : ImageView
    lateinit var edtTextTitle:EditText
    lateinit var btnCalenderStart:Button
    lateinit var btnCalenderEnd:Button
    lateinit var editTextContents:EditText
    lateinit var imgBtnCheck : ImageButton
    lateinit var btnPicSelect : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_mem_recored)
        binding = ActivityMemRecoredBinding.inflate(layoutInflater) // Initialize the binding property
        setContentView(binding.root)
        dbManager = DBManager(this)

        // id 연결
        imgPic = findViewById(R.id.imgPic)
        edtTextTitle = findViewById(R.id.edtTextTitle)
        btnCalenderStart = findViewById(R.id.btnCalenderStart)
        btnCalenderEnd = findViewById(R.id.btnCalenderEnd)
        editTextContents = findViewById(R.id.editTextContents)
        imgBtnCheck = findViewById(R.id.imgBtnCheck)
        btnPicSelect = findViewById(R.id.btnPicSelect)

        // 사진 선택 클릭 시
        binding.btnPicSelect.setOnClickListener {
            // 갤러리 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }

        // 체크 버튼 클릭 시
        imgBtnCheck.setOnClickListener(){
            // db에 들어갈 변수 선언
            val str_title :String = edtTextTitle.text.toString()
            val str_contents :String = editTextContents.text.toString()
            val str_startDate : String = "2023 - 07 - 02"
            val str_endDate : String = "2023 - 07 - 20"

            // 이미지
            val bitmap = (imgPic.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

            val str_pic : ByteArray = stream.toByteArray()
            stream.close()


            // DB에 저장
            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL("INSERT INTO diaries VALUES ('"+str_title+"','"+str_contents+"', '"+str_startDate+"' , '"+str_endDate+"','"+str_pic+"')")
            sqlitedb.close()

            val intent: Intent = Intent(this, MemoryInfo::class.java)
//            intent.putExtra("intent_name", str_title)
            startActivity(intent)
        }



        // 뒤로 가기 버튼
        backButton = findViewById<ImageButton>(R.id.imgBtnBack)
        backButton.setOnClickListener{
            // 클릭되면 MapActivity로 이동
            val intent: Intent = Intent(this, MemoryInfo::class.java)
            startActivity(intent)
        }
    } // onCreate


    // 결과 가져오기
    private val activityResult : ActivityResultLauncher<Intent> = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK && it.data != null){
        // 값 담기
        val uri = it.data!!.data

        // 화면에 보여주기
        Glide.with(this)
            .load(uri) // 이미지
            .into(binding.imgPic) // 보여줄 위치
        }
    }

    // 이미지를 바이트로 쪼개기
//    private fun drawableToByteArray(drawable: Bitmap):ByteArray?{
//        val bitmapDrawable = drawable as BitmapDrawable?
//        val bitmap = bitmapDrawable?.bitmap
//        val stream = ByteArrayOutputStream()
//        bitmap?.compress(Bitmap.CompressFormat.PNG, 100,stream)
//        val byteArray = stream.toByteArray()
//
//        return byteArray
//    }
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }


}