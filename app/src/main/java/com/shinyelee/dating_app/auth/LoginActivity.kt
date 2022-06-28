package com.shinyelee.dating_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shinyelee.dating_app.MainActivity
import com.shinyelee.dating_app.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    // 뷰바인딩
    private var vBinding : ActivityLoginBinding? = null
    private val binding get() = vBinding!!

    // 파이어베이스 인증
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // 뷰바인딩
        vBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 파이어베이스 인증
        auth = Firebase.auth

        // 로그인 버튼
        binding.loginBtn.setOnClickListener {

            // 로그인조건 확인
            var loginAvailable = true

            // 메일주소, 비밀번호
            val email = binding.email.text.toString()
            val pw = binding.pw.text.toString()

            // 빈 칸 검사
            if(email.isEmpty()) {
                loginAvailable = false
                binding.emailArea.error = "이메일주소를 입력해 주세요"
            }
            if(pw.isEmpty()) {
                loginAvailable = false
                binding.pwArea.error = "비밀번호를 입력해 주세요"
            }

            // 로그인조건 모두 만족하면 로그인
            if(loginAvailable) {
                auth.signInWithEmailAndPassword(binding.email.text.toString(), binding.pw.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }

    }

    override fun onDestroy() {
        vBinding = null
        super.onDestroy()
    }

}