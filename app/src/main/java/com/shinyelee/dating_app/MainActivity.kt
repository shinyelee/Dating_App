package com.shinyelee.dating_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.shinyelee.dating_app.auth.UserDataModel
import com.shinyelee.dating_app.setting.SettingActivity
import com.shinyelee.dating_app.slider.CardStackAdapter
import com.shinyelee.dating_app.utils.FirebaseRef
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    // 효율적으로 데이터와 뷰를 관리하기 위해 어댑터가 필요함
    lateinit var cardStackAdapter: CardStackAdapter

    // 레이아웃 매니저로 뷰를 그려줌
    lateinit var manager: CardStackLayoutManager

    private val TAG: String = "MainActivity"

    // 사용자 데이터 리스트
    private val usersDataList = mutableListOf<UserDataModel>()

    // 사용자 수 세기
    private var userCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 설정 아이콘
        val setting = findViewById<ImageView>(R.id.settingIcon)

        // 을 클릭하면
        setting.setOnClickListener {

            // 세팅액티비티로 이동
            val intent = Intent(this, SettingActivity::class.java)
            startActivity(intent)

        }

        val cardStackView = findViewById<CardStackView>(R.id.cardStackView)

        manager = CardStackLayoutManager(baseContext, object: CardStackListener {

            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }

            // 카드 넘기기
            override fun onCardSwiped(direction: Direction?) {

                // 왼쪽(관심없음)
                if(direction == Direction.Left) {
                    Toast.makeText(this@MainActivity, "관심없음", Toast.LENGTH_SHORT).show()
                }

                // 오른쪽(좋아요)
                if(direction == Direction.Right) {
                    Toast.makeText(this@MainActivity, "좋아요", Toast.LENGTH_SHORT).show()
                }

                // 넘긴 프로필의 수를 셈
                userCount += 1

                // 프로필 전부 다 봤을 때
                if(userCount == usersDataList.count()) {
                    getUserDataList()
                    Toast.makeText(this@MainActivity, "모든 프로필을 확인했습니다", Toast.LENGTH_SHORT).show()
                }

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