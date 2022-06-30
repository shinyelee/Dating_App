package com.shinyelee.dating_app.auth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import com.shinyelee.dating_app.MainActivity
import com.shinyelee.dating_app.databinding.ActivityJoinBinding
import com.shinyelee.dating_app.utils.FirebaseRef
import java.io.ByteArrayOutputStream

class JoinActivity : AppCompatActivity() {

    private val TAG = "JoinActivity"

    // 뷰바인딩
    private var vBinding : ActivityJoinBinding? = null
    private val binding get() = vBinding!!

    // 파이어베이스 인증
    private lateinit var auth: FirebaseAuth

    // UID, 별명, 성별, 지역, 나이, 프사
    private var uid = ""
    private var nickname = ""
    private var gender = ""
    private var city = ""
    private var age = ""
    private lateinit var selfie : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // 뷰바인딩
        vBinding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 파이어베이스 인증
        auth = Firebase.auth

        selfie = binding.selfie

        // 선택한 이미지로 프사 변경
        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                selfie.setImageURI(uri)
            }
        )
        selfie.setOnClickListener {
            getAction.launch("image/*")
        }

        // 회원가입 버튼
        binding.joinBtn.setOnClickListener {

            // 가입조건 확인
            var joinAvailable = true

            // 메일주소, 비밀번호, 비밀번호 확인
            val emailTxt = binding.email.text.toString()
            val pwTxt = binding.pw.text.toString()
            val pw2Txt = binding.pw2.text.toString()

            // 별명, 성별, 지역, 나이
            nickname = binding.nickname.text.toString()
            gender = binding.gender.text.toString()
            city = binding.city.text.toString()
            age = binding.age.text.toString()

            // 빈 칸 검사
            if(emailTxt.isEmpty() || pwTxt.isEmpty() || pw2Txt.isEmpty() || nickname.isEmpty() || gender.isEmpty() || city.isEmpty() || age.isEmpty()) {
                joinAvailable = false
                Toast.makeText(this, "입력란을 모두 채워주세요", Toast.LENGTH_SHORT).show()
            }

            // 이메일주소 검사
            if(!emailTxt.contains("@") || !emailTxt.contains(".")) {
                joinAvailable = false
                binding.emailArea.error = "형식이 올바르지 않습니다"
            }

            // 비밀번호 검사
            if(pwTxt.length < 6 || pwTxt.length > 20) {
                joinAvailable = false
                binding.pwArea.error = "최소 6자리 이상 20자리 이하로 입력하세요"
            }
            if(pw2Txt.length < 6 || pw2Txt.length > 20) {
                joinAvailable = false
                binding.pw2Area.error = "최소 6자리 이상 20자리 이하로 입력하세요"
            }
            if(pwTxt != pw2Txt) {
                joinAvailable = false
                binding.pw2Area.error = "비밀번호가 일치하지 않습니다"
            }

            // 가입조건 모두 만족하면 회원가입
            if(joinAvailable) {
                auth.createUserWithEmailAndPassword(emailTxt, pwTxt)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                            val user = auth.currentUser
                            uid = user?.uid.toString()
                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                OnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                                    return@OnCompleteListener
                                }
                                val token = task.result.toString()
                                Log.e(TAG, "user token value - $token")
                                val userModel = UserDataModel(
                                    uid,
                                    nickname,
                                    gender,
                                    city,
                                    age,
                                    token
                                )
                                FirebaseRef.userInfoRef.child(uid).setValue(userModel)
                                uploadImage(uid)
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            })
                        } else {
                            binding.email.error = "이미 가입된 이메일입니다"
                            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()

                        }
                    }
            }

        }

    }

    // 프사 업로드
    private fun uploadImage(uid : String) {

        val storage = Firebase.storage
        val storageRef = storage.reference.child("$uid.png")

        // 이미지뷰에서 데이터 가져옴
        selfie.isDrawingCacheEnabled = true
        selfie.buildDrawingCache()
        val bitmap = (selfie.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {}.addOnSuccessListener { taskSnapshot -> }

    }

    override fun onDestroy() {
        vBinding = null
        super.onDestroy()
    }

}