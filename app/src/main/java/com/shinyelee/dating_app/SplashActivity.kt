package com.shinyelee.dating_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.shinyelee.dating_app.auth.IntroActivity
import com.shinyelee.dating_app.utils.FirebaseAuthUtils

class SplashActivity : AppCompatActivity() {

    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // FirebaseAuthUtils 클래스에서 getUid 함수 받아옴
        val uid = FirebaseAuthUtils.getUid()
        // 로그인 안 한 상태
        if(uid == "null") {
            // 3초 뒤 인트로액티비티로 이동
            Handler().postDelayed({
                val intent = Intent(this, IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }, 3000)
        // 로그인 중이면
        } else {
            // 3초 뒤 메인액티비티로 이동
            Handler().postDelayed({
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(intent)
                finish()
            }, 3000)
        }

    }
}