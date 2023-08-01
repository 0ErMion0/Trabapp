package com.example.test

import android.content.Context
import android.content.SharedPreferences

object MySharePreferences {
    private val MY_ACCOUNT : String = "account"
    //var id : String = "id"
    var id : String = ""

//    fun setUserName(context: Context, name: String){
//        val prefs: SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
//        val editor: SharedPreferences.Editor = prefs.edit()
//
//        //MY_NAME에 name 넣음
//        editor.putString("MY_NAME", name)
//        editor.commit()
//    }
//
//    fun getUserName(context: Context): String{
//        val prefs: SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
//
//        //MY_NAME에 있는 값 Get
//        return prefs.getString("MY_NAME", "").toString()
//    }

    fun setUserId(context: Context, id: String){
        val prefs: SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()

        //MY_ID에 id 넣음
        editor.putString("MY_ID", id)
        editor.commit()

        this.id = id
    }

    fun getUserId(context: Context): String{
        val prefs: SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)

        //MY_ID에 있는 값 Get
        return prefs.getString("MY_ID", "").toString()
    }

//    fun setUserPass(context: Context, password: String){
//        val prefs: SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
//        val editor: SharedPreferences.Editor = prefs.edit()
//
//        editor.putString("MY_PASS", password)
//        editor.commit()
//    }
//
//    fun getUserPass(context: Context): String{
//        val prefs: SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
//        return prefs.getString("MY_PASS", "").toString()
//    }

    fun clearUser(context: Context){
        val prefs: SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = prefs.edit()

        editor.clear()
        editor.commit()
    }
}