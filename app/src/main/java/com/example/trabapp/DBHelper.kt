package com.example.test

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper (context: Context?)
    : SQLiteOpenHelper (context, "users", null, 1){

    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE users(userId text primary key, userName text, userPassword text)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("drop Table if exists users")
    }

    //존재하는 id인지
    fun checkUserId(id: String) : Boolean{
        //우선 존재하지 않는다고 설정
        var res = false

        //db 쓸 수 있게 함
        val MyDB = this.writableDatabase
        //users 테이블에서 userId가 id(받아온 값)인 값
        var cursor = MyDB.rawQuery("SELECT * FROM users WHERE userId = ?", arrayOf(id))

        //0보다 크면 존재하는 id
        if(cursor.count > 0)
        //존재 여부 true로 설정
            res = true

        //닫기
        //MyDB.close()
        cursor.close()

        //반환
        return res
    }

    //패스워드 맞는지 체크
    fun checkUserPass(id: String, password: String):Boolean{
        //일단 틀리다고 설정
        var res = true

        //db 쓸 수 있게 함
        val MyDB = this.writableDatabase
        //users 테이블에서 userId와 userPassword가 id, password(받아온 값)인 값
        val cursor = MyDB.rawQuery("SELECT * FROM users WHERE userId = ? and userPassword = ?", arrayOf(id, password))

        //0보다 크면 존재 (아이디와 비밀번호가 전부 존재, 일치)
        if(cursor.count <= 0)
            res = false

        //닫기
        //MyDB.close()
        cursor.close()

        //반환
        return res
    }
}