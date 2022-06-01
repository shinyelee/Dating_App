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
        val joinBtn = findViewById<Button>(R.id.joinBtn)

        joinBtn.setOnClickListener {

            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)

        }

    }

}