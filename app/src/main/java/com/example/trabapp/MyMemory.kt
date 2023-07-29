package com.example.trabapp

import android.annotation.SuppressLint
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_my_memory) // view binding할 때 이거 필요 없음.
        // 있으면 뒤로 가기 버튼 눌렀을 때 에러 나면서 버튼이랑 상호작용이 안됨

        // 뷰 바인딩 - 이게 가장 위에 있어야함
        binding = ActivityMyMemoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager = DBManager(this, "memories", null, 1)
        //dbManager=DBManager(this)
        scrollView2 = findViewById(R.id.scrollView2)
        //layout = findViewById(R.id.groupLayout)
        // 데이터베이스 조회 후 목록에 추가
        //loadMemories()

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

            ////dbManager = DBManager(this, "memories", null, 1)
            //dbManager = DBManager(this)

            // 확인 버튼
            val okButton = mDialogView.findViewById<Button>(R.id.addGroupAcceptBtn)
            okButton.setOnClickListener{
                // 확인 버튼 누르면
                // db에 전달 및 저장

                // 변수 선언
                //var str_members : String = btnConfirm.text.toString()   // 멤버들
                //var str_color : String = ""                             // 컬러

                var str_memTitle : String = newGroupName.text.toString() // 추억 제목
                var str_memMb : String = edtTextMember.text.toString() // 멤버
                var str_startDate : String = startDate.text.toString() // 시작일
                var str_endDate : String = endDate.text.toString() // 마감일
                var str_memColor : String = "" // 기록 색

                // 라디오버튼
                if (rdoGrpColor.checkedRadioButtonId == R.id.rdoRed){           // 빨강
                    str_memColor = "red"
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
                sqlitedb.close()
                dbManager.close()

//
//                // 내 추억,추억 상세로 데이터 전달
//                val intentMyMemory = Intent(this, MyMemory::class.java)
//                //val intentMemoryInfo = Intent(this, MemoryInfo::class.java)
//                //intentMyMemory.putExtra("intent_title", str_memTitle)
//                //intentMemoryInfo.putExtra("intent_title", str_memTitle)
//                startActivity(intentMyMemory) // 이 과정까지 해야 데이터가 전달됨
////                startActivity(intentMemoryInfo)

                //
                Toast.makeText(this, "토스트 메시지", Toast.LENGTH_SHORT).show()
                mAlertDialog.dismiss() // 원래 화면으로 돌아감
            }
            // 취소 버튼
            val noButton = mDialogView.findViewById<Button>(R.id.btnCancel)
            noButton.setOnClickListener {
                mAlertDialog.dismiss() // 원래 화면으로 돌아감
            }
        }
    }

//    // 데이터베이스 조회 후 목록에 추가하는 함수
//    private fun loadMemories(){
//        //dbManager = DBManager(this, "memories", null, 1)
//        sqlitedb = dbManager.readableDatabase
//
//        val cursor = dbManager.getAllMemories()
//
//        // 근데 이렇게 쓰면 value가 0 이상? 이어야된다고 오류 뜸
//        // 근데 이 오류가 column name specified is not found in the cursor일 경우에 뜨는 오륜데;;
////        val cursor : Cursor
////        cursor = sqlitedb.rawQuery("SELECT * FROM memories", null)
//
//        var num : Int = 0
//        while (cursor.moveToNext()) { // 오류나서 getColumnIndex->getColumnIndexOrThrow로 바꿈. 스택오버플로가 그러랬음. >>:(
//            val str_memTitle = cursor.getString(cursor.getColumnIndex("memTitle")).toString()
//            val str_startDate = cursor.getString(cursor.getColumnIndex("memStartDate")).toString()
//            val str_endDate = cursor.getString(cursor.getColumnIndex("memEndDate")).toString()
//            val str_memColor = cursor.getString(cursor.getColumnIndex("memColor")).toString()
//
////            // 이제 이 정보를 목록에 추가하는 작업을 진행합니다.
////            // 여기서는 TextView를 생성하고 목록에 추가하는 방식으로 예시를 드리겠습니다.
////            val textView = TextView(this)
////            textView.text = "추억 제목: $str_memTitle, 시작일: $str_startDate, 마감일: $str_endDate"
////            // 이후 textView를 원하는 목록 뷰(LinearLayout, ListView 등)에 추가하면 됩니다.
//
//            // Inflate memory_item_layout.xml for each memory item
//            //val memoryItemView = layoutInflater.inflate(R.layout.activity_my_memory, null)
//            val memoryItemView = layoutInflater.inflate(R.layout.memory_item_layout, null)
////
//            // Find views in the memory_item_layout
//            val groupColor = memoryItemView.findViewById<ImageView>(R.id.groupColor)
//            val groupName = memoryItemView.findViewById<TextView>(R.id.groupName)
//            val groupDate = memoryItemView.findViewById<TextView>(R.id.groupDate)
//
//            memoryItemView.id = num
//
//            // Set data from the database to the views
//            groupName.text = str_memTitle
//            groupDate.text = "$str_startDate ~ $str_endDate"
//
//            // Set the color of ImageView based on the value from the database
//            when (str_memColor) {
//                "red" -> groupColor.setImageResource(R.drawable.circle_red)
//                "orange" -> groupColor.setImageResource(R.drawable.circle_orange)
//                "green" -> groupColor.setImageResource(R.drawable.circle_green)
//                "mint" -> groupColor.setImageResource(R.drawable.circle_mint)
//                "blue" -> groupColor.setImageResource(R.drawable.circle_blue)
//                "purple" -> groupColor.setImageResource(R.drawable.circle_purple)
//                // Add more cases for other colors if needed
//                //else -> groupColor.setImageResource(R.drawable.default_circle)
//            }
//
//            // Add the memory item view to your LinearLayout or ListView
//            // (Assuming you have a LinearLayout named 'memoryListLayout')
//            scrollView2.addView(memoryItemView)
//            num++;
//        }
//
//        cursor.close()
//        sqlitedb.close()
//        dbManager.close()
//    }
}
