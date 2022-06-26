package com.shinyelee.dating_app.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shinyelee.dating_app.R
import com.shinyelee.dating_app.auth.IntroActivity
import com.shinyelee.dating_app.message.MyLikeActivity
import com.shinyelee.dating_app.message.MyMsgActivity

class SettingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)

        // 내 정보 버튼
        val myPage = findViewById<Button>(R.id.myPageBtn)
        // 을 클릭하면
        myPage.setOnClickListener {
            // 마이페이지액티비티로 이동
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        // 좋아요 버튼
        val myLike = findViewById<Button>(R.id.myLikeBtn)
        // 을 클릭하면
        myLike.setOnClickListener {
            // 마이라이크액티비티로 이동
            val intent = Intent(this, MyLikeActivity::class.java)
            startActivity(intent)
        }

        // 메시지 버튼
        val myMsg = findViewById<Button>(R.id.myMsgBtn)
        // 을 클릭하면
        myMsg.setOnClickListener {
            // 마이메시지액티비티로 이동
            val intent = Intent(this, MyMsgActivity::class.java)
            startActivity(intent)
        }

        // 로그아웃 버튼
        val logout = findViewById<Button>(R.id.logoutBtn)
        // 을 클릭하면
        logout.setOnClickListener {
            // 로그아웃 후
            val auth = Firebase.auth
            auth.signOut()
            Toast.makeText(this, "로그아웃 완료", Toast.LENGTH_SHORT).show()
            // 인트로액티비티로 이동
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }

    }

}