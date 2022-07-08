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
import com.shinyelee.dating_app.R
import com.shinyelee.dating_app.auth.IntroActivity
import com.shinyelee.dating_app.auth.UserDataModel
import com.shinyelee.dating_app.databinding.ActivityMyPageBinding
import com.shinyelee.dating_app.utils.FirebaseAuthUtils
import com.shinyelee.dating_app.utils.FirebaseRef

class MyPageActivity : AppCompatActivity() {

    private val TAG = "MyPageActivity"

    // 뷰바인딩
    private var vBinding : ActivityMyPageBinding? = null
    private val binding get() = vBinding!!

    // 파이어베이스 내 UID 정보 받아와야 함
    private val uid = FirebaseAuthUtils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // 뷰바인딩
        vBinding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 내 정보 불러오기
        getMyData()

        // 로그아웃 버튼
        binding.logoutBtn.setOnClickListener {
            // 로그아웃 후
            val auth = Firebase.auth
            auth.signOut()
            Toast.makeText(this, "로그아웃", Toast.LENGTH_SHORT).show()
            // 인트로액티비티로 이동
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)
        }

    }

    // 파이어베이스에서 현재 사용자의 정보 불러오기
    private fun getMyData() {

        val postListener = object : ValueEventListener {

            // 데이터스냅샷 내 사용자 데이터 출력
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 프사 제외한 나머지 정보
                Log.d(TAG, dataSnapshot.toString())
                val data = dataSnapshot.getValue(UserDataModel::class.java)
                // UID
                binding.myUid.text = data!!.uid
                // 별명
                binding.myNickname.text = data!!.nickname
                // 성별
                binding.myGender.text = data!!.gender
                if(binding.myGender.text.equals("W")) {
                    binding.myGender.text = "여자"
                } else {
                    binding.myGender.text = "남자"
                }
                // 지역
                binding.myCity.text = data!!.city
                // 나이
                binding.myAge.text = data!!.age
                // 프사
                val storageRef = Firebase.storage.reference.child(data.uid + ".png")
                storageRef.downloadUrl.addOnCompleteListener( OnCompleteListener { task ->
                    if(task.isSuccessful) {
                        Glide.with(baseContext)
                            .load(task.result)
                            .into(binding.myImage)
                    }
                })
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "getMyData - loadPost:onCancelled", databaseError.toException())
            }

        }

        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)

    }

    override fun onDestroy() {
        vBinding = null
        super.onDestroy()
    }

}