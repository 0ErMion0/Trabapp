package com.example.trabapp

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.trabapp.databinding.ActivityMyMemoryBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

@SuppressLint("Range")
class MyMemory : AppCompatActivity() {
    lateinit var dbManager: DBManager
    lateinit var sqlitedb:SQLiteDatabase

    lateinit var backButton: ImageButton // 뒤로 가기 버튼
    lateinit var floatBtn: FloatingActionButton// 추억 추가 버튼
    private lateinit var binding: ActivityMyMemoryBinding // 레이아웃과 MyMemory 연결
    lateinit var scrollView2: ScrollView
    lateinit var layout: LinearLayout

//    // 수정
//    companion object {
//        const val REQUEST_CODE_MEMORY_INFO = 1
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_my_memory) // view binding할 때 이거 필요 없음.
        // 있으면 뒤로 가기 버튼 눌렀을 때 에러 나면서 버튼이랑 상호작용이 안됨

        // 뷰 바인딩 - 이게 가장 위에 있어야함
        binding = ActivityMyMemoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //dbManager = DBManager(this, "memories", null, 1)
        dbManager=DBManager(this)
        scrollView2 = findViewById(R.id.scrollView2)
        layout = findViewById(R.id.groupLayout)
        // 데이터베이스 조회 후 목록에 추가
        loadMemories()

        // 뒤로 가기 버튼
        backButton = findViewById<ImageButton>(R.id.imgBtnBack)
        backButton.setOnClickListener{
            // 클릭되면 MapActivity로 이동
            val intent: Intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        // 추억 추가 버튼
        floatBtn = findViewById<FloatingActionButton>(R.id.floatBtn)
        binding.floatBtn.setOnClickListener{
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
            okButton.setOnClickListener{
                // 확인 버튼 누르면
                // <<해야됨>> 모든 빈칸이 다 채워졌을 경우
                // db에 전달 및 저장

                // db에 보낼 변수들 정의
                var str_memTitle : String = newGroupName.text.toString() // 추억 제목
                var str_memMb : String = edtTextMember.text.toString() // 멤버
                var str_startDate : String = startDate.text.toString() // 시작일
                var str_endDate : String = endDate.text.toString() // 마감일
                var str_memColor : String = "" // 기록 색
                var str_memTitleForDi : String = newGroupName.text.toString() // 일지에서 쓸 추억 제목

                // 라디오버튼
                if (rdoGrpColor.checkedRadioButtonId == R.id.rdoRed){           // 빨강(핑크)
                    str_memColor = "pink"
                }
                else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoOrange){   // 주황
                    str_memColor = "orange"
                }
                else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoGreen){    // 초록
                    str_memColor = "green"
                }
                else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoMint){    // 민트
                    str_memColor = "mint"
                }
                else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoBlue){    // 파랑
                    str_memColor = "blue"
                }
                else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoPurple){  // 보라
                    str_memColor = "purple"
                }

                sqlitedb = dbManager.writableDatabase
                sqlitedb.execSQL("INSERT INTO memories VALUES ('"+str_memTitle+"','"+str_memMb+"', "+str_startDate+" , '"+str_endDate+"', '"+str_memColor+"')")
                sqlitedb.execSQL("INSERT INTO diaries (memTitleForDi) VALUES ('str_memTitle')")
                sqlitedb.close()
                //dbManager.close()



//                // 내 추억,추억 상세로 데이터 전달
                val intentMyMemory = Intent(this, MyMemory::class.java)
//                //val intentMemoryInfo = Intent(this, MemoryInfo::class.java)
                intentMyMemory.putExtra("intent_title", str_memTitle)
//                //intentMemoryInfo.putExtra("intent_title", str_memTitle)

                Toast.makeText(this, "내 추억을 작성했습니다.", Toast.LENGTH_SHORT).show()

                startActivity(intentMyMemory) // 이 과정까지 해야 데이터가 전달됨(안되는데욬ㅋㅋㅋㅋ
////                startActivity(intentMemoryInfo)

                //
                //Toast.makeText(this, "내 추억을 작성했습니다.", Toast.LENGTH_SHORT).show()
                //mAlertDialog.dismiss() // 원래 화면으로 돌아감
            }
            // 취소 버튼
            val noButton = mDialogView.findViewById<Button>(R.id.btnCancel)
            noButton.setOnClickListener {
                mAlertDialog.dismiss() // 원래 화면으로 돌아감
            }
        }
    }

    // 데이터베이스 조회 후 목록에 추가하는 함수
    private fun loadMemories(){
        //dbManager = DBManager(this, "memories", null, 1)
        sqlitedb = dbManager.readableDatabase

        val cursor = dbManager.getAllMemories()

        var num : Int = 0
        while (cursor.moveToNext()) {
            // 데이터베이스에 저장된 값 가져옴
            val str_memTitle = cursor.getString(cursor.getColumnIndex("memTitle")).toString()
            val str_startDate = cursor.getString(cursor.getColumnIndex("memStartDate")).toString()
            val str_endDate = cursor.getString(cursor.getColumnIndex("memEndDate")).toString()
            val str_memColor = cursor.getString(cursor.getColumnIndex("memColor")).toString()

            // Inflate memory_item_layout.xml for each memory item
            val memoryItemView = layoutInflater.inflate(R.layout.memory_item_layout, null)

            // Find views in the memory_item_layout
            //val groupButton = memoryItemView.findViewById<CardView>(R.id.groupButton)
            val groupColor = memoryItemView.findViewById<ImageView>(R.id.groupColor)
            val groupName = memoryItemView.findViewById<TextView>(R.id.groupName)
            val groupDate = memoryItemView.findViewById<TextView>(R.id.groupDate)

            memoryItemView.id = num // 목록 번호

            // Set data from the database to the views
            groupName.text = str_memTitle // 그룹 이름 설정
            groupDate.text = "$str_startDate ~ $str_endDate" // 날짜 설정
            //groupName.setText(str_memTitle) // 그룹 이름 설정
            //groupDate.setText("$str_startDate ~ $str_endDate") // 날짜 설정

            // Set the color of ImageView based on the value from the database
            when (str_memColor) {
                "pink" -> groupColor.setImageResource(R.drawable.circle_red)
                "orange" -> groupColor.setImageResource(R.drawable.circle_orange)
                "green" -> groupColor.setImageResource(R.drawable.circle_green)
                "mint" -> groupColor.setImageResource(R.drawable.circle_mint)
                "blue" -> groupColor.setImageResource(R.drawable.circle_blue)
                "purple" -> groupColor.setImageResource(R.drawable.circle_purple)
                // Add more cases for other colors if needed
                //else -> groupColor.setImageResource(R.drawable.default_circle)
            }

            // 레이아웃 클릭하면 추억 기록으로 이동 및 데이터 전달
            memoryItemView.setOnClickListener {
                val intent = Intent(this, MemoryInfo::class.java)
                intent.putExtra("intent_memTitle", str_memTitle)
                startActivity(intent)
            }

            // Add the memory item view to LinearLayout
            layout.addView(memoryItemView)
            num++;
        }

        cursor.close()
        sqlitedb.close()
        dbManager.close()
    }
}
