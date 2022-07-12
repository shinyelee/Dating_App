package com.shinyelee.dating_app

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.shinyelee.dating_app.auth.UserDataModel
import com.shinyelee.dating_app.databinding.ActivityMainBinding
import com.shinyelee.dating_app.message.MyLikeActivity
import com.shinyelee.dating_app.setting.MyPageActivity
import com.shinyelee.dating_app.slider.CardStackAdapter
import com.shinyelee.dating_app.utils.FirebaseAuthUtils
import com.shinyelee.dating_app.utils.FirebaseRef
import com.shinyelee.dating_app.utils.MyInfo
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.Direction

class MainActivity : AppCompatActivity() {

    // 카드스택뷰
    lateinit var cardStackAdapter: CardStackAdapter
    lateinit var manager: CardStackLayoutManager

    private val TAG: String = "MainActivity"

    // 뷰바인딩
    private var vBinding : ActivityMainBinding? = null
    private val binding get() = vBinding!!

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
        // 뷰바인딩
        vBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 커스텀한 toolbar를 액션바로 사용
        setSupportActionBar(binding.toolbar)
        // 액션바에 제목의 표시유무 설정
        // (false -> 커스텀한 이름이 나옴)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.title = "프로필 탐색하기"

        // 카드스택뷰
        manager = CardStackLayoutManager(baseContext, object: CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {
            }
            // 카드 넘기기
            override fun onCardSwiped(direction: Direction?) {
                // 왼쪽(관심없음)
                if(direction == Direction.Left) {}
                // 오른쪽(좋아요)
                if(direction == Direction.Right) {
                    // 하트 애니메이션
                    binding.ltAnimation.playAnimation()
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

        // 카드스택어댑터에 데이터 넘김
        cardStackAdapter = CardStackAdapter(baseContext, usersDataList)
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = cardStackAdapter

        getMyUserData()

    }

    // 액션버튼 메뉴 액션바에 집어 넣음
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    // 액션버튼 클릭 했을 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item?.itemId){
            // 하트 버튼 -> 마이라이크액티비티
            R.id.likeBtn -> {
                val intent = Intent(this, MyLikeActivity::class.java)
                startActivity(intent)
                return super.onOptionsItemSelected(item)

            }
            // 설정 버튼 -> 마이페이지액티비티
            R.id.settingBtn -> {
                val intent = Intent(this, MyPageActivity::class.java)
                startActivity(intent)
                return super.onOptionsItemSelected(item)
            }
            else -> return super.onOptionsItemSelected(item)
        }
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

    // 푸시 알림 - 매칭
    private fun sendNotification() {

        var builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_baseline_local_fire_department_24)
            .setContentTitle("매칭 완료")
            .setContentText("상대방도 나에게 호감이 있어요! 메시지를 보내볼까요?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
        with(NotificationManagerCompat.from(this)) {
            notify(123, builder.build())
        }

    }

    override fun onDestroy() {
        vBinding = null
        super.onDestroy()
    }

}