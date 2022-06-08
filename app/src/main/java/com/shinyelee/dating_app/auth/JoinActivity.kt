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
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.shinyelee.dating_app.MainActivity
import com.shinyelee.dating_app.R
import com.shinyelee.dating_app.utils.FirebaseRef
import java.io.ByteArrayOutputStream

class JoinActivity : AppCompatActivity() {

    private val TAG = "JoinActivity"

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
        setContentView(R.layout.activity_join)

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

        // 회원가입 버튼
        val joinBtn = findViewById<Button>(R.id.joinBtn)
        // 을 클릭하면
        joinBtn.setOnClickListener {

            // 가입조건 확인
            var joinAvailable = true

            // 이메일주소, 비밀번호, 비밀번호 확인
            val email = findViewById<TextInputEditText>(R.id.emailArea)
            val pw = findViewById<TextInputEditText>(R.id.pwArea)
            val pw2 = findViewById<TextInputEditText>(R.id.pw2Area)

            val emailText = email.text.toString()
            val pwText = pw.text.toString()
            val pw2Text = pw2.text.toString()

            // 별명
            nickname = findViewById<TextInputEditText>(R.id.nicknameArea).text.toString()
            // 성별
            gender = findViewById<TextInputEditText>(R.id.genderArea).text.toString()
            // 지역
            city = findViewById<TextInputEditText>(R.id.cityArea).text.toString()
            // 나이
            age = findViewById<TextInputEditText>(R.id.ageArea).text.toString()

            // 빈 칸 검사
            if(emailText.isEmpty() || pwText.isEmpty() || pw2Text.isEmpty() || nickname.isEmpty() || gender.isEmpty() || city.isEmpty() || age.isEmpty()) {
                joinAvailable = false
                Toast.makeText(this, "입력란을 모두 채워주세요", Toast.LENGTH_SHORT).show()
            }

            // 이메일주소 검사
            if(!emailText.contains("@")) {
                joinAvailable = false
                Toast.makeText(this, "잘못된 이메일주소입니다", Toast.LENGTH_SHORT).show()
            }

            // 비밀번호 검사
            if(pwText.length < 6 || pw2Text.length < 6) {
                joinAvailable = false
                Toast.makeText(this, "비밀번호를 최소 6자리 이상 입력하세요", Toast.LENGTH_SHORT).show()
            }
            if(pwText.length > 20 || pw2Text.length > 20) {
                joinAvailable = false
                Toast.makeText(this, "비밀번호를 20자리 이하로 입력하세요", Toast.LENGTH_SHORT).show()
            }
            if(pwText != pw2Text) {
                joinAvailable = false
                Toast.makeText(this, "비밀번호 불일치", Toast.LENGTH_SHORT).show()
            }

            // 가입조건 모두 만족하면
            if(joinAvailable) {
                // 회원가입
                auth.createUserWithEmailAndPassword(email.text.toString(), pw.text.toString())
                    .addOnCompleteListener(this) { task ->
                        // 성공
                        if (task.isSuccessful) {
                            Log.d(TAG, "createUserWithEmail:success")
                            Toast.makeText(this, "회원가입 완료", Toast.LENGTH_SHORT).show()
                            // 로그인 확인 위해 현재사용자 UID 받아옴
                            val user = auth.currentUser
                            uid = user?.uid.toString()
                            val userModel = UserDataModel(
                                uid,
                                nickname,
                                gender,
                                city,
                                age
                            )
                            // 현재 사용자 정보 넣기
                            FirebaseRef.userInfoRef.child(uid).setValue(userModel)
                            // 프사 업로드
                            uploadImage(uid)
                            // 메인액티비티로 이동
                            val intent = Intent(this, MainActivity::class.java)
                            // 액티비티 관리
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                        // 실패
                        } else {
                            Log.w(TAG, "createUserWithEmail:failure", task.exception)
                            Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }

    }

    // 프사 업로드
    private fun uploadImage(uid : String) {

        val storage = Firebase.storage
        val storageRef = storage.reference.child(uid + ".png")

        // 이미지뷰에서 데이터 가져옴
        profileImage.isDrawingCacheEnabled = true
        profileImage.buildDrawingCache()
        val bitmap = (profileImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
        }.addOnSuccessListener { taskSnapshot ->
        }

    }
}