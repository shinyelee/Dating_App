package com.shinyelee.dating_app.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import android.widget.Toast
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

    // 현재 사용자가 좋아하는 사용자 UID
    private val myLikeListUid = mutableListOf<String>()
    // 현재 사용자가 좋아하는 사용자 정보
    private val myLikeList = mutableListOf<UserDataModel>()
    // 현재 사용자가 좋아하는 사용자 리스트
    lateinit var listviewAdapter: ListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like)

        // 어댑터
        val myLikeListView = findViewById<ListView>(R.id.myLikeListView)
        listviewAdapter = ListViewAdapter(this, myLikeList)
        myLikeListView.adapter = listviewAdapter

        // 현재 사용자가 좋아하는 사용자
        getMyLikeList()

        // 나를 좋아하는 사용자
        myLikeListView.setOnItemClickListener { parent, view, position, id ->
            checkMatching(myLikeList[position].uid.toString())
        }

    }

    // 현재 사용자와 상대방이 서로 좋아요 했는지 체크
    private fun checkMatching(otherUid : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if(dataSnapshot.children.count() == 0) {
                    Toast.makeText(this@MyLikeActivity, "매칭 실패ㅠㅠ", Toast.LENGTH_SHORT).show()
                } else {
                    for(dataModel in dataSnapshot.children) {
                        val likeUserKey = dataModel.key.toString()
                        if(likeUserKey.equals(uid)) {
                            Toast.makeText(this@MyLikeActivity, "매칭 성공!", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@MyLikeActivity, "매칭 실패ㅠㅠ", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 실패
                Log.w(TAG, "checkMatching - loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userLikeRef.child(otherUid).addValueEventListener(postListener)
    }

    // 현재 사용자의 좋아요 리스트
    private fun getMyLikeList() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for(dataModel in dataSnapshot.children) {
                    // 현재 사용자가 좋아하는 사용자들의 UID가 myLikeList에 들어있음
                    myLikeListUid.add(dataModel.key.toString())
                }
                // 전체 사용자 정보 받아옴
                getUserDataList()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 실패
                Log.w(TAG, "getMyLikeList - loadPost:onCancelled", databaseError.toException())
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
                    if(myLikeListUid.contains(user?.uid)) {
                        // 현재 사용자가 좋아하는 사용자의 정보만 추가
                        myLikeList.add(user!!)
                    }
                }
                listviewAdapter.notifyDataSetChanged()
                Log.d(TAG, myLikeList.toString())
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 실패
                Log.w(TAG, "getUserDataList - loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.addValueEventListener(postListener)
    }

}