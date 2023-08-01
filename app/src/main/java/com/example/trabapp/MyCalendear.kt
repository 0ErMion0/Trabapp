package com.example.trabapp

import android.annotation.SuppressLint
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.util.Collections

class MyCalendear : AppCompatActivity() {
    lateinit var backButton: ImageButton // 뒤로 가기 버튼

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    lateinit var str_memTitle: String
    lateinit var str_startDate: String
    lateinit var str_endDate: String
    lateinit var str_memColor: String

    lateinit var calendar : MaterialCalendarView

    lateinit var layout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_calendear)

        calendar = findViewById(R.id.calender)
        layout = findViewById(R.id.groupLinearLayout)

        // ---뒤로 가기 버튼---
        backButton = findViewById<ImageButton>(R.id.imgBtnBack)
        backButton.setOnClickListener{
            // 클릭되면 MapActivity로 이동
            val intent: Intent = Intent(this, MapActivity::class.java)
            startActivity(intent)
        }

        //DB 불러오기
        loadMemories()

        //오늘 날짜로 설정
        calendar.setSelectedDate(CalendarDay.today())

        layout.removeAllViews()
        draw(CalendarDay.today().year, CalendarDay.today().month+1,CalendarDay.today().day)

        //날짜를 옮기면(선택된 게 변하면)
        calendar.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                layout.removeAllViews()

                var year = date.year
                var month = date.month
                var day = date.day

                draw(year, month+1, day)
            }
        })

    }

    @SuppressLint("Range")
    private fun draw(year:Int, month:Int, day:Int){
        dbManager = DBManager(this)
        sqlitedb = dbManager.readableDatabase

        val cursor = dbManager.getAllMemories()

        var num: Int = 0

        //val ItemView = layoutInflater.inflate(R.layout.memory_item_layout, null)

        while (cursor.moveToNext()) {
            val ItemView = layoutInflater.inflate(R.layout.memory_item_layout, null)

            // 데이터베이스에 저장된 값 가져옴
            str_memTitle = cursor.getString(cursor.getColumnIndex("memTitle")).toString()
            str_startDate = cursor.getString(cursor.getColumnIndex("memStartDate")).toString()
            str_endDate = cursor.getString(cursor.getColumnIndex("memEndDate")).toString()
            str_memColor = cursor.getString(cursor.getColumnIndex("memColor")).toString()

            //날짜 슬라이싱
            var startYear = sliceDate(str_startDate,0)
            var startMonth = sliceDate(str_startDate,1)
            var startDay = sliceDate(str_startDate,2)

            var endYear = sliceDate(str_endDate,0)
            var endMonth = sliceDate(str_endDate,1)
            var endDay = sliceDate(str_endDate,2)

            if(startYear==year && startMonth==month && startDay==day){

                val groupColor = ItemView.findViewById<ImageView>(R.id.groupColor)
                val groupName = ItemView.findViewById<TextView>(R.id.groupName)
                val groupDate = ItemView.findViewById<TextView>(R.id.groupDate)

                //ItemView.id = num // 목록 번호

                groupName.text = str_memTitle + " 시작" // 그룹 이름 설정
                groupDate.text = "$str_startDate ~ $str_endDate" // 날짜 설정

                when (str_memColor) {
                    "pink" -> groupColor.setImageResource(R.drawable.circle_red)
                    "orange" -> groupColor.setImageResource(R.drawable.circle_orange)
                    "green" -> groupColor.setImageResource(R.drawable.circle_green)
                    "mint" -> groupColor.setImageResource(R.drawable.circle_mint)
                    "blue" -> groupColor.setImageResource(R.drawable.circle_blue)
                    "purple" -> groupColor.setImageResource(R.drawable.circle_purple)
                }

//                // 레이아웃 클릭하면 추억 기록으로 이동 및 데이터 전달
//                ItemView.setOnClickListener {
//                    val intent = Intent(this@MyCalendear, MemoryInfo::class.java)
//                    intent.putExtra("intent_memTitle", str_memTitle)
//                    startActivity(intent)
//                }

                // Add the memory item view to LinearLayout
                layout.addView(ItemView)
            }

            if(endYear==year && endMonth==month && endDay==day){

                val groupColor = ItemView.findViewById<ImageView>(R.id.groupColor)
                val groupName = ItemView.findViewById<TextView>(R.id.groupName)
                val groupDate = ItemView.findViewById<TextView>(R.id.groupDate)

                //ItemView.id = num // 목록 번호

                groupName.text = str_memTitle + " 끝" // 그룹 이름 설정
                groupDate.text = "$str_startDate ~ $str_endDate" // 날짜 설정

                when (str_memColor) {
                    "pink" -> groupColor.setImageResource(R.drawable.circle_red)
                    "orange" -> groupColor.setImageResource(R.drawable.circle_orange)
                    "green" -> groupColor.setImageResource(R.drawable.circle_green)
                    "mint" -> groupColor.setImageResource(R.drawable.circle_mint)
                    "blue" -> groupColor.setImageResource(R.drawable.circle_blue)
                    "purple" -> groupColor.setImageResource(R.drawable.circle_purple)
                }

//                // 레이아웃 클릭하면 추억 기록으로 이동 및 데이터 전달
//                ItemView.setOnClickListener {
//                    val intent = Intent(this@MyCalendear, MemoryInfo::class.java)
//                    intent.putExtra("intent_memTitle", str_memTitle)
//                    startActivity(intent)
//                }

                // Add the memory item view to LinearLayout
                layout.addView(ItemView)
            }

            num++
        }
    }

    @SuppressLint("Range")
    private fun loadMemories() {
        dbManager = DBManager(this)
        sqlitedb = dbManager.readableDatabase

        val cursor = dbManager.getAllMemories()

        var num: Int = 0
        while (cursor.moveToNext()) {
            // 데이터베이스에 저장된 값 가져옴
            str_memTitle = cursor.getString(cursor.getColumnIndex("memTitle")).toString()
            str_startDate = cursor.getString(cursor.getColumnIndex("memStartDate")).toString()
            str_endDate = cursor.getString(cursor.getColumnIndex("memEndDate")).toString()
            str_memColor = cursor.getString(cursor.getColumnIndex("memColor")).toString()

            //날짜 슬라이싱
            var startYear = sliceDate(str_startDate,0)
            var startMonth = sliceDate(str_startDate,1)
            var startDay = sliceDate(str_startDate,2)

            var endYear = sliceDate(str_endDate,0)
            var endMonth = sliceDate(str_endDate,1)
            var endDay = sliceDate(str_endDate,2)

            var color: String

            when (str_memColor) {
                "pink" -> color = "#FF96BE"
                "orange" -> color = "#FFD494"
                "green" -> color = "#BDFF94"
                "mint" -> color = "#AEFFF5"
                "blue" -> color = "#94C5FF"
                "purple" -> color = "#E394FF"
                else -> color = "#000000"
            }

            calendar.addDecorator(
                EventDecorator(
                    Color.parseColor(color),
                    Collections.singleton(
                        CalendarDay.from(startYear, startMonth-1, startDay)
                    )
                )
            )

            calendar.addDecorator(
                EventDecorator(
                    Color.parseColor(color),
                    Collections.singleton(
                        CalendarDay.from(endYear, endMonth-1, endDay)
                    )
                )
            )


        }

        sliceDate(str_startDate,0)

        cursor.close()
        sqlitedb.close()
        dbManager.close()
    }

    private fun sliceDate(date: String, index: Int):Int{
        ////날짜 슬라이싱
        //.을 기준으로 자름
        var str_arr = date.split(".")

        //연월일 구분
        var year = str_arr.get(0)
        var month = str_arr.get(1)
        var dayOfMonth = str_arr.get(2)

        //공백 제거
        month = month.substring(1,month.length)
        dayOfMonth = dayOfMonth.substring(1,dayOfMonth.length)

        when(index){
            0 -> return year.toInt()
            1 -> return month.toInt()
            2 -> return dayOfMonth.toInt()
            else-> return 0
        }
    }


}