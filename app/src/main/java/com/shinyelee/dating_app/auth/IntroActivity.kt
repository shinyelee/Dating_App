package com.shinyelee.dating_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.shinyelee.dating_app.databinding.ActivityIntroBinding

class IntroActivity : AppCompatActivity() {

    // 태그
//    private val TAG = "IntroActivity"

    // (전역변수) 바인딩 객체 선언
    private var vBinding : ActivityIntroBinding? = null

    // 매번 null 확인 귀찮음 -> 바인딩 변수 재선언
    private val binding get() = vBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // 자동 생성된 뷰바인딩 클래스에서의 inflate 메서드 활용
        // -> 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        vBinding = ActivityIntroBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부 최상위에 있는 뷰의 인스턴스 활용
        // -> 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

        // 로그인 버튼 클릭하면
        binding.loginBtn.setOnClickListener {

            // 명시적 인텐트 -> 다른 액티비티 호출
            // 로그인 액티비티 시작
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

        }

        // 회원가입 버튼 클릭하면
        binding.joinBtn.setOnClickListener {

            // 조인 액티비티 시작
            val intent = Intent(this, JoinActivity::class.java)
            startActivity(intent)

        }

    }

    // 액티비티 파괴시
    override fun onDestroy() {

        // 바인딩 클래스 인스턴스 참조를 정리 -> 메모리 효율이 좋아짐
        vBinding = null
        super.onDestroy()

    }

}