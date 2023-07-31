package com.example.trabapp

import android.annotation.SuppressLint
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.trabapp.databinding.ActivityDiaryInfoBinding
import com.example.trabapp.databinding.ActivityMyMemoryBinding

@SuppressLint("Range")
class DiaryInfo : AppCompatActivity() {
    lateinit var backButton: ImageButton // 뒤로 가기 버튼

    lateinit var dbManager: DBManager
    lateinit var sqlitedb: SQLiteDatabase

    // id 정의
    lateinit var diMainTitle: TextView
    lateinit var diEditBtn: ImageButton
    lateinit var imgBtnDel: ImageButton
    lateinit var diDate: TextView
    lateinit var imgPic: ImageView
    lateinit var imgFace: ImageView
    lateinit var diTitle: TextView
    lateinit var diCont: TextView

    // db에서 가져올 변수
    lateinit var str_diTitle: String
    lateinit var str_diContents: String
    lateinit var str_diStartDate: String
    lateinit var str_diEndDate: String
    lateinit var str_diImg: String
    lateinit var str_diEmotion: String
    lateinit var str_memTitleForDi: String

    private lateinit var binding: ActivityDiaryInfoBinding // 레이아웃과 연결

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_diary_info)

        // 뷰 바인딩
        binding = ActivityDiaryInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbManager = DBManager(this)
        //sqlitedb = dbManager.readableDatabase
        sqlitedb = dbManager.writableDatabase

        val intent = intent
        str_diTitle = intent.getStringExtra("intent_diTitle").toString()

        // ---id 연결---
        diMainTitle = findViewById<TextView>(R.id.textTitle)
        diEditBtn = findViewById<ImageButton>(R.id.imgBtnEdit)
        //diDelBtn = findViewById<ImageButton>(R.id.imgBtnDel)
        diDate = findViewById<TextView>(R.id.textDate)
        imgPic = findViewById<ImageView>(R.id.imgPic)
        imgFace = findViewById<ImageView>(R.id.imgFace)
        diTitle = findViewById<TextView>(R.id.textTitle2)
        diCont = findViewById<TextView>(R.id.textDiContents)

        // 내용 불러오기
        loadDiary()

        // 편집 버튼
        diEditBtn.setOnClickListener{
            // 클릭되면 추억 상세 페이지로 이동 (구현해야됨)
            val intent = Intent(this, DiaryEdit::class.java)
            intent.putExtra("intent_diTitle", str_diTitle)
            startActivity(intent)
        }

        // 삭제 버튼
        imgBtnDel = findViewById<ImageButton>(R.id.imgBtnDel)
        binding.imgBtnDel.setOnClickListener{
            // 삭제할건지 팝업 뜸
            // Dialog
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.diary_del_popup, null)
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()

            // 확인 버튼
            val okButton = mDialogView.findViewById<Button>(R.id.deleteAcceptBtn)
            okButton.setOnClickListener{
                // 확인 버튼 누르면
                // 일지 삭제함
                val intent = Intent(this, MemoryInfo::class.java)
                intent.putExtra("intent_memTitle", str_memTitleForDi)

                // 데이터 삭제되고
                sqlitedb.execSQL("DELETE FROM diaries WHERE diTitle = '" + str_diTitle + "';'")
                Toast.makeText(this, "일지가 삭제되었습니다", Toast.LENGTH_SHORT).show()

                // MemoryInfo로 이동
                startActivity(intent)
            }
            // 취소 버튼
            val noButton = mDialogView.findViewById<Button>(R.id.cancelBtn)
            noButton.setOnClickListener {
                mAlertDialog.dismiss() // 원래 화면으로 돌아감
            }

//            // MemoryInfo로 이동
//            val intent = Intent(this, MemoryInfo::class.java)
//            intent.putExtra("intent_memTitle", str_memTitleForDi)
//
//            // 데이터 삭제되고
//            sqlitedb.execSQL("DELETE FROM diaries WHERE diTitle = '" + str_diTitle + "';'")
//
//            // MemoryInfo로 이동
//            startActivity(intent)
        }

        // 뒤로 가기 버튼
        backButton = findViewById<ImageButton>(R.id.imgBtnBack)
        backButton.setOnClickListener{
            // 클릭되면 추억 상세 페이지로 이동 (구현해야됨)
            val intent = Intent(this, MemoryInfo::class.java)
            intent.putExtra("intent_memTitle", str_memTitleForDi)
            intent.putExtra("intent_diTitle", str_diTitle)
            startActivity(intent)
        }
    }

    // 데이터베이스 조회 후 일지 내용 추가하는 함수
    private fun loadDiary(){
        var cursor : Cursor
        cursor = sqlitedb.rawQuery("SELECT * FROM diaries WHERE diTitle = '" + str_diTitle + "';'", null)

        while (cursor.moveToNext()){
            // 데이터베이스에 저장된 값 가져옴
            //str_diTitle = cursor.getString(cursor.getColumnIndex("diTitle")).toString() // intent 통해 받아옴
            str_diContents = cursor.getString(cursor.getColumnIndex("diContents")).toString()
            str_diStartDate = cursor.getString(cursor.getColumnIndex("diStartDate")).toString()
            str_diEndDate = cursor.getString(cursor.getColumnIndex("diEndDate")).toString()
            //str_diImg = cursor.getString(cursor.getColumnIndex("diImg")).toString()
            str_diEmotion = cursor.getString(cursor.getColumnIndex("diEmotion")).toString()
            str_memTitleForDi = cursor.getString(cursor.getColumnIndex("memTitleForDi")).toString()

            // 저장된 값 대입
            diMainTitle.text = str_diTitle
            diDate.text = "$str_diStartDate ~ $str_diEndDate"
            //imgPic =
            when(str_diEmotion){
                "ReallyBad"->imgFace.setImageResource(R.drawable.face_really_bad)
                "Bad"->imgFace.setImageResource(R.drawable.face_bad)
                "Soso"->imgFace.setImageResource(R.drawable.face_soso)
                "Good"->imgFace.setImageResource(R.drawable.face_good)
                "ReallyGood"->imgFace.setImageResource(R.drawable.face_really_good)
            }
            diTitle.text = str_diTitle
            diCont.text = str_diContents
        }

        cursor.close()
        //sqlitedb.close()
        //dbManager.close()
    }
}