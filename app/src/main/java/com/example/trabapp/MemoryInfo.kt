package com.example.trabapp

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener

@SuppressLint("Range")
class MemoryInfo : AppCompatActivity() {
    lateinit var backButton: ImageButton // 뒤로 가기 버튼

    lateinit var dbManager: DBManager
    //lateinit var sqlitedb:SQLiteDatabase

    lateinit var memLayout: LinearLayout

    lateinit var btnConfirm : Button
    lateinit var edtTextTitle : EditText
    lateinit var edtTextMember : EditText
    lateinit var btnCalenderStart : ImageButton
    lateinit var btnCalenderEnd: ImageButton
    lateinit var rdoGrpColor : RadioGroup
    lateinit var rdoRed : RadioButton
    lateinit var rdoOrange : RadioButton
    lateinit var rdoGreen : RadioButton
    lateinit var rdoMint : RadioButton
    lateinit var rdoBlue : RadioButton
    lateinit var rdoPurple : RadioButton
    lateinit var floatBtn : FloatingActionButton
    lateinit var startDate : TextView
    lateinit var endDate : TextView



    // db에 보낼 변수들 정의 - 추억 상세
    //private lateinit var str_memFirstTitle : String // 초기 추억 제목
    lateinit var str_memTitle : String // 추억 제목
    lateinit var str_memMb : String  // 멤버
    lateinit var str_memStartDate : String // 시작일
    lateinit var str_memEndDate : String // 마감일
    lateinit var str_memColor : String // 기록 색

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_info)

        // ---id 연결---
        memLayout = findViewById(R.id.memLayout)
        btnConfirm = findViewById(R.id.btnConfirm)
        edtTextTitle = findViewById(R.id.edtTextTitle)
        edtTextMember = findViewById(R.id.edtTextMember)
        btnCalenderStart = findViewById(R.id.startDateIconBtn)
        btnCalenderEnd = findViewById(R.id.endDateIconBtn)
        rdoGrpColor = findViewById(R.id.rdoGrpColor)
        rdoRed = findViewById(R.id.rdoRed)
        rdoOrange = findViewById(R.id.rdoOrange)
        rdoGreen = findViewById(R.id.rdoGreen)
        rdoMint = findViewById(R.id.rdoMint)
        rdoBlue = findViewById(R.id.rdoBlue)
        rdoPurple = findViewById(R.id.rdoPurple)
        startDate = findViewById(R.id.startDate)
        endDate = findViewById(R.id.endDate)

        val intent = intent
        str_memTitle = intent.getStringExtra("intent_memTitle").toString()
        //str_memFirstTitle = intent.getStringExtra("intent_memFirstTitle").toString()
        //str_diaTitle = intent.getStringExtra("intent_diTitle").toString()


        dbManager=DBManager(this)
        loadMemories() // 추억 작성한 내용 추억 상세 페이지에 반영되도록

        // 플로팅 버튼 (추억 추가)
        floatBtn = findViewById(R.id.floatBtn)
        floatBtn.setOnClickListener{
            val intent: Intent = Intent(this, MemRecored::class.java)
            intent.putExtra("intent_memTitleForDi", str_memTitle) //
            startActivity(intent)
        }

        // 확인 버튼 - 변경사항 데이터베이스에 수정되어 업데이트 됨
        // 어떤 제목에 있는지 파악 후 진행 되어야 그 행만 수정이 됨
        btnConfirm.setOnClickListener(){
            // Get the new values from the input fields and radio button
            str_memTitle = edtTextTitle.text.toString()
            str_memMb = edtTextMember.text.toString()
            str_memStartDate = startDate.text.toString()
            str_memEndDate = endDate.text.toString()
            str_memColor = when (rdoGrpColor.checkedRadioButtonId) {
                R.id.rdoRed -> "pink"
                R.id.rdoOrange -> "orange"
                R.id.rdoGreen -> "green"
                R.id.rdoMint -> "mint"
                R.id.rdoBlue -> "blue"
                R.id.rdoPurple -> "purple"
                else -> ""
            }

            val sqlitedb = dbManager.writableDatabase

            // Use the update method to modify the existing record with the new values
            val values = ContentValues().apply {
                put("memTitle", str_memTitle)
                put("memMb", str_memMb)
                put("memStartDate", str_memStartDate)
                put("memEndDate", str_memEndDate)
                put("memColor", str_memColor)
            }

            sqlitedb.update("memories", values, "memTitle = ?", arrayOf(str_memTitle))

            sqlitedb.close()

            //loadMemories() // 이렇게 하면 추억 목록이 2배가 되는 문제 발생
            Toast.makeText(this, "변경되었습니다", Toast.LENGTH_SHORT).show()

            // 그냥 해당 액티비티 다시 로드해볼까? 근데 여기 로드 위해 필요한 내용 보내줘야 함
            val intent: Intent = Intent(this, MemoryInfo::class.java)
            intent.putExtra("intent_memTitle", str_memTitle)
            startActivity(intent)
        }

        btnCalenderStart.setOnClickListener{

            ////날짜 슬라이싱
            //.을 기준으로 자름
            var str_arr = startDate.text.toString().split(".")

            //연월일 구분
            var year = str_arr.get(0)
            var month = str_arr.get(1)
            var dayOfMonth = str_arr.get(2)

            //공백 제거
            month = month.substring(1,month.length)
            dayOfMonth = dayOfMonth.substring(1,dayOfMonth.length)

            Log.d("연", year)
            Log.d("월", month)
            Log.d("일", dayOfMonth)

            datePopup(startDate, year.toInt(), month.toInt(), dayOfMonth.toInt())
        }
        btnCalenderEnd.setOnClickListener{

            ////날짜 슬라이싱
            //.을 기준으로 자름
            var str_arr = endDate.text.toString().split(".")

            //연월일 구분
            var year = str_arr.get(0)
            var month = str_arr.get(1)
            var dayOfMonth = str_arr.get(2)

            //공백 제거
            month = month.substring(1,month.length)
            dayOfMonth = dayOfMonth.substring(1,dayOfMonth.length)

            Log.d("연", year)
            Log.d("월", month)
            Log.d("일", dayOfMonth)

            datePopup(endDate, year.toInt(), month.toInt(), dayOfMonth.toInt())
        }

        // ---뒤로 가기 버튼---
        backButton = findViewById<ImageButton>(R.id.imgBtnBack)
        backButton.setOnClickListener{
            // 클릭되면 MapActivity로 이동
            val intent: Intent = Intent(this, MyMemory::class.java)
            startActivity(intent)
        }
    }



    // 데이터베이스 조회 후 추억 상세와 목록에 추가하는 함수
    private fun loadMemories(){
//        diaryList.clear()

        //dbManager = DBManager(this, "memories", null, 1)
        var sqlitedb: SQLiteDatabase = dbManager.readableDatabase

        //val cursor = dbManager.getAllMemories()
        var cursorMem : Cursor // 추억 관련 - 제목으로 해당 추억 찾아가야 함
        cursorMem = sqlitedb.rawQuery("SELECT * FROM memories WHERE memTitle = '" + str_memTitle +"';", null)
        var cursorDiy : Cursor // 일지 관련
        cursorDiy = sqlitedb.rawQuery("SELECT * FROM diaries WHERE memTitleForDi = '" + str_memTitle +"';", null)

        var num : Int = 0

        // 추억 상세 관련
        while (cursorMem.moveToNext()){
            // 데이터베이스에 저장된 값 가져옴
            // 추억 상세
            str_memTitle = cursorMem.getString(cursorMem.getColumnIndex("memTitle")).toString()
            str_memMb = cursorMem.getString(cursorMem.getColumnIndex("memMb")).toString()
            str_memStartDate = cursorMem.getString(cursorMem.getColumnIndex("memStartDate")).toString()
            str_memEndDate = cursorMem.getString(cursorMem.getColumnIndex("memEndDate")).toString()
            str_memColor = cursorMem.getString(cursorMem.getColumnIndex("memColor")).toString()

            // ---- 추억 상세 ----
            edtTextTitle.setText(str_memTitle)
            edtTextMember.setText(str_memMb)
            startDate.setText(str_memStartDate)
            endDate.setText(str_memEndDate)
            when (str_memColor) {
                "pink" -> rdoGrpColor.check(R.id.rdoRed)
                "orange" -> rdoGrpColor.check(R.id.rdoOrange)
                "green" -> rdoGrpColor.check(R.id.rdoGreen)
                "mint" -> rdoGrpColor.check(R.id.rdoMint)
                "blue" -> rdoGrpColor.check(R.id.rdoBlue)
                "purple" -> rdoGrpColor.check(R.id.rdoPurple)
                // Add more cases for other colors if needed
                //else -> groupColor.rdoGrpColor.check(R.id.rdoPurple)
            }
        }

        // 일지 목록 관련
        while (cursorDiy.moveToNext()){
            // 데이터베이스에 저장된 값 가져옴
            // 일지 목록
            val str_diaTitle = cursorDiy.getString(cursorDiy.getColumnIndex("diTitle")).toString()
            //str_diaTitle = cursorDiy.getString(cursorDiy.getColumnIndex("diTitle")).toString()
            val str_diaContents = cursorDiy.getString(cursorDiy.getColumnIndex("diContents")).toString()
            val str_diaStartDate = cursorDiy.getString(cursorDiy.getColumnIndex("diStartDate")).toString()
            val str_diaEndDate = cursorDiy.getString(cursorDiy.getColumnIndex("diEndDate")).toString()
            val str_emotion = cursorDiy.getString(cursorDiy.getColumnIndex("diEmotion"))
            val str_diImg = cursorDiy.getBlob(cursorDiy.getColumnIndex("diImg"))



            // ---- 작성한 일지 토대로 일지 목록 만듦 ----
            // 아이템 레이아웃 불러오기
            val diaryItemView = layoutInflater.inflate(R.layout.diary_item_layout, null)

            // 아이템 레이아웃 id 연결
            val testTitle = diaryItemView.findViewById<TextView>(R.id.testTitle)
            val textContents = diaryItemView.findViewById<TextView>(R.id.textContents)
            val textStartDate = diaryItemView.findViewById<TextView>(R.id.textStartDate)
            val textEndDate = diaryItemView.findViewById<TextView>(R.id.textEndDate)
            var imotion = diaryItemView.findViewById<ImageView>(R.id.imgFace)
            var imgPic = diaryItemView.findViewById<ImageView>(R.id.imgPic)


            // 라디오 관련
            val mem_recored = layoutInflater.inflate(R.layout.activity_mem_recored, null)
            val rdoGrpEmotion = mem_recored.findViewById<RadioGroup>(R.id.rdoGrpEmotion)
            val rdoReallyBad = mem_recored.findViewById<RadioButton>(R.id.rdoReallyBad)
            val rdoBad = mem_recored.findViewById<RadioButton>(R.id.rdoBad)
            val rdoSoso = mem_recored.findViewById<RadioButton>(R.id.rdoSoso)
            val rdoGood = mem_recored.findViewById<RadioButton>(R.id.rdoGood)
            val rdoReallyGood = mem_recored.findViewById<RadioButton>(R.id.rdoReallyGood)

            diaryItemView.id = num

            // 정보 넣기
            testTitle.setText(str_diaTitle)
            textContents.setText(str_diaContents)
            textStartDate.text = "$str_diaStartDate"
            textEndDate.text = "$str_diaEndDate"

            // 라디오
            when (str_emotion){
                "ReallyBad" -> imotion.setImageResource(R.drawable.face_really_bad)
                "Bad" -> imotion.setImageResource(R.drawable.face_bad)
                "Soso" -> imotion.setImageResource(R.drawable.face_soso)
                "Good" -> imotion.setImageResource(R.drawable.face_good)
                "ReallyGood" -> imotion.setImageResource(R.drawable.face_really_good)
            }


            // 이미지
            val bm = byteArrayToBitmap(str_diImg)
            imgPic.setImageBitmap(bm)

            // 일지 클릭 시 일지 상세로
            diaryItemView.setOnClickListener {
                val intent = Intent(this, DiaryInfo::class.java)
                intent.putExtra("intent_diTitle", str_diaTitle)
                startActivity(intent)
            }

            memLayout.addView(diaryItemView)
            num++;
        }

        cursorDiy.close()
        cursorMem.close()
        sqlitedb.close()
        //dbManager.close()
    }

    // 이미지!!!!!
    fun ByteArray.toBitmap(): Bitmap {
        return BitmapFactory.decodeByteArray(this, 0, this.size)
    }

    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
        val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
        return bitmap
    }

    //일정 팝업
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