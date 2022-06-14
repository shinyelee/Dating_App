package com.shinyelee.dating_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shinyelee.dating_app.MainActivity
import com.shinyelee.dating_app.R

class LoginActivity : AppCompatActivity() {

    private val TAG = "LoginActivity"

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        // 로그인 버튼
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        // 을 클릭하면
        loginBtn.setOnClickListener {
            // 메일주소, 비밀번호 받아옴
            val email = findViewById<TextInputEditText>(R.id.emailArea)
            val pw = findViewById<TextInputEditText>(R.id.pwArea)
            // 로그인
            auth.signInWithEmailAndPassword(email.text.toString(), pw.text.toString())
                .addOnCompleteListener(this) { task ->
                    // 성공
                    if (task.isSuccessful) {
                        Log.d(TAG, "로그인 성공")
                        // 메인액티비티로 이동
                        val intent = Intent(this, MainActivity::class.java)
                        // 액티비티 관리
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        startActivity(intent)
                    // 실패
                    } else {
                        Log.w(TAG, "로그인 실패", task.exception)
                        Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

}