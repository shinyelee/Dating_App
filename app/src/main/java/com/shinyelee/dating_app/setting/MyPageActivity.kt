package com.shinyelee.dating_app.setting

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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

        // 프사, uid, 별명, 성별, 지역, 나이
        val myImage = findViewById<ImageView>(R.id.myImage)
        val myUid = findViewById<TextView>(R.id.myUid)
        val myNickname = findViewById<TextView>(R.id.myNickname)
        val myGender = findViewById<TextView>(R.id.myGender)
        val myCity = findViewById<TextView>(R.id.myCity)
        val myAge = findViewById<TextView>(R.id.myAge)

        val postListener = object : ValueEventListener {

            // 데이터스냅샷 내 사용자 데이터 출력
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 프사 제외한 나머지 정보
                Log.d(TAG, dataSnapshot.toString())
                val data = dataSnapshot.getValue(UserDataModel::class.java)

                // uid, 별명, 성별, 지역, 나이
                myUid.text = data!!.uid
                myNickname.text = data!!.nickname
                myGender.text = data!!.gender
                myCity.text = data!!.city
                myAge.text = data!!.age

                // 프사
                val storageRef = Firebase.storage.reference.child(data.uid + ".png")

                storageRef.downloadUrl.addOnCompleteListener( OnCompleteListener { task ->

                    if(task.isSuccessful) {
                        Glide.with(baseContext)
                            .load(task.result)
                            .into(myImage)
                    }

                })

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }

        }

        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)

    }
}