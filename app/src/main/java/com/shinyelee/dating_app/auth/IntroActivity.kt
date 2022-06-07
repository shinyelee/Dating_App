package com.shinyelee.dating_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.shinyelee.dating_app.R

class IntroActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)

        // android-kotlin-extension -> 현재 사용 불가
        // findViewById -> 초보자용
        // DataBinding, ViewBinding -> 권장

        // 로그인 버튼
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        // 을 클릭하면
        loginBtn.setOnClickListener {
            // 로그인액티비티로 이동
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 회원가입 버튼
        val joinBtn = findViewById<Button>(R.id.joinBtn)
        // 을 클릭하면
        joinBtn.setOnClickListener {
            // 조인액티비티로 이동
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

    }

}