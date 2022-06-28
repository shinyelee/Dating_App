package com.shinyelee.dating_app.auth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.ktx.storage
import com.shinyelee.dating_app.MainActivity
import com.shinyelee.dating_app.R
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

    // UID
    private var uid = ""
    // 별명
    private var nickname = ""
    // 성별
    private var gender = ""
    // 지역
    private var city = ""
    // 나이
    private var age = ""
    // 프사
    lateinit var profileImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // 뷰바인딩
        vBinding = ActivityJoinBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 파이어베이스 인증
        auth = Firebase.auth

        profileImage = findViewById(R.id.imageArea)
        // 선택한 이미지로 프사 변경
        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                profileImage.setImageURI(uri)
            }
        )

        // 프사 클릭하면 getAction 실행
        profileImage.setOnClickListener {
            getAction.launch("image/*")
        }

        // 회원가입 버튼 -> 가입조건 확인
        binding.joinBtn.setOnClickListener {

            // 가입조건 확인
            var joinAvailable = true

            // 메일주소, 비밀번호, 비밀번호 확인
            val email = binding.email.text.toString()
            val pw = binding.pw.text.toString()
            val pw2 = binding.pw2.text.toString()

            // 별명, 성별, 지역, 나이
            nickname = binding.nickname.text.toString()
            gender = binding.gender.text.toString()
            city = binding.city.text.toString()
            age = binding.age.text.toString()

            // 빈 칸 검사
            if(email.isEmpty() || pw.isEmpty() || pw2.isEmpty() || nickname.isEmpty() || gender.isEmpty() || city.isEmpty() || age.isEmpty()) {
                joinAvailable = false
                Toast.makeText(this, "입력란을 모두 채워주세요", Toast.LENGTH_SHORT).show()
            }

            // 이메일주소 검사
            if(!email.contains("@")) {
                joinAvailable = false
                Toast.makeText(this, "잘못된 이메일주소입니다", Toast.LENGTH_SHORT).show()
            }

            // 비밀번호 검사
            if(pw.length < 6 || pw2.length < 6) {
                joinAvailable = false
                Toast.makeText(this, "비밀번호를 최소 6자리 이상 입력하세요", Toast.LENGTH_SHORT).show()
            }
            if(pw.length > 20 || pw2.length > 20) {
                joinAvailable = false
                Toast.makeText(this, "비밀번호를 20자리 이하로 입력하세요", Toast.LENGTH_SHORT).show()
            }
            if(pw != pw2) {
                joinAvailable = false
                Toast.makeText(this, "비밀번호 불일치", Toast.LENGTH_SHORT).show()
            }

            // 가입조건 모두 만족하면
            if(joinAvailable) {
                // 회원가입
                auth.createUserWithEmailAndPassword(binding.email.text.toString(), binding.pw.text.toString())
                    .addOnCompleteListener(this) { task ->
                        // 성공
                        if (task.isSuccessful) {
                            Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                            // 로그인 확인 위해 현재사용자 UID 받아옴
                            val user = auth.currentUser
                            uid = user?.uid.toString()
                            // 토큰
                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                OnCompleteListener { task ->
                                if (!task.isSuccessful) {
                                    Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                                    return@OnCompleteListener
                                }
                                // Get new FCM registration token
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
                                // 현재 사용자 정보 넣기
                                FirebaseRef.userInfoRef.child(uid).setValue(userModel)
                                // 프사 업로드
                                uploadImage(uid)
                                // 메인액티비티로 이동
                                val intent = Intent(this, MainActivity::class.java)
                                // 액티비티 관리
//                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                startActivity(intent)
                            })
                        // 실패
                        } else {
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
        profileImage.isDrawingCacheEnabled = true
        profileImage.buildDrawingCache()
        val bitmap = (profileImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {}.addOnSuccessListener { taskSnapshot -> }

    }

}