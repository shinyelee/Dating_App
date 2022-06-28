package com.shinyelee.dating_app.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shinyelee.dating_app.auth.IntroActivity
import com.shinyelee.dating_app.databinding.ActivitySettingBinding
import com.shinyelee.dating_app.message.MyLikeActivity
import com.shinyelee.dating_app.message.MyMsgActivity

class SettingActivity : AppCompatActivity() {

    private val TAG = "SettingActivity"

    // 뷰바인딩
    private var vBinding : ActivitySettingBinding? = null
    private val binding get() = vBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // 뷰바인딩
        vBinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 내 정보 버튼 -> 마이페이지액티비티
        binding.myPageBtn.setOnClickListener {
            val intent = Intent(this, MyPageActivity::class.java)
            startActivity(intent)
        }

        // 좋아요 버튼 -> 마이라이크액티비티
        binding.myLikeBtn.setOnClickListener {
            val intent = Intent(this, MyLikeActivity::class.java)
            startActivity(intent)
        }

        // 메시지 버튼 -> 마이메시지액티비티
        binding.myMsgBtn.setOnClickListener {
            // 마이메시지액티비티로 이동
            val intent = Intent(this, MyMsgActivity::class.java)
            startActivity(intent)
        }

        // 로그아웃 버튼
        binding.logoutBtn.setOnClickListener {
            // 로그아웃 후
            val auth = Firebase.auth
            auth.signOut()
            Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show()
            // 인트로액티비티로 이동
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }

    }

}