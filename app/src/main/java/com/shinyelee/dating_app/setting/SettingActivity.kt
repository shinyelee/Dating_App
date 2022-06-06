package com.shinyelee.dating_app.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shinyelee.dating_app.R
import com.shinyelee.dating_app.auth.IntroActivity

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        // 내 정보 아이콘
        val mypage = findViewById<ImageView>(R.id.mypageIcon)

        // 을 클릭하면
        mypage.setOnClickListener {

            // 마이페이지액티비티로 이동
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)

        }

        // 로그아웃 아이콘
        val logout = findViewById<ImageView>(R.id.logoutIcon)

        // 을 클릭하면
        logout.setOnClickListener {

            // 로그아웃 후
            val auth = Firebase.auth
            auth.signOut()

            Toast.makeText(this, "로그아웃 되었습니다", Toast.LENGTH_SHORT).show()

            // 인트로액티비티로 이동
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)

        }

    }

}