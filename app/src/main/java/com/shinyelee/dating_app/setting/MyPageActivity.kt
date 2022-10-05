package com.shinyelee.dating_app.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.shinyelee.dating_app.auth.IntroActivity
import com.shinyelee.dating_app.auth.UserDataModel
import com.shinyelee.dating_app.databinding.ActivityMyPageBinding
import com.shinyelee.dating_app.utils.FirebaseAuthUtils
import com.shinyelee.dating_app.utils.FirebaseRef

class MyPageActivity : AppCompatActivity() {

    // 태그
    private val TAG = "MyPageActivity"

    // 파이어베이스 인스턴스 선언
    private var vBinding : ActivityMyPageBinding? = null

    // (전역변수) 바인딩 객체 선언
    private val binding get() = vBinding!!

    // 파이어베이스 내 UID 정보 받아와야 함
    private val uid = FirebaseAuthUtils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // 자동 생성된 뷰바인딩 클래스에서의 inflate 메서드 활용
        // -> 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        vBinding = ActivityMyPageBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부 최상위에 있는 뷰의 인스턴스 활용
        // -> 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

        // 내 정보 불러오기
        getMyData()

        // 로그아웃 버튼
        binding.logoutBtn.setOnClickListener {

            // 로그아웃 실행
            Firebase.auth.signOut()

            // 로그아웃 확인 메시지지
            Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show()

            // '새 작업(task) 시작' 또는 '시작하려는 액티비티보다 상위에 존재하는 액티비티 삭제'
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)

            // 명시적 인텐트 -> 다른 액티비티 호출
            val intent = Intent(this, IntroActivity::class.java)

            // 인트로 액티비티 시작
            startActivity(intent)

            // 프로필 액티비티 종료
            finish()

        }

        // 뒤로가기 버튼 -> 프로필 액티비티 종료
        binding.backBtn.setOnClickListener { finish() }

    }

    // 파이어베이스에서 현재 사용자의 정보 불러오기
    private fun getMyData() {

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터스냅샷 내 사용자 데이터 출력
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 프사 제외한 나머지 정보
                Log.d(TAG, dataSnapshot.toString())
                val data = dataSnapshot.getValue(UserDataModel::class.java)

                // UID
                binding.myUid.text = data!!.uid

                // 별명
                binding.myNickname.text = data.nickname

                // 성별
                binding.myGender.text = data.gender

                // 성별 표시
                if(binding.myGender.text.equals("W")) {

                    // W로 입력 -> 여자
                    binding.myGender.text = "여자"

                } else {
                    // 그 외(M) -> 남자
                    binding.myGender.text = "남자"
                }

                // 지역
                binding.myCity.text = data.city

                // 생년
                binding.myAge.text = data.age

                // 프사
                val storageRef = Firebase.storage.reference.child(data.uid + ".png")

                // 프사 다운로드
                storageRef.downloadUrl.addOnCompleteListener( OnCompleteListener { task ->

                    // 수행
                    if(task.isSuccessful) {

                        // 글라이드로 불러옴
                        Glide.with(baseContext)
                            .load(task.result)
                            .into(binding.myImage)

                    }

                })

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "getMyData - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)

    }

    // 액티비티 파괴시
    override fun onDestroy() {

        // 바인딩 클래스 인스턴스 참조를 정리 -> 메모리 효율이 좋아짐
        vBinding = null
        super.onDestroy()

    }

}