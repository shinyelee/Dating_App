package com.shinyelee.dating_app

import android.annotation.SuppressLint
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

    // 태그
    private val TAG: String = "MainActivity"

    // 카드스택뷰
    lateinit var cardStackAdapter: CardStackAdapter
    private lateinit var manager: CardStackLayoutManager

    // (전역변수) 바인딩 객체 선언
    private var vBinding : ActivityMainBinding? = null

    // 매번 null 확인 귀찮음 -> 바인딩 변수 재선언
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

        // 자동 생성된 뷰바인딩 클래스에서의 inflate 메서드 활용
        // -> 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        vBinding = ActivityMainBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부 최상위에 있는 뷰의 인스턴스 활용
        // -> 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

        // 커스텀한 툴바를 액션바로 사용
        setSupportActionBar(binding.toolbar)

        // 액션바에 제목의 표시유무 설정
        // (false -> 커스텀한 이름이 나옴)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        binding.toolbar.title = "프로필 탐색하기"

        // 카드스택뷰
        manager = CardStackLayoutManager(baseContext, object: CardStackListener {

            // 카드 넘기기
            override fun onCardSwiped(direction: Direction?) {

                // 왼쪽(관심없음)
//                if(direction == Direction.Left) {}

                // 오른쪽(좋아요)
                if(direction == Direction.Right) {

                    // 하트 애니메이션
                    binding.ltAnimation.playAnimation()

                    // 해당 카드(사용자) 좋아요 처리
                    userLikeOther(uid, usersDataList[userCount].uid.toString())

                }

                // 넘긴 프로필의 수를 셈
                userCount += 1

                // 프로필 전부 다 봤을 때
                if(userCount == usersDataList.count()) {

                    // 자동으로 새로고침
                    getUserDataList(currentUserGender)
                    Toast.makeText(this@MainActivity, "모든 프로필을 확인했습니다", Toast.LENGTH_SHORT).show()

                }
            }

            override fun onCardDragging(direction: Direction?, ratio: Float) {}
            override fun onCardRewound() {}
            override fun onCardCanceled() {}
            override fun onCardAppeared(view: View?, position: Int) {}
            override fun onCardDisappeared(view: View?, position: Int) {}

        })

        // 카드스택어댑터에 데이터 넘김
        cardStackAdapter = CardStackAdapter(baseContext, usersDataList)
        binding.cardStackView.layoutManager = manager
        binding.cardStackView.adapter = cardStackAdapter

        // 현재 사용자 정보
        getMyUserData()

    }

    // 액션바에
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        // 액션버튼 메뉴 집어 넣음
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true

    }

    // 액션바 내 아이템(아이콘)을 클릭할 때
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        // 해당 아이템이
        when(item.itemId){

            // 하트 버튼인 경우
            R.id.likeBtn -> {

                // -> 좋아요 액티비티 시작
                val intent = Intent(this, MyLikeActivity::class.java)
                startActivity(intent)
                return super.onOptionsItemSelected(item)

            }

            // 설정 버튼인 경우
            R.id.settingBtn -> {

                // -> 내 정보 액티비티 시작
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

                // 현재 사용자와 성별이 반대인 사용자 목록
                getUserDataList(currentUserGender)

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "getMyUserData - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userInfoRef.child(uid).addValueEventListener(postListener)

    }

    // 전체 사용자 정보
    private fun getUserDataList(currentUserGender : String) {

        val postListener = object : ValueEventListener {

            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 데이터스냅샷 내 사용자 데이터 출력
                for(dataModel in dataSnapshot.children) {

                    val user = dataModel.getValue(UserDataModel::class.java)

                    // 현재 사용자와 같은 성별인 사용자 -> 패스
                    if(user!!.gender.toString() == currentUserGender) {
                    } else {

                        // 현재 사용자와 다른 성별인 사용자만 불러옴
                        usersDataList.add(user)

                    }

                }

                // 동기화(새로고침) -> 리스트 크기 및 아이템 변화를 어댑터에 알림
                cardStackAdapter.notifyDataSetChanged()

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "getUserDataList - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userInfoRef.addValueEventListener(postListener)

    }

    // 카드 좋아요 하기
    private fun userLikeOther(myUid : String, otherUid : String) {

        // (카드 오른쪽으로 넘기면) 좋아요 값 true로 설정
        FirebaseRef.userLikeRef.child(myUid).child(otherUid).setValue("true")

        // 좋아요 목록
        getMyLikeList(otherUid)

    }
    // 대충 이런 구조임
    // DB
    // └─userLike
    //   └─현재 사용자의 UID
    //     └─현재 사용자가 좋아요 한 사용자의 UID : "true"

    // 현재 사용자의 좋아요 목록
    private fun getMyLikeList(otherUid: String) {

        val postListener = object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // "모든" 사용자의 좋아요 리스트 (x)
                // "현재 사용자가 좋아하는" 사용자의 좋아요 리스트 (O)
                for(dataModel in dataSnapshot.children) {

                    val likeUserKey = dataModel.key.toString()

                    // 상대방도 현재 사용자를 좋아하면
                    if(likeUserKey == uid) {

                        // 푸시 알림(매칭)
                        createNotificationChannel()
                        sendNotification()

                    }

                }

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "getMyLikeList - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userLikeRef.child(otherUid).addValueEventListener(postListener)

    }

    // 알림 채널 시스템에 등록
    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "name"
            val descriptionText = "descriptionText"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply { description = descriptionText }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

        }

    }

    // 푸시 알림(매칭)
    private fun sendNotification() {

        var builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_baseline_local_fire_department_24)
            .setContentTitle("매칭 완료")
            .setContentText("상대방도 나에게 호감이 있어요! 메시지를 보내볼까요?")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) { notify(1234, builder.build()) }

    }

    // 액티비티 파괴시
    override fun onDestroy() {

        // 바인딩 클래스 인스턴스 참조를 정리 -> 메모리 효율이 좋아짐
        vBinding = null
        super.onDestroy()

    }

}