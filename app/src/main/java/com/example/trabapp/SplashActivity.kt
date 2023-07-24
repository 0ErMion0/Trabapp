package com.example.trabapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            startActivity(intent)
            finish() // 끝내기
        },DURATION)

    }
    companion object {
        private const val DURATION : Long = 3000 // 3초동안 보여짐
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}