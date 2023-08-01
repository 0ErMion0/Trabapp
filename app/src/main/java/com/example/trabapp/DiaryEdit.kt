package com.example.trabapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.trabapp.databinding.ActivityDiaryEditBinding
import com.example.trabapp.databinding.ActivityMemRecoredBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.io.ByteArrayOutputStream

@SuppressLint("Range")
class DiaryEdit : AppCompatActivity() {
    lateinit var backButton: ImageButton // 뒤로 가기 버튼

    private lateinit var binding: ActivityDiaryEditBinding

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    // id 정의
    lateinit var diEditBtn: ImageButton
    lateinit var diStartDate: TextView
    lateinit var diEndDate: TextView
    lateinit var imgPic: ImageButton
    lateinit var diTitle: TextView
    lateinit var diCont: EditText

    lateinit var rdoGrpEmotion : RadioGroup
    lateinit var rdoReallyBad : RadioButton
    lateinit var rdoBad : RadioButton
    lateinit var rdoSoso : RadioButton
    lateinit var rdoGood : RadioButton
    lateinit var rdoReallyGood : RadioButton

    // db에서 가져올 변수
    lateinit var str_diTitle: String
    lateinit var str_diContents: String
    lateinit var str_diStartDate: String
    lateinit var str_diEndDate: String
    lateinit var str_diEmotion: String
    lateinit var str_memTitleForDi: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // setContentView(R.layout.activity_diary_edit)
        binding = ActivityDiaryEditBinding.inflate(layoutInflater) // Initialize the binding property
        setContentView(binding.root)

        // 사진선택 클릭 시
        binding.imgPic.setOnClickListener {
            // 갤러리 호출
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            activityResult.launch(intent)
        }

        dbManager = DBManager(this)
        sqlitedb = dbManager.writableDatabase

        val intent = intent
        str_diTitle = intent.getStringExtra("intent_diTitle").toString()

        // ---id 연결---
        diEditBtn = findViewById<ImageButton>(R.id.imgBtnEdit)
        diStartDate = findViewById<TextView>(R.id.textStartDate)
        diEndDate = findViewById<TextView>(R.id.textEndDate)
        imgPic = findViewById<ImageButton>(R.id.imgPic)
        diTitle = findViewById<TextView>(R.id.textTitle2)
        diCont = findViewById<EditText>(R.id.textDiContents)


        rdoGrpEmotion = findViewById<RadioGroup>(R.id.rdoGrpEmotion)
        rdoReallyBad = findViewById<RadioButton>(R.id.rdoReallyBad)
        rdoBad = findViewById<RadioButton>(R.id.rdoBad)
        rdoSoso = findViewById<RadioButton>(R.id.rdoSoso)
        rdoGood = findViewById<RadioButton>(R.id.rdoGood)
        rdoReallyGood = findViewById<RadioButton>(R.id.rdoReallyGood)

        // 내용 불러오기
        loadDiary()

        diStartDate.setOnClickListener {

            ////날짜 슬라이싱
            //.을 기준으로 자름
            var str_arr = diStartDate.text.toString().split(".")

            //연월일 구분
            var year = str_arr.get(0)
            var month = str_arr.get(1)
            var dayOfMonth = str_arr.get(2)

            //공백 제거
            month = month.substring(1,month.length)
            dayOfMonth = dayOfMonth.substring(1,dayOfMonth.length)

            datePopup(diStartDate, year.toInt(), month.toInt(), dayOfMonth.toInt())
        }

        diEndDate.setOnClickListener {

            ////날짜 슬라이싱
            //.을 기준으로 자름
            var str_arr = diEndDate.text.toString().split(".")

            //연월일 구분
            var year = str_arr.get(0)
            var month = str_arr.get(1)
            var dayOfMonth = str_arr.get(2)

            //공백 제거
            month = month.substring(1,month.length)
            dayOfMonth = dayOfMonth.substring(1,dayOfMonth.length)

            datePopup(diEndDate, year.toInt(), month.toInt(), dayOfMonth.toInt())
        }

