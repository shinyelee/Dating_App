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

    // 태그
//    private val TAG = "LoginActivity"

    // 파이어베이스 인스턴스 선언
    private lateinit var auth: FirebaseAuth

    // (전역변수) 바인딩 객체 선언
    private var vBinding : ActivityLoginBinding? = null

    // 매번 null 확인 귀찮음 -> 바인딩 변수 재선언
    private val binding get() = vBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // 파이어베이스 인스턴스 초기화
        auth = Firebase.auth

        // 자동 생성된 뷰바인딩 클래스에서의 inflate 메서드 활용
        // -> 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        vBinding = ActivityLoginBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부 최상위에 있는 뷰의 인스턴스 활용
        // -> 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

        // 로그인 버튼 클릭하면
        binding.loginBtn.setOnClickListener {

            // 로그인조건 확인
            var emailCheck = true
            var pwCheck = true

            // 이메일, 비밀번호
            val emailTxt = binding.email.text.toString()
            val pwTxt = binding.pw.text.toString()

            // 이메일 정규식
            val emailPattern = Patterns.EMAIL_ADDRESS

            // 이메일 검사
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

            // 로그인 조건을 모두 만족하면
            if(emailCheck and pwCheck) {

                // 로그인
                auth.signInWithEmailAndPassword(emailTxt, pwTxt)
                    .addOnCompleteListener(this) { task ->

                        // 성공하면
                        if (task.isSuccessful) {

                            // 명시적 인텐트 -> 다른 액티비티 호출
                            val intent = Intent(this, MainActivity::class.java)

                            // 메인 액티비티 시작
                            startActivity(intent)

                            // 로그인 액티비티 종료
                            finish()

                        // 오류 등 -> 로그인 실패
                        } else { Toast.makeText(this, "이메일과 비밀번호를 다시 확인하세요", Toast.LENGTH_SHORT).show() }

                    }

            // 조건 불만족하면 로그인 실패
            } else { Toast.makeText(this, "이메일과 비밀번호를 다시 확인하세요", Toast.LENGTH_SHORT).show() }

        }

    }

    // 액티비티 파괴시
    override fun onDestroy() {

        // 바인딩 클래스 인스턴스 참조를 정리 -> 메모리 효율이 좋아짐
        vBinding = null
        super.onDestroy()

    }

}