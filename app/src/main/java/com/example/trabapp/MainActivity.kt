package com.example.trabapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.test.MySharePreferences


class MainActivity : AppCompatActivity(){

    lateinit var btnReg : Button
    lateinit var btnLog : Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnReg = findViewById(R.id.btnCreateAccount)
        btnLog = findViewById(R.id.btnLogin)

        //데이터가 저장되어 있으면
        if(!MySharePreferences.getUserId(this).isNullOrBlank()){

            //토스트 메시지(자동 로그인)
            Toast.makeText(this@MainActivity,
                "자동 로그인되었습니다.", Toast.LENGTH_SHORT).show()

            //화면 전환 - 맵으로
            val intent = Intent(applicationContext, MapActivity::class.java)
            startActivity(intent)

            finish()
        }

        btnReg.setOnClickListener {
            //화면 전환
            val intent = Intent(applicationContext, CreateAccount::class.java)
            startActivity(intent)
        }

        btnLog.setOnClickListener {
            //화면 전환
            val intent = Intent(applicationContext, LoginAccount::class.java)
            startActivity(intent)
        }
    }
}