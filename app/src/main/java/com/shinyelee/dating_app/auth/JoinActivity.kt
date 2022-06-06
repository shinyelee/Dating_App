package com.shinyelee.dating_app.auth

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.shinyelee.dating_app.R
import com.shinyelee.dating_app.utils.FirebaseRef
import java.io.ByteArrayOutputStream

class JoinActivity : AppCompatActivity() {

    private val TAG = "JoinActivity"

    // Firebase
    private lateinit var auth: FirebaseAuth

    // 별명
    private var nickname = ""
    // 성별
    private var gender = ""
    // 지역
    private var city = ""
    // 나이
    private var age = ""
    // UID
    private var uid = ""

    // 프로필 사진
    lateinit var profileImage : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // 프사
        profileImage = findViewById(R.id.imageArea)

        // (프사의 기본 이미지를 선택한 이미지로 변경)
        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri ->
                profileImage.setImageURI(uri)
            }
        )

        // 프사 클릭하면
        profileImage.setOnClickListener {
            // getAction 실행
            getAction.launch("image/*")
        }

        // 회원가입 버튼
        val joinBtn = findViewById<Button>(R.id.joinBtn)

        // 을 클릭하면
        joinBtn.setOnClickListener {

            // 메일주소, 비밀번호 받아옴
            val email = findViewById<TextInputEditText>(R.id.emailArea)
            val pw = findViewById<TextInputEditText>(R.id.pwArea)

            // 별명
            nickname = findViewById<TextInputEditText>(R.id.nicknameArea).text.toString()
            // 성별
            gender = findViewById<TextInputEditText>(R.id.genderArea).text.toString()
            // 지역
            city = findViewById<TextInputEditText>(R.id.cityArea).text.toString()
            // 나이
            age = findViewById<TextInputEditText>(R.id.ageArea).text.toString()

            // 회원가입
            auth.createUserWithEmailAndPassword(email.text.toString(), pw.text.toString())
                .addOnCompleteListener(this) { task ->

                    // 성공
                    if (task.isSuccessful) {

                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")

                        // 로그인 확인 위해 사용자 uid 받아옴
                        val user = auth.currentUser
                        uid = user?.uid.toString()

                        val userModel = UserDataModel(
                            uid,
                            nickname,
                            gender,
                            city,
                            age
                        )

                        // 사용자 정보 값 넣기
                        FirebaseRef.userInfoRef.child(uid).setValue(userModel)

                        // 프사 업로드
                        uploadImage()

                        // 메인액티비티로 이동
//                        val intent = Intent(this, MainActivity::class.java)
//                        startActivity(intent)

                    // 실패
                    } else {

                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)

                    }

                }

        }

    }

    // 프사 업로드
    private fun uploadImage() {

        val storage = Firebase.storage
        val storageRef = storage.reference.child("image.png")

        // Get the data from an ImageView as bytes
        profileImage.isDrawingCacheEnabled = true
        profileImage.buildDrawingCache()
        val bitmap = (profileImage.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {
            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            // ...
        }

    }
}