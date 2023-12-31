package com.example.trabapp

import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.icu.util.Calendar
import android.media.Image
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import com.example.trabapp.databinding.ActivityMemRecoredBinding
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import org.w3c.dom.Text
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException


class MemRecored : AppCompatActivity() {
    lateinit var backButton: ImageButton // 뒤로 가기 버튼

    private lateinit var binding:ActivityMemRecoredBinding

    // DB
    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    // 변수 선언
    lateinit var edtTextTitle:EditText
    lateinit var startDate : TextView
    lateinit var btnCalenderStart: ImageButton
    lateinit var endDate : TextView
    lateinit var btnCalenderEnd: ImageButton
    lateinit var editTextContents:EditText
    lateinit var imgBtnCheck : ImageButton
    lateinit var rdoGrpEmotion : RadioGroup
    lateinit var rdoReallyBad : RadioButton
    lateinit var rdoBad : RadioButton
    lateinit var rdoSoso : RadioButton
    lateinit var rdoGood : RadioButton
    lateinit var rdoReallyGood : RadioButton
    lateinit var imgBtnPic : ImageButton


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
        edtTextTitle = findViewById(R.id.edtTextTitle)
        btnCalenderStart = findViewById(R.id.startDateIconBtn)
        btnCalenderEnd = findViewById(R.id.endDateIconBtn)
        editTextContents = findViewById(R.id.editTextContents)
        imgBtnCheck = findViewById(R.id.imgBtnCheck)
        imgBtnPic = findViewById(R.id.imgBtnPic)
        startDate = findViewById(R.id.startDate)
        endDate = findViewById(R.id.endDate)

        rdoGrpEmotion = findViewById(R.id.rdoGrpEmotion)
        rdoReallyBad = findViewById(R.id.rdoReallyBad)
        rdoBad = findViewById(R.id.rdoBad)
        rdoSoso = findViewById(R.id.rdoSoso)
        rdoGood = findViewById(R.id.rdoGood)
        rdoReallyGood = findViewById(R.id.rdoReallyGood)

        //텍스트 오늘 날짜로 설정
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        startDate.text = "$year. ${month+1}. $dayOfMonth"
        endDate.text = "$year. ${month+1}. $dayOfMonth"

        btnCalenderStart.setOnClickListener{
            datePopup(startDate, year, month, dayOfMonth)
        }
        btnCalenderEnd.setOnClickListener {
            datePopup(endDate, year, month, dayOfMonth)
        }

        // 사진선택 클릭 시
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
            val str_diStartDate : String = startDate.text.toString()
            val str_diEndDate : String = endDate.text.toString()
            var str_emotion : String

            // 만족도 (라디오 버튼)
            str_emotion = when (rdoGrpEmotion.checkedRadioButtonId) {
                R.id.rdoReallyBad -> "ReallyBad"
                R.id.rdoBad -> "Bad"
                R.id.rdoSoso -> "Soso"
                R.id.rdoGood -> "Good"
                R.id.rdoReallyGood -> "ReallyGood"
                else -> ""
            }


            // 이미지!!!!!!!!!!!!!
            val bitmap = (imgBtnPic.drawable as BitmapDrawable).bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)

            val str_diImg : ByteArray = stream.toByteArray()
            stream.close()


            // DB에 저장
            sqlitedb = dbManager.writableDatabase
////            sqlitedb.execSQL("INSERT INTO diaries VALUES ('"str_title"','$str_contents', '$str_startDate' , '$str_endDate')")
////            sqlitedb.close()
//            sqlitedb.execSQL("INSERT INTO diaries (diTitle, diContents, diStartDate, diEndDate, diImg) VALUES ('"
//                    +str_diTitle+"','"+str_diContents+"', "+str_diStartDate+" , '"+str_diEndDate+"', '"+str_diImg+"')")
//            sqlitedb.close()
            val sql = "INSERT INTO diaries (memTitleForDi, diTitle, diContents, diStartDate, diEndDate, diEmotion, diImg) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?);"

            val values = arrayOf(memTitleForDi, str_diTitle, str_diContents, str_diStartDate, str_diEndDate,str_emotion,str_diImg)    // 마지막에 str_diImg 추가
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
            // 클릭되면 MemoryInfo로 이동
            val intent: Intent = Intent(this, MemoryInfo::class.java)
            intent.putExtra("intent_memTitle", memTitleForDi)
            startActivity(intent)
        }
    } // onCreate


//    // 결과 가져오기 - 이미지
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

     // 이미지!!!!!!!
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
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

        //시작 날짜 오늘로 설정
        Calendar.setSelectedDate(CalendarDay.today())

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