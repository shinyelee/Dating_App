package com.shinyelee.dating_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shinyelee.dating_app.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    private val TAG = "IntroActivity"

    // 뷰바인딩
    private var vBinding : ActivityIntroBinding? = null
    private val binding get() = vBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // 뷰바인딩
        vBinding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 로그인 버튼 -> 로그인액티비티
        binding.loginBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 회원가입 버튼 -> 조인액티비티
        binding.joinBtn.setOnClickListener {
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)
        }

    }

}