package com.shinyelee.dating_app.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shinyelee.dating_app.MainActivity
import com.shinyelee.dating_app.databinding.ActivityLoginBinding
import java.util.regex.Pattern

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

        // 이메일주소, 비밀번호
        val emailText = binding.email.text.toString()
        val pwText = binding.pw.text.toString()

        // 이메일주소 정규식
        val emailValidation = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

        // 이메일주소 형식
        fun emailPattern(string: String): Boolean {
            val pattern = Pattern.matches(emailValidation, emailText)
            return pattern
        }

        // 이메일주소 리스너
        fun emailTextWatcher() {
            binding.email.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
//                    if (s != null) {
//                        when {
//                            s.isEmpty() -> {
//                                binding.emailArea.error = "이메일주소를 입력하세요"
//                            }
//                            !emailPattern(s.toString()) -> {
//                                binding.emailArea.error = "이메일주소가 올바르지 않습니다"
//                            }
//                            else -> {
//                                binding.emailArea.error = null
//                            }
//                        }
//                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s!!.isEmpty()) {
                        binding.emailArea.error = "이메일주소를 입력하세요"
                    } else if (!emailPattern(s.toString())) {
                        binding.emailArea.error = "이메일주소가 올바르지 않습니다"
                    } else {
                        binding.emailArea.error = null
                    }
                }
            })
        }

        // 비밀번호 리스너
        fun pwTextWatcher() {
            binding.pw.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
//                    if (s != null) {
//                        when {
//                            s.isEmpty() -> {
//                                binding.pwArea.error = "비밀번호를 입력하세요"
//                            }
//                            s.isNotEmpty() -> {
//                                when {
//                                    pwText.length<6 -> {
//                                        binding.pwArea.error = "최소 6자리 이상 입력하세요"
//                                    }
//                                    pwText.length>20 -> {
//                                        binding.pwArea.error = "20자리 이하로 입력하세요"
//                                    }
//                                    else -> {
//                                        binding.pwArea.error = null
//                                    }
//                                }
//                            }
//                            else -> {
//                                binding.pwArea.error = "비밀번호를 다시 확인하세요"
//                            }
//                        }
//                    }
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s!!.isEmpty()) {
                        binding.pwArea.error = "비밀번호를 입력하세요"
                    } else if (pwText.length<6) {
                        binding.pwArea.error = "최소 6자리 이상 입력하세요"
                    } else if (pwText.length>20) {
                        binding.pwArea.error = "20자리 이하로 입력하세요"
                    } else {
                        binding.pwArea.error = null
                    }
                }
            })
        }

        emailTextWatcher()
        pwTextWatcher()

        // 로그인 버튼
        binding.loginBtn.setOnClickListener {
            // 조건 모두 만족하면 로그인
            auth.signInWithEmailAndPassword(emailText, pwText)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, "이메일주소와 비밀번호를 다시 확인하세요", Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }

    override fun onDestroy() {
        vBinding = null
        super.onDestroy()
    }

}