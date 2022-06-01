package com.shinyelee.dating_app.auth

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.google.android.material.textfield.TextInputEditText
import com.shinyelee.dating_app.R

class JoinActivity : AppCompatActivity() {

    private val TAG = "JoinActivity"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val joinBtn = findViewById<Button>(R.id.joinBtn)

        // 회원가입 버튼 클릭릭
        joinBtn.setOnClickListener {

            // 메일주소, 비밀번호 받아옴
            val email = findViewById<TextInputEditText>(R.id.emailArea)
            val pw = findViewById<TextInputEditText>(R.id.pwArea)

            Log.d(TAG, email.toString())
            Log.d(TAG, pw.toString())

        }

    }
}