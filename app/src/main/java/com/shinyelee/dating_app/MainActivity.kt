package com.shinyelee.dating_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.shinyelee.dating_app.auth.IntroActivity
import com.shinyelee.dating_app.auth.UserDataModel
import com.shinyelee.dating_app.slider.CardStackAdapter
import com.shinyelee.dating_app.utils.FirebaseRef
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction
import java.lang.StringBuilder

class MainActivity : AppCompatActivity() {

    // 효율적으로 데이터와 뷰를 관리하기 위해 어댑터가 필요함
    lateinit var cardStackAdapter: CardStackAdapter
    // 레이아웃 매니저로 뷰를 그려줌
    lateinit var manager: CardStackLayoutManager

    private val TAG: String = "MainActivity"

    // 사용자 데이터 리스트
    private val usersDataList = mutableListOf<UserDataModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 로그아웃 아이콘
        val logout = findViewById<ImageView>(R.id.logoutIcon)

        // 을 클릭하면
        logout.setOnClickListener {

            // 로그아웃 후
            val auth = Firebase.auth
            auth.signOut()

            // 인트로액티비티로 이동
            val intent = Intent(this, IntroActivity::class.java)
            startActivity(intent)

        }

        val cardStackView = findViewById<CardStackView>(R.id.cardStackView)

        manager = CardStackLayoutManager(baseContext, object: CardStackListener {


            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            override fun onCardSwiped(direction: Direction?) {
            }

            override fun onCardRewound() {
            }

            override fun onCardCanceled() {
            }

            override fun onCardAppeared(view: View?, position: Int) {
            }

            override fun onCardDisappeared(view: View?, position: Int) {
            }

        })

        // 카드스택어댑터에 데이터 넘겨주기
        cardStackAdapter = CardStackAdapter(baseContext, usersDataList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter

        getUserDataList()

    }

    private fun getUserDataList() {

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 데이터스냅샷 내 사용자 데이터 출력
                for(dataModel in dataSnapshot.children) {
                    Log.d(TAG, dataModel.toString())

                    val user = dataModel.getValue(UserDataModel::class.java)
                    usersDataList.add(user!!)

                }

                cardStackAdapter.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }

        }

        FirebaseRef.userInfoRef.addValueEventListener(postListener)

    }

}