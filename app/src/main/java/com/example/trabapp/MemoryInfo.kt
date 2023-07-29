package com.example.trabapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup

class MemoryInfo : AppCompatActivity() {
    lateinit var backButton: ImageButton // 뒤로 가기 버튼

    lateinit var btnConfirm : Button
    lateinit var edtTextMember : EditText
    lateinit var rdoGrpColor : RadioGroup
    lateinit var rdoRed : RadioButton
    lateinit var rdoOrange : RadioButton
    lateinit var rdoGreen : RadioButton
    lateinit var rdoMint : RadioButton
    lateinit var rdoBlue : RadioButton
    lateinit var rdoPurple : RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_info)

        // ---id 연결---
        btnConfirm = findViewById(R.id.btnConfirm)
        edtTextMember = findViewById(R.id.edtTextMember)
        rdoGrpColor = findViewById(R.id.rdoGrpColor)
        rdoRed = findViewById(R.id.rdoRed)
        rdoOrange = findViewById(R.id.rdoOrange)
        rdoGreen = findViewById(R.id.rdoGreen)
        rdoMint = findViewById(R.id.rdoMint)
        rdoBlue = findViewById(R.id.rdoBlue)
        rdoPurple = findViewById(R.id.rdoPurple)


        btnConfirm.setOnClickListener(){
            // db에 넣을 변수 선언
            var str_members : String = btnConfirm.text.toString()   // 멤버들
            var str_color : String = ""                             // 컬러

            // 라디오버튼
            if (rdoGrpColor.checkedRadioButtonId == R.id.rdoRed){           // 빨강
                str_color = "red"
            }
            else if (rdoGrpColor.checkedRadioButtonId == R.id.rdoOrange){   // 주황
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
        }





        // ---뒤로 가기 버튼---
        backButton = findViewById<ImageButton>(R.id.imgBtnBack)
        backButton.setOnClickListener{
            // 클릭되면 MapActivity로 이동
            val intent: Intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }



    }
}