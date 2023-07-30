package com.example.trabapp

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBManager (
    context: Context?,
//    name: String?,
//    factory: SQLiteDatabase.CursorFactory?,
//    version: Int
//) : SQLiteOpenHelper(context, name, factory, version) {
) : SQLiteOpenHelper(context, "memoryDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        // 추억 테이블
        db!!.execSQL("CREATE TABLE memories(" +
                "memTitle text, " +
                "memMb text, " +
                "memStartDate text, " +
                "memEndDate text, " +
                "memColor text)")
        // 일지 테이블
        db!!.execSQL("CREATE TABLE diaries(" +
                "diTitle text, " +
                "diContents text, " +
                "diStartDate text, " +
                "diEndDate text, " +
                "diImg blob)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    // 데이터 조회
    fun getAllMemories(): Cursor {
        val db = this.readableDatabase
        return db.rawQuery("SELECT * FROM memories", null)
    }


}