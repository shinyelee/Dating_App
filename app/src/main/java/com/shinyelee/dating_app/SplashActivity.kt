package com.shinyelee.dating_app

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.shinyelee.dating_app.auth.IntroActivity
import com.shinyelee.dating_app.utils.FirebaseAuthUtils

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    // 태그
//    private val TAG = "SplashActivity"

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // FirebaseAuthUtils 클래스에서 getUid 함수 받아옴
        val uid = FirebaseAuthUtils.getUid()

        // 로그아웃 상태면
        if(uid == "null") {

            // Handler() is deprecated
            // -> 생성자로 Looper.getMainLooper() 넣어주면 됨
            Handler(Looper.getMainLooper()).postDelayed({

                // 둘 다 로딩 화면에 많이 씀
                // FLAG_ACTIVITY_NO_ANIMATION -> 액티비티 실행시 화면 전환 효과(좌우 슬라이드) 무시
                // FLAG_ACTIVITY_NO_HISTORY -> 액티비티를 스택에 쌓지 않음 -> 다른 액티비티로 이동시 사라짐
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION and Intent.FLAG_ACTIVITY_NO_HISTORY)

                // 인트로 액티비티 시작
                startActivity(Intent(this, IntroActivity::class.java))

                // 스플래시 액티비티 종료
                finish()

            // 2초 후 실행
            }, 2000)

            // 로그인 한 상태면
        } else {

            Handler(Looper.getMainLooper()).postDelayed({

                // 플래그 세우고 -> 메인 액티비티 시작 -> 스플래시 액티비티 종료
                val intent = Intent(this, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                startActivity(intent)
                finish()

            }, 2000)

        }

    }

}