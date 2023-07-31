package com.example.trabapp

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import com.example.test.DBHelper
import com.example.test.MySharePreferences

@SuppressLint("Range")
class LoginAccount : AppCompatActivity() {

    lateinit var dbHelper: DBHelper
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var btnLogin : Button
    lateinit var btnCheck : CheckBox

    lateinit var edtId : EditText
    lateinit var edtPassword : EditText

    lateinit var str_id: String
    lateinit var str_password: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_account)

        dbHelper = DBHelper(this)

        btnLogin = findViewById(R.id.btnLogin)
        btnCheck = findViewById(R.id.checkBox)

        edtId = findViewById(R.id.edtId)
        edtPassword = findViewById(R.id.edtPass)

        //로그인 클릭
        btnLogin.setOnClickListener {
            sqlitedb = DBHelper(this).writableDatabase

            str_id = edtId.text.toString()
            str_password = edtPassword.text.toString()

            //하나라도 입력하지 않았을 때
            if(str_id == "" || str_password ==""){
                //토스트 메시지
                Toast.makeText(this@LoginAccount,
                    "회원정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()

            }else{  //회원 정보가 전부 입력되었을 경우

                //존재하는 아이디인지 체크, 존재하면 true
                val checkUserId = dbHelper.checkUserId(str_id)

                //이미 존재하지 않는 아이디
                if(checkUserId == false){
                    //토스트 메시지
                    Toast.makeText(this@LoginAccount,
                        "존재하지 않는 아이디입니다.", Toast.LENGTH_SHORT).show()

                }else{  //있는 ID일 때

                    //비밀번호가 맞는지 체크, 맞으면 true 반환
                    val checkPass = dbHelper.checkUserPass(str_id, str_password)

                    //비밀번호가 맞을 때
                    if(checkPass == true){
                        //토스트 메시지(로그인 성공)
                        Toast.makeText(this@LoginAccount,
                            "로그인되었습니다.", Toast.LENGTH_SHORT).show()

                        //로그인 성공 + 자동 로그인 체크되었을 때
                        if(btnCheck.isChecked == true){
                            //MySharePreferences.setUserName(this, str_name)
                            MySharePreferences.setUserId(this, str_id)
                            //MySharePreferences.setUserPass(this, str_password)
                        }

                        //화면 전환
                        val intent = Intent(applicationContext, MapActivity::class.java)
                        startActivity(intent)

                    }else{  //비밀번호가 틀릴 때
                        //토스트 메시지
                        Toast.makeText(this@LoginAccount,
                            "잘못된 비밀번호입니다.", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            //sqlitedb.close()
        }
        //dbHelper.close()
    }
}