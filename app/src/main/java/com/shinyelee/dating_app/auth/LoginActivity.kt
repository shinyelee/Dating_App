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
import android.util.Patterns


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
            var emailCheck = true
            var pwCheck = true

            // 이메일주소, 비밀번호
            val emailTxt = binding.email.text.toString()
            val pwTxt = binding.pw.text.toString()

            // 이메일주소 정규식
            val emailPattern = Patterns.EMAIL_ADDRESS

            // 이메일주소 검사
            if(emailTxt.isEmpty()) {
                emailCheck = false
                binding.emailArea.error = "이메일주소를 입력하세요"
            } else if(!emailPattern.matcher(emailTxt).matches()) {
                emailCheck = false
                binding.emailArea.error = "이메일 형식이 잘못되었습니다"
            } else {
                emailCheck = true
                binding.emailArea.error = null
            }

            // 비밀번호 검사
            if(pwTxt.isEmpty()) {
                pwCheck = false
                binding.pwArea.error = "비밀번호를 입력해 주세요"
            } else if (pwTxt.length<6) {
                pwCheck = false
                binding.pwArea.error = "최소 6자 이상 입력하세요"
            } else if (pwTxt.length>20) {
                pwCheck = false
                binding.pwArea.error = "20자 이하로 입력하세요"
            } else {
                pwCheck = true
                binding.pwArea.error = null
            }

            // 로그인조건 모두 만족하면 로그인
            if(emailCheck and pwCheck) {
                auth.signInWithEmailAndPassword(emailTxt, pwTxt)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        } else {
                            Toast.makeText(this, "이메일주소와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "이메일주소와 비밀번호를 확인하세요", Toast.LENGTH_SHORT).show()
            }

        }

    }

    override fun onDestroy() {
        vBinding = null
        super.onDestroy()
    }

}