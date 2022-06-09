package com.shinyelee.dating_app.message

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

class MyLikeActivity : AppCompatActivity() {

    private val TAG = "MyLikeActivity"
    private val uid = FirebaseAuthUtils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like)

        // 전체 사용자 정보 받아옴
        getUserDataList()
        // 현재 사용자의 좋아요 리스트
        getMyLikeList()

    }

    // 현재 사용자의 좋아요 리스트
    private fun getMyLikeList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children) {
                    Log.d(TAG, dataModel.key.toString())
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 실패
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userLikeRef.child(uid).addValueEventListener(postListener)
    }

    // 전체 사용자 정보
    private fun getUserDataList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 데이터스냅샷 내 사용자 데이터 출력
                for(dataModel in dataSnapshot.children) {
                    val user = dataModel.getValue(UserDataModel::class.java)
                    Log.d(TAG, "point : " + user.toString())
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 실패
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.addValueEventListener(postListener)
    }

}