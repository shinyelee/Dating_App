package com.shinyelee.dating_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.shinyelee.dating_app.auth.UserDataModel
import com.shinyelee.dating_app.setting.SettingActivity
import com.shinyelee.dating_app.slider.CardStackAdapter
import com.shinyelee.dating_app.utils.FirebaseAuthUtils
import com.shinyelee.dating_app.utils.FirebaseRef
import com.shinyelee.dating_app.utils.MyInfo
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
    // 현재 사용자의 성별
    private lateinit var currentUserGender: String
    // UID
    private val uid = FirebaseAuthUtils.getUid()

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
//                    Toast.makeText(this@MainActivity, "관심없음", Toast.LENGTH_SHORT).show()
                }
                // 오른쪽(좋아요)
                if(direction == Direction.Right) {
                    Toast.makeText(this@MainActivity, "좋아요", Toast.LENGTH_SHORT).show()
                    // 다른 사용자 UID 받아옴
                    userLikeOther(uid, usersDataList[userCount].uid.toString())
                }
                // 넘긴 프로필의 수를 셈
                userCount += 1
                // 프로필 전부 다 봤을 때 자동으로 새로고침
                if(userCount == usersDataList.count()) {
                    getUserDataList(currentUserGender)
                    Toast.makeText(this@MainActivity, "모든 프로필을 확인했습니다", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCardRewound() {}
            override fun onCardCanceled() {}
            override fun onCardAppeared(view: View?, position: Int) {}
            override fun onCardDisappeared(view: View?, position: Int) {}
        })

        // 카드스택어댑터에 데이터 넘겨주기
        cardStackAdapter = CardStackAdapter(baseContext, usersDataList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter

        getMyUserData()

    }

    // 현재 사용자 정보
    private fun getMyUserData() {
        val postListener = object : ValueEventListener {
            // 데이터스냅샷 내 사용자 데이터 출력
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 프사 제외한 나머지 정보
                val data = dataSnapshot.getValue(UserDataModel::class.java)
                // 현재 사용자의 성별
                currentUserGender = data?.gender.toString()
                // 현재 사용자의 닉네임
                MyInfo.myNickname = data?.nickname.toString()
                getUserDataList(currentUserGender)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 실패
                Log.w(TAG, "getMyUserData - loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)
    }

    // 전체 사용자 정보
    private fun getUserDataList(currentUserGender : String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // 데이터스냅샷 내 사용자 데이터 출력
                for(dataModel in dataSnapshot.children) {
                    val user = dataModel.getValue(UserDataModel::class.java)
                    // 성별 필터링
                    if(user!!.gender.toString().equals(currentUserGender)) {
                        // 현재 사용자와 같은 성별의 사용자는 패스
                    } else {
                        // 다른 성별의 사용자 정보만 불러옴
                        usersDataList.add(user!!)
                    }
                }
                cardStackAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 실패
                Log.w(TAG, "getUserDataList - loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userInfoRef.addValueEventListener(postListener)
    }

    // 현재 사용자의 좋아요 정보
    private fun userLikeOther(myUid : String, otherUid : String) {
        FirebaseRef.userLikeRef.child(myUid).child(otherUid).setValue("true")
        getMyLikeList(otherUid)
    }
    // 대충 이런 구조임
    // DB
    // └─userLike
    //   └─현재 사용자의 UID
    //     └─현재 사용자가 좋아요 한 사용자의 UID : "true"

    // 현재 사용자의 좋아요 리스트
    private fun getMyLikeList(otherUid: String) {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // "모든" 사용자의 좋아요 리스트 (x)
                // "현재 사용자가 좋아하는" 사용자의 좋아요 리스트 (O)
                for(dataModel in dataSnapshot.children) {
                    val likeUserKey = dataModel.key.toString()
                    // 상대방도 현재 사용자를 좋아요 했는지 확인
                    if(likeUserKey.equals(uid)) {
//                        Toast.makeText(this@MainActivity, "매칭 성공", Toast.LENGTH_SHORT).show()
                        createNotificationChannel()
                        sendNotification()
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 실패
                Log.w(TAG, "getMyLikeList - loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userLikeRef.child(otherUid).addValueEventListener(postListener)
    }

    // 알림 채널 시스템에 등록
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "name"
            val descriptionText = "descriptionText"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // 푸시 알림
    private fun sendNotification() {
        var builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.logo)
            .setContentTitle("매칭 완료")
            .setContentText("상대방도 나에게 호감이 있어요! 메시지를 보내볼까요?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(1234, builder.build())
        }
    }

}