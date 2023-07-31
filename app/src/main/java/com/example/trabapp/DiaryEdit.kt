package com.example.trabapp

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast

@SuppressLint("Range")
class DiaryEdit : AppCompatActivity() {
    lateinit var backButton: ImageButton // 뒤로 가기 버튼

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    // id 정의
    lateinit var diEditBtn: ImageButton
    //lateinit var diDelBtn: ImageButton
    lateinit var diDate: TextView
    lateinit var imgPic: ImageView
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
        setContentView(R.layout.activity_diary_edit)

        dbManager = DBManager(this)
        sqlitedb = dbManager.writableDatabase

        val intent = intent
        str_diTitle = intent.getStringExtra("intent_diTitle").toString()

        // ---id 연결---
        diEditBtn = findViewById<ImageButton>(R.id.imgBtnEdit)
        //diDelBtn = findViewById<ImageButton>(R.id.imgBtnDel)
        diDate = findViewById<TextView>(R.id.textDate)
        imgPic = findViewById<ImageView>(R.id.imgPic)
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

        // 체크 버튼 - 편집한 내용 업데이트
        diEditBtn.setOnClickListener{
            str_diContents = diCont.text.toString()
            //str_diStartDate = btnCalenderStart.text.toString()
            //str_diEndDate = btnCalenderEnd.text.toString()
            str_diEmotion = when (rdoGrpEmotion.checkedRadioButtonId) {
                R.id.rdoReallyBad -> "ReallyBad"
                R.id.rdoBad -> "Bad"
                R.id.rdoSoso -> "Soso"
                R.id.rdoGood -> "Good"
                R.id.rdoReallyGood -> "ReallyGood"
                else->""
            }

            // Use the update method to modify the existing record with the new values
            val values = ContentValues().apply {
                put("diContents", str_diContents)
                //put("diStartDate", str_diStartDate)
                //put("diEndDate", str_diEndDate)
                put("diEmotion", str_diEmotion)
                //put("diImg", str_diImg) // 여기 사진 관련 추가
            }

            sqlitedb.update("diaries", values, "diTitle = ?", arrayOf(str_diTitle))

            sqlitedb.close()

            //loadMemories() // 이렇게 하면 추억 목록이 2배가 되는 문제 발생
            Toast.makeText(this, "변경되었습니다.", Toast.LENGTH_SHORT).show()


            val intent: Intent = Intent(this, MemoryInfo::class.java)
            intent.putExtra("intent_memTitle", str_memTitleForDi)
            intent.putExtra("intent_diTitle", str_diTitle)
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
            //val str_diImg = cursor.getString(cursor.getColumnIndex("diImg")).toString()
            val str_diEmotion = cursor.getString(cursor.getColumnIndex("diEmotion")).toString()

            // 저장된 값 대입
            diDate.text = "$str_diStartDate ~ $str_diEndDate"
            //imgPic =
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
}