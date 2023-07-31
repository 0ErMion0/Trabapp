package com.example.trabapp

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import com.example.test.DBHelper
import com.example.test.MySharePreferences

class MyInfo : AppCompatActivity() {
    lateinit var backButton: ImageButton // 뒤로 가기 버튼

    lateinit var edtName : EditText
    lateinit var edtCurrentPass : EditText
    lateinit var edtNewPass : EditText
    lateinit var edtPassRe : EditText

    lateinit var btnConfirm : Button

    lateinit var dbHelper : DBHelper
    lateinit var sqlitedb : SQLiteDatabase

    lateinit var str_name : String
    lateinit var str_id : String
    lateinit var str_password : String
    lateinit var str_newPass : String
    lateinit var str_PassRe : String

    lateinit var str_newName : String

    lateinit var id : String

    @SuppressLint("Range")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_info)

        edtName = findViewById(R.id.nickName)
        edtCurrentPass = findViewById(R.id.currentPassword)
        edtNewPass = findViewById(R.id.newPassword)
        edtPassRe = findViewById(R.id.repeatNewPassword)

        btnConfirm = findViewById(R.id.btnConfirm)

        str_id = MySharePreferences.id

        dbHelper = DBHelper(this)
        sqlitedb = dbHelper.writableDatabase

        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM users WHERE userId = '" + str_id + "';", null)

        if(cursor.moveToNext()){
            str_name = cursor.getString(cursor.getColumnIndex("userName")).toString()
            str_password = cursor.getString(cursor.getColumnIndex("userPassword")).toString()
        }

        edtName.setText(str_name)

        //버튼을 눌렀을 때
        btnConfirm.setOnClickListener {

            cursor = sqlitedb.rawQuery("SELECT * FROM users WHERE userId = '" + str_id + "';", null)

            if(cursor.moveToNext()){
                str_name = cursor.getString(cursor.getColumnIndex("userName")).toString()
                str_password = cursor.getString(cursor.getColumnIndex("userPassword")).toString()

                Log.v("알람0", "AA")
            }

            Log.v("알람1", str_password)
            Log.v("알람1", str_name)
            
            //현재 닉네임 칸에 입력된 닉네임 얻어옴
            str_newName = edtName.text.toString()
            
            //기존에 입력된 닉네임과 현재 입력되어 있는 닉네임이 다를 경우
            if(str_name != str_newName)
            {   
                //닉네임 변경 및 DB 업데이트
                sqlitedb.execSQL("UPDATE users SET userName = '" + edtName.text + "' WHERE userId = '" + str_id + "';")
                //토스트 메시지
                Toast.makeText(this@MyInfo,
                    "닉네임 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show()
            }

            //하나라도 비어있지 않은 경우, 비밀번호를 변경한다고 생각
            if(edtCurrentPass.text.toString() != "" || edtNewPass.text.toString() != "" ||
                edtPassRe.text.toString() != ""){

                //하나라도 비어 있으면,
                if(edtCurrentPass.text.toString() == "" || edtNewPass.text.toString() == "" ||
                    edtPassRe.text.toString() == ""){

                    //토스트 메시지
                    Toast.makeText(this@MyInfo,
                        "정보를 모두 입력해주세요.", Toast.LENGTH_SHORT).show()
                }
                else{   //전부 채워져 있으면

                    str_newPass = edtNewPass.text.toString()
                    str_PassRe = edtPassRe.text.toString()
                    
                    //비밀번호가 맞는지 체크, 맞으면 true 반환
                    val checkPass = dbHelper.checkUserPass(str_id, str_password)

                    //비밀번호가 맞을 때
                    if(str_password == edtCurrentPass.text.toString()){
                        //새비밀번호와 비밀번호 확인이 동일하지 않을 때
                        if(str_newPass != str_PassRe){
                            //토스트 메시지(로그인 성공)
                            Toast.makeText(this@MyInfo,
                                "새 비밀번호 확인이 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                        }else{
                            sqlitedb.execSQL("UPDATE users SET userPassword = '" + str_newPass + "' WHERE userId = '" + str_id + "';")
                            
                            //토스트 메시지(로그인 성공)
                            Toast.makeText(this@MyInfo,
                                "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show()
                        }
                    }else{  //비밀번호가 틀릴 때
                        //토스트 메시지
                        Toast.makeText(this@MyInfo,
                            "현재 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                    }

                }
            }
            Log.v("알람2", str_password)
            Log.v("알람2", str_name)
        }

        //sqlitedb.close()

        // 뒤로 가기 버튼
        backButton = findViewById<ImageButton>(R.id.imgBtnBack)
        backButton.setOnClickListener{
            // 클릭되면 MapActivity로 이동
            val intent: Intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }
    }
}