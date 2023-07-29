package com.example.trabapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.example.trabapp.databinding.ActivityMyMemoryBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text
@SuppressLint("Range")
class MemoryInfo : AppCompatActivity() {
    lateinit var backButton: ImageButton // 뒤로 가기 버튼

    lateinit var dbManager: DBManager
    //lateinit var sqlitedb:SQLiteDatabase

    lateinit var memLayout: LinearLayout

    lateinit var btnConfirm : Button
    lateinit var edtTextTitle : EditText
    lateinit var edtTextMember : EditText
    lateinit var btnCalenderStart : AppCompatButton
    lateinit var btnCalenderEnd: AppCompatButton
    lateinit var rdoGrpColor : RadioGroup
    lateinit var rdoRed : RadioButton
    lateinit var rdoOrange : RadioButton
    lateinit var rdoGreen : RadioButton
    lateinit var rdoMint : RadioButton
    lateinit var rdoBlue : RadioButton
    lateinit var rdoPurple : RadioButton
    lateinit var floatBtn : FloatingActionButton

    // db에 보낼 변수들 정의 - 추억 상세
    private lateinit var str_memFirstTitle : String // 초기 추억 제목
    lateinit var str_memTitle : String // 추억 제목
    lateinit var str_memMb : String  // 멤버
    lateinit var str_memStartDate : String // 시작일
    lateinit var str_memEndDate : String // 마감일
    lateinit var str_memColor : String // 기록 색



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_info)

        // ---id 연결---
        btnConfirm = findViewById(R.id.btnConfirm)
        edtTextTitle = findViewById(R.id.edtTextTitle)
        edtTextMember = findViewById(R.id.edtTextMember)
        btnCalenderStart = findViewById(R.id.btnCalenderStart)
        btnCalenderEnd = findViewById(R.id.btnCalenderEnd)
        rdoGrpColor = findViewById(R.id.rdoGrpColor)
        rdoRed = findViewById(R.id.rdoRed)
        rdoOrange = findViewById(R.id.rdoOrange)
        rdoGreen = findViewById(R.id.rdoGreen)
        rdoMint = findViewById(R.id.rdoMint)
        rdoBlue = findViewById(R.id.rdoBlue)
        rdoPurple = findViewById(R.id.rdoPurple)

        val intent = intent
        str_memTitle = intent.getStringExtra("intent_memTitle").toString()
        //str_memFirstTitle = intent.getStringExtra("intent_memFirstTitle").toString()

        dbManager=DBManager(this)
        loadMemories() // 추억 작성한 내용 추억 상세 페이지에 반영되도록

        // 플로팅 버튼 (추억 추가)
        floatBtn = findViewById(R.id.floatBtn)
        floatBtn.setOnClickListener{
            val intent: Intent = Intent(this, MemRecored::class.java)
            startActivity(intent)
        }

        // 확인 버튼 - 변경사항 데이터베이스에 수정되어 업데이트 됨
        // 어떤 제목에 있는지 파악 후 진행 되어야 그 행만 수정이 됨
        btnConfirm.setOnClickListener(){
//            var sqlitedb : SQLiteDatabase = dbManager.writableDatabase
//            var cursorMem : Cursor // 추억 관련 - 제목으로 해당 추억 찾아가야 함
//            cursorMem = sqlitedb.rawQuery("SELECT * FROM memories WHERE memFirstTitle = '" + str_memFirstTitle +"';", null)
//            while (cursorMem.moveToNext()){
//                // db에 넣을 변수 선언
//                str_memTitle = edtTextTitle.text.toString()   // 제목
//                str_memMb = edtTextMember.text.toString()   // 멤버들
//                str_memColor  = ""                             // 컬러
//                str_memStartDate = "2023 - 07 - 06"
//                str_memEndDate = "2023 - 07 - 20"
//
//                // 라디오버튼
//                if (rdoGrpColor.checkedRadioButtonId == R.id.rdoRed){           // 빨강(핑크)
//                    str_memColor = "pink"
//                }
//                else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoOrange){   // 주황
//                    str_memColor = "orange"
//                }
//                else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoGreen){    // 초록
//                    str_memColor = "green"
//                }
//                else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoMint){    // 민트
//                    str_memColor = "mint"
//                }
//                else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoBlue){    // 파랑
//                    str_memColor = "blue"
//                }
//                else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoPurple){  // 보라
//                    str_memColor = "purple"
//                }
//            }
//
//            sqlitedb = dbManager.writableDatabase
//            //sqlitedb.execSQL("UPDATE INTO memories VALUES ('$str_memTitle','$str_memMb', '$str_memStartDate' , '$str_memEndDate', '$str_memColor')")
////            sqlitedb.execSQL("UPDATE memories SET gNumber = "+edtNumber.text+" WHERE gName = '"
////                    +edtName.text.toString()+"';")
//
//            // Use the update method to modify the existing record with the new values
//            val sql = "UPDATE memories SET memMb = ?, memColor = ?, memStartDate = ?, memEndDate = ? WHERE memTitle = ?;"
//            val values = arrayOf(str_memMb, str_memColor, str_memStartDate, str_memEndDate, str_memTitle)
//
//            sqlitedb.execSQL(sql, values)
//            //sqlitedb.close()
//
//            cursorMem.close()
//            sqlitedb.close()


//            // 나의 구원자 지피티.. 일단은 된다.. 근데 제목은 안 바뀐다..
//            // Get the new values from the input fields and radio button
            str_memTitle = edtTextTitle.text.toString()
            str_memMb = edtTextMember.text.toString()
            str_memStartDate = btnCalenderStart.text.toString()
            str_memEndDate = btnCalenderEnd.text.toString()
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

            loadMemories()
            Toast.makeText(this, "변경되었습니다.", Toast.LENGTH_SHORT).show()
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
        //dbManager = DBManager(this, "memories", null, 1)
        var sqlitedb: SQLiteDatabase = dbManager.readableDatabase

        //val cursor = dbManager.getAllMemories()
        var cursorMem : Cursor // 추억 관련 - 제목으로 해당 추억 찾아가야 함
        cursorMem = sqlitedb.rawQuery("SELECT * FROM memories WHERE memTitle = '" + str_memTitle +"';", null)
        var cursorDiy : Cursor // 일지 관련
        cursorDiy = sqlitedb.rawQuery("SELECT * FROM diaries", null)

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
            btnCalenderStart.setText(str_memStartDate)
            btnCalenderEnd.setText(str_memEndDate)
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
            val str_diaContents = cursorDiy.getString(cursorDiy.getColumnIndex("diContents")).toString()
            val str_diaStartDate = cursorDiy.getString(cursorDiy.getColumnIndex("diStartDate")).toString()
            val str_diaEndDate = cursorDiy.getString(cursorDiy.getColumnIndex("diEndDate")).toString()
            val str_diImg = cursorDiy.getBlob(cursorDiy.getColumnIndex("diImg"))

            // ---- 작성한 일지 토대로 일지 목록 만듦 ----
            // 아이템 레이아웃 불러오기
            val diaryItemView = layoutInflater.inflate(R.layout.diary_item_layout, null)

            // 아이템 레이아웃 id 연결
            val testTitle = diaryItemView.findViewById<TextView>(R.id.testTitle)
            val textContents = diaryItemView.findViewById<TextView>(R.id.textContents)
            val textDate = diaryItemView.findViewById<TextView>(R.id.textDate)
            var imgPic = diaryItemView.findViewById<ImageView>(R.id.imgPic)

            diaryItemView.id = num

            testTitle.text = str_diaTitle
            textContents.text = str_diaContents
            textDate.text = "$str_diaStartDate ~ $str_diaEndDate"
//          imgPic = str_diImg

            diaryItemView.setOnClickListener {
                val intent = Intent(this, DiaryInfo::class.java)
                intent.putExtra("intent_name", str_diaTitle)
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

    // 변경사항 저장 후에 목록 업데이트 될 수 있도록 추가
//    override fun onResume() {
//        super.onResume()
//        // Call loadMemories() to refresh the data whenever the activity resumes
//        loadMemories()
//    }

//    // 버튼에서도, loadMemories()에서도 sqlitedb 쓰기 때문에 액티비티 종료할 때 close하도록 함
//    override fun onDestroy() {
//        super.onDestroy()
//        //sqlitedb.close()
//        loadMemories()
//    }
}