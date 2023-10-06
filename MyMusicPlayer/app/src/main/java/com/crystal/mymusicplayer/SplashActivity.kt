package com.crystal.mymusicplayer

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    // FirebaseAuth 인스턴스 획득
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        startActivity(Intent(this, MainActivity::class.java))
        finish()

        // SplashActivity 생성 1초 후 auth.currentUser.uid가 null이 아니면 MainActivity로 이동
        // null이면 LoginActivity로 이동
        Handler(Looper.getMainLooper()).postDelayed({
            if (auth.currentUser?.uid != null) {
                moveToMain(auth.currentUser)
            } else {
                // Login 화면 구현 후 넣을 예정
                Toast.makeText(this, "로그인된 계정이 없습니다.", Toast.LENGTH_SHORT).show()
                moveToMain(null)

            }
        }, 1000)

    }

    private fun moveToMain(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}