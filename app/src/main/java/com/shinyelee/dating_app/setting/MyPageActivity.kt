package com.shinyelee.dating_app.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.shinyelee.dating_app.R
import com.shinyelee.dating_app.auth.UserDataModel
import com.shinyelee.dating_app.utils.FirebaseAuthUtils
import com.shinyelee.dating_app.utils.FirebaseRef

class MyPageActivity : AppCompatActivity() {

    private val TAG = "MyPageActivity"

    // 파이어베이스 내 Uid 정보 받아와야 함
    private val uid = FirebaseAuthUtils.getUid()

   override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_page)

        // 내 정보 불러오기
       getMyData()

    }

    // 파이어베이스에서 현재 사용자의 정보 불러오기
    private fun getMyData() {

        val postListener = object : ValueEventListener {

            // 데이터스냅샷 내 사용자 데이터 출력
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, dataSnapshot.toString())
                val data = dataSnapshot.getValue(UserDataModel::class.java)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }

        }

        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)

    }
}