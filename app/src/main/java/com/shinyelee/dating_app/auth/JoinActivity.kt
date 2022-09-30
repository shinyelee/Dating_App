package com.shinyelee.dating_app.auth

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Patterns
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

    // 태그
    private val TAG = "JoinActivity"

    // 파이어베이스 인스턴스 선언
    private lateinit var auth: FirebaseAuth

    // (전역변수) 바인딩 객체 선언
    private var vBinding : ActivityJoinBinding? = null

    // 매번 null 확인 귀찮음 -> 바인딩 변수 재선언
    private val binding get() = vBinding!!

    // UID, 별명, 성별, 지역, 생년, 프사
    private var uid = ""
    private var nickname = ""
    private var gender = ""
    private var city = ""
    private var age = ""
    private lateinit var selfie : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // 파이어베이스 인스턴스 초기화
        auth = Firebase.auth

        // 자동 생성된 뷰바인딩 클래스에서의 inflate 메서드 활용
        // -> 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        vBinding = ActivityJoinBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부 최상위에 있는 뷰의 인스턴스 활용
        // -> 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

        // 프사 아이콘
        selfie = binding.selfie

        // 이미지 데이터 전달
        val getAction = registerForActivityResult(
            ActivityResultContracts.GetContent(),
            ActivityResultCallback { uri -> selfie.setImageURI(uri) }
        )

        // 아이콘 클릭하면 프사 업로드 실행
        selfie.setOnClickListener { getAction.launch("image/*") }

        // 회원가입 버튼
        binding.joinBtn.setOnClickListener {

            // 가입조건 확인
            var emailCheck = true
            var pwCheck = true
            var nicknameCheck = true
            var genderCheck = true
            var cityCheck = true
            var ageCheck = true

            // 모든 가입조건
            var allCheck = emailCheck and pwCheck and nicknameCheck and genderCheck and cityCheck and ageCheck

            // 이메일주소, 비밀번호
            val emailTxt = binding.email.text.toString()
            val pwTxt = binding.pw.text.toString()

            // 별명, 성별, 지역, 생년
            nickname = binding.nickname.text.toString()
            gender = binding.gender.text.toString()
            city = binding.city.text.toString()
            age = binding.age.text.toString()

            // 빈 칸 검사
            if(emailTxt.isEmpty() || pwTxt.isEmpty() || nickname.isEmpty() || gender.isEmpty() || city.isEmpty() || age.isEmpty()) {
                allCheck = false
                Toast.makeText(this, "입력란을 모두 작성하세요", Toast.LENGTH_SHORT).show()
            }

            // 이메일주소 정규식
            val emailPattern = Patterns.EMAIL_ADDRESS

            // 이메일주소 검사
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
                binding.pwArea.error = "비밀번호를 입력하세요"
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

            // 별명 검사
            if(nickname.isEmpty()) {
                nicknameCheck = false
                binding.nicknameArea.error = "별명을 입력하세요"
            } else if(nickname.length>20) {
                nicknameCheck = false
                binding.nicknameArea.error = "10자 이하로 입력하세요"
            } else {
                nicknameCheck = true
                binding.nicknameArea.error = null
            }

            // 성별 검사
            if(gender.isEmpty()) {
                genderCheck = false
                binding.genderArea.error = "성별을 입력하세요"
            } else {
                genderCheck = true
                binding.genderArea.error = null
            }

            // 지역 검사
            if(city.isEmpty()) {
                cityCheck = false
                binding.cityArea.error = "지역을 입력하세요"
            } else {
                cityCheck = true
                binding.cityArea.error = null
            }

            // 생년 검사
            if(age.isEmpty()) {
                ageCheck = false
                binding.ageArea.error = "생년을 입력하세요"
            } else {
                ageCheck = true
                binding.ageArea.error = null
            }

            // 가입조건 모두 만족하면
            if(allCheck) {

                // 계정 생성
                auth.createUserWithEmailAndPassword(emailTxt, pwTxt)
                    .addOnCompleteListener(this) { task ->

                        // 회원가입 성공
                        if (task.isSuccessful) {

                            // UID 정의
                            val user = auth.currentUser
                            uid = user?.uid.toString()

                            // 토큰
                            FirebaseMessaging.getInstance().token.addOnCompleteListener(
                                OnCompleteListener { task ->

                                    // 실패시 로그
                                    if (!task.isSuccessful) {
                                        Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                                        return@OnCompleteListener
                                    }

                                    // 새 FCM 등록 토큰
                                    val token = task.result.toString()
                                    Log.e(TAG, "토큰(user token value) - $token")

                                    // 작성한 내용과 토큰값을 데이터 클래스 형태로 만들어
                                    val userModel = UserDataModel(
                                        uid,
                                        nickname,
                                        gender,
                                        city,
                                        age,
                                        token
                                    )

                                    // 파이어베이스에 회원정보 하위값으로 넣고
                                    FirebaseRef.userInfoRef.child(uid).setValue(userModel)

                                    // 프사 업로드 후
                                    uploadImage(uid)

                                    // 명시적 인텐트 -> 다른 액티비티 호출
                                    val intent = Intent(this, MainActivity::class.java)

                                    // 메인 액티비티 시작
                                    startActivity(intent)

                                    // 조인 액티비티 종료
                                    finish()

                                })

                        // 오류, 중복 계정 등 -> 회원가입 실패
                        } else { Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show() }

                    }

            // 조건 불만족하면 회원가입 실패
            } else { Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show() }

        }

    }

    // 프사 업로드
    private fun uploadImage(uid : String) {

        // 파이어베이스 스토리지(프사 저장용)
        val storage = Firebase.storage
        val storageRef = storage.reference.child("$uid.png")

        // 이미지뷰에서 비트맵으로 데이터 가져옴
        selfie.isDrawingCacheEnabled = true
        selfie.buildDrawingCache()
        val bitmap = (selfie.drawable as BitmapDrawable).bitmap
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()

        // 업로드
        var uploadTask = storageRef.putBytes(data)
        uploadTask.addOnFailureListener {}.addOnSuccessListener { taskSnapshot -> }

    }

    // 액티비티 파괴시
    override fun onDestroy() {

        // 바인딩 클래스 인스턴스 참조를 정리 -> 메모리 효율이 좋아짐
        vBinding = null
        super.onDestroy()

    }

}