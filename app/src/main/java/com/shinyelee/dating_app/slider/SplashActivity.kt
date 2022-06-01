package com.shinyelee.dating_app.slider

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.shinyelee.dating_app.R
import com.shinyelee.dating_app.auth.IntroActivity
import com.shinyelee.dating_app.utils.FirebaseAuthUtils

class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // FirebaseAuthUtils 클래스에서 getUid 함수 받아옴
        val uid = FirebaseAuthUtils.getUid()

        // uid == null
        if(uid == "null") {

            // 3초 뒤 인트로액티비티로 이동
            Handler().postDelayed({
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }, 3000)

        // uid != "null"
        } else {

            // 3초 뒤 메인액티비티로 이동
            Handler().postDelayed({
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()
            }, 3000)

        }

    }
}