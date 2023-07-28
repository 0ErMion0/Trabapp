package com.example.trabapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class DiaryInfo : AppCompatActivity() {
    lateinit var backButton: ImageButton // 뒤로 가기 버튼

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary_info)

        // 뒤로 가기 버튼
        backButton = findViewById<ImageButton>(R.id.imgBtnBack)
        backButton.setOnClickListener{
            // 클릭되면 추억 상세 페이지로 이동 (구현해야됨)
            val intent: Intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }
}