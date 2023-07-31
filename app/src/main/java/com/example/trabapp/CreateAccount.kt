package com.example.trabapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.test.DBHelper

class CreateAccount : AppCompatActivity() {

    lateinit var dbHelper: DBHelper
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var edtName: EditText
    lateinit var edtId: EditText
    lateinit var edtPassword: EditText
    lateinit var edtRePassWord: EditText

    lateinit var btnReg: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        dbHelper = DBHelper(this)

        edtName = findViewById(R.id.edtNickname)
        edtId = findViewById(R.id.edtId)
        edtPassword = findViewById(R.id.edtPass)
        edtRePassWord = findViewById(R.id.edtPassVer)

        btnReg = findViewById(R.id.btnJoin)

        //가입하기 클릭
        btnReg.setOnClickListener {

            sqlitedb = dbHelper.writableDatabase

            var str_name: String = edtName.text.toString()
            var str_id: String = edtId.text.toString()
            var str_password : String = edtPassword.text.toString()
            var str_re_password : String = edtRePassWord.text.toString()

            //정보가 하나라도 입력되지 않았을 경우
            if(str_name == "" || str_id == "" || str_password == "" || str_re_password == ""){
                //토스트 메시지
                Toast.makeText(this@CreateAccount,
                    "회원정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()

            }else{  //회원 정보가 전부 입력되었을 경우

                //존재하는 아이디인지 체크, 존재하면 true
                val checkUserId = dbHelper.checkUserId(str_id)

                //이미 존재하는 아이디일 경우
                if(checkUserId == true){
                    //토스트 메시지
                    Toast.makeText(this@CreateAccount,
                        "이미 존재하는 아이디입니다.", Toast.LENGTH_SHORT).show()

                }else{ //가입 가능한 아이디인 경우

                    //패스워드가 불일치할 때
                    if(str_password != str_re_password){
                        //토스트 메시지
                        Toast.makeText(this@CreateAccount,
                            "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()

                    }else{//패스워드 일치

                        //데이터 삽입
                        sqlitedb.execSQL("INSERT INTO users VALUES ('"
                                + str_id + "', '" + str_name + "', '" + str_password + "');")

                        //토스트 메시지
                        Toast.makeText(this@CreateAccount, "가입되었습니다.", Toast.LENGTH_SHORT).show()

                        //화면 전환
                        val intent = Intent(applicationContext, LoginAccount::class.java)
                        startActivity(intent)
                    }
                }
            }
            //sqlitedb.close()
        }
        //dbHelper.close()
    }
}