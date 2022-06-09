package com.shinyelee.dating_app.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.shinyelee.dating_app.R
import com.shinyelee.dating_app.utils.FirebaseAuthUtils
import com.shinyelee.dating_app.utils.FirebaseRef

class MyLikeActivity : AppCompatActivity() {

    private val TAG = "MyLikeActivity"

    private val uid = FirebaseAuthUtils.getUid()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like)

        // 현재 사용자의 좋아요 리스트
        getMyLikeList()

    }

    // 다른 사용자의 좋아요를 가져옴
    private fun getMyLikeList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // "모든" 사용자들의 좋아요 리스트 (x)
                // "현재 사용자가 좋아요 한" 사용자들의 좋아요 리스트 (O)
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

}