        // 체크 버튼 - 편집한 내용 업데이트
        diEditBtn.setOnClickListener{
            str_diContents = diCont.text.toString()
            str_diStartDate = diStartDate.text.toString()
            str_diEndDate = diEndDate.text.toString()
            str_diEmotion = when (rdoGrpEmotion.checkedRadioButtonId) {
                R.id.rdoReallyBad -> "ReallyBad"
                R.id.rdoBad -> "Bad"
                R.id.rdoSoso -> "Soso"
                R.id.rdoGood -> "Good"
                R.id.rdoReallyGood -> "ReallyGood"
                else->""
            }

            // 이미지
            val bitmap = (imgPic.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

            val str_diImg : ByteArray = stream.toByteArray()
            stream.close()


            // Use the update method to modify the existing record with the new values
            val values = ContentValues().apply {
                put("diContents", str_diContents)
                put("diStartDate", str_diStartDate)
                put("diEndDate", str_diEndDate)
                put("diEmotion", str_diEmotion)
                put("diImg", str_diImg) // 여기 사진 관련 추가
            }

            sqlitedb.update("diaries", values, "diTitle = ?", arrayOf(str_diTitle))

            sqlitedb.close()

            //loadMemories() // 이렇게 하면 추억 목록이 2배가 되는 문제 발생
            Toast.makeText(this, "변경되었습니다.", Toast.LENGTH_SHORT).show()


            val intent: Intent = Intent(this, MemoryInfo::class.java)
            intent.putExtra("intent_memTitle", str_memTitleForDi)
            //intent.putExtra("intent_diTitle", str_diTitle)
            startActivity(intent)
        }

        // 뒤로 가기 버튼
        backButton = findViewById<ImageButton>(R.id.imgBtnBack)
        backButton.setOnClickListener {
            // 클릭되면 추억 상세 페이지로 이동 (구현해야됨)
            val intent = Intent(this, DiaryInfo::class.java)
            intent.putExtra("intent_diTitle", str_diTitle)
            startActivity(intent)
        }
    }

    // 데이터베이스 조회 후 일지 내용 추가하는 함수
    private fun loadDiary() {
        var cursor: Cursor
        cursor =
            sqlitedb.rawQuery("SELECT * FROM diaries WHERE diTitle = '" + str_diTitle + "';'", null)

        while (cursor.moveToNext()) {
            // 데이터베이스에 저장된 값 가져옴
            str_memTitleForDi = cursor.getString(cursor.getColumnIndex("memTitleForDi")).toString()
            //str_diTitle = cursor.getString(cursor.getColumnIndex("diTitle")).toString() // intent 통해 받아옴
            val str_diContents = cursor.getString(cursor.getColumnIndex("diContents")).toString()
            val str_diStartDate = cursor.getString(cursor.getColumnIndex("diStartDate")).toString()
            val str_diEndDate = cursor.getString(cursor.getColumnIndex("diEndDate")).toString()
            val str_diImg = cursor.getBlob(cursor.getColumnIndex("diImg"))
            val str_diEmotion = cursor.getString(cursor.getColumnIndex("diEmotion")).toString()

            // 저장된 값 대입
            diStartDate.text = "$str_diStartDate"
            diEndDate.text = "$str_diEndDate"

            // 이미지
            val bm = byteArrayToBitmap(str_diImg)
            imgPic.setImageBitmap(bm)

            when(str_diEmotion){
                "ReallyBad" -> rdoReallyBad.isChecked = true
                "Bad" -> rdoBad.isChecked = true
                "Soso" -> rdoSoso.isChecked = true
                "Good" -> rdoGood.isChecked = true
                "ReallyGood" -> rdoReallyGood.isChecked = true
                else->""
            }
            diTitle.setText(str_diTitle)
            diCont.setText(str_diContents)
        }

        cursor.close()
    }

    // 결과 가져오기 - 이미지
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

    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
        return bitmap
    }

    private fun datePopup(text: TextView, yearC: Int, monthC: Int, dayC: Int){
        //데이터 피커 팝업
        val cDiaryView = LayoutInflater.from(this).inflate(R.layout.date_picker_popup, null)
        val cBuilder = AlertDialog.Builder(this).setView(cDiaryView)
        val cAlerDialog = cBuilder.show()

        //데이터 피커 팝업 요소(캘린더&버튼)
        val Calendar = cDiaryView.findViewById<MaterialCalendarView>(R.id.datePicker)
        val btnConfirm = cDiaryView.findViewById<Button>(R.id.btnConfirmCalender)
        val btnCancel = cDiaryView.findViewById<Button>(R.id.btnCancelCalender)

        //시작 날짜 설정되어있던 날짜로 설정
        Calendar.setSelectedDate(CalendarDay.from(yearC, monthC-1, dayC))

        var year : Int = yearC
        var month : Int = monthC
        var day : Int = dayC

        //날짜를 옮기면(선택된 게 변하면)
        Calendar.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                year = date.year
                month = date.month
                day = date.day
            }
        })
        //확인 버튼
        btnConfirm.setOnClickListener {
            text.setText("$year. ${month+1}. $day")
            cAlerDialog.dismiss()
        }
        //취소 버튼
        btnCancel.setOnClickListener {
            cAlerDialog.dismiss()
        }
    }
}