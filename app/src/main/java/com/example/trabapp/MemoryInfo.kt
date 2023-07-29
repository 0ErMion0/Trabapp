package com.example.trabapp

import android.annotation.SuppressLint
import android.content.Intent
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
import androidx.appcompat.app.AlertDialog
import com.example.trabapp.databinding.ActivityMyMemoryBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.w3c.dom.Text
@SuppressLint("Range")
class MemoryInfo : AppCompatActivity() {
    lateinit var backButton: ImageButton // 뒤로 가기 버튼

    lateinit var dbManager: DBManager
    lateinit var sqlitedb:SQLiteDatabase

    lateinit var memLayout: LinearLayout

    lateinit var btnConfirm : Button
    lateinit var edtTextMember : EditText
    lateinit var rdoGrpColor : RadioGroup
    lateinit var rdoRed : RadioButton
    lateinit var rdoOrange : RadioButton
    lateinit var rdoGreen : RadioButton
    lateinit var rdoMint : RadioButton
    lateinit var rdoBlue : RadioButton
    lateinit var rdoPurple : RadioButton
    lateinit var edtTextTitle : EditText
    lateinit var floatBtn : FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_info)

        dbManager=DBManager(this)

        // ---id 연결---
        edtTextTitle = findViewById(R.id.edtTextTitle)
        btnConfirm = findViewById(R.id.btnConfirm)
        edtTextMember = findViewById(R.id.edtTextMember)
        rdoGrpColor = findViewById(R.id.rdoGrpColor)
        rdoRed = findViewById(R.id.rdoRed)
        rdoOrange = findViewById(R.id.rdoOrange)
        rdoGreen = findViewById(R.id.rdoGreen)
        rdoMint = findViewById(R.id.rdoMint)
        rdoBlue = findViewById(R.id.rdoBlue)
        rdoPurple = findViewById(R.id.rdoPurple)

        // 플로팅 버튼 (추억 추가)
        floatBtn = findViewById(R.id.floatBtn)
        floatBtn.setOnClickListener{
            val intent: Intent = Intent(this, MemRecored::class.java)
            startActivity(intent)
        }


        // 확인 버튼
        btnConfirm.setOnClickListener(){
            // db에 넣을 변수 선언
            var str_title : String = edtTextTitle.text.toString()   // 제목
            var str_members : String = btnConfirm.text.toString()   // 멤버들
            var str_color : String = ""                             // 컬러
            var str_startDate : String = "2023 - 07 - 06"
            var str_endDate : String = "2023 - 07 - 20"

            // 라디오버튼
            if (rdoGrpColor.checkedRadioButtonId == R.id.rdoRed){           // 빨강(핑크)
                str_color = "pink"
            }
            else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoOrange){   // 주황
                str_color = "orange"
            }
            else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoGreen){    // 초록
                str_color = "green"
            }
            else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoMint){    // 민트
                str_color = "mint"
            }
            else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoBlue){    // 파랑
                str_color = "blue"
            }
            else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoPurple){  // 보라
                str_color = "purple"
            }

            sqlitedb = dbManager.writableDatabase
            sqlitedb.execSQL("UPDATE INTO memories VALUES ('$str_title','$str_members', '$str_startDate' , '$str_endDate', '$str_color')")
            sqlitedb.close()


        }





        // ---뒤로 가기 버튼---
        backButton = findViewById<ImageButton>(R.id.imgBtnBack)
        backButton.setOnClickListener{
            // 클릭되면 MapActivity로 이동
            val intent: Intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }




    }

    // 데이터베이스 조회 후 목록에 추가하는 함수
    private fun loadMemories(){
        //dbManager = DBManager(this, "memories", null, 1)
        sqlitedb = dbManager.readableDatabase

        val cursor = dbManager.getAllMemories()

        var num : Int = 0

        while (cursor.moveToNext()){
            val str_diaTitle = cursor.getString(cursor.getColumnIndex("diTitle")).toString()
            val str_diaContents = cursor.getString(cursor.getColumnIndex("diContents")).toString()
            val str_diaStartDate = cursor.getString(cursor.getColumnIndex("diStartDate")).toString()
            val str_diaEndDate = cursor.getString(cursor.getColumnIndex("diEndDate")).toString()
            val str_diImg = cursor.getBlob(cursor.getColumnIndex("diImg"))


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
        cursor.close()
        sqlitedb.close()
        dbManager.close()
    }
}