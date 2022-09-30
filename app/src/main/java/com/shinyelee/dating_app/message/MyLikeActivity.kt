package com.shinyelee.dating_app.message

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.shinyelee.dating_app.R
import com.shinyelee.dating_app.auth.UserDataModel
import com.shinyelee.dating_app.databinding.ActivityMyLikeBinding
import com.shinyelee.dating_app.message.fcm.NotiModel
import com.shinyelee.dating_app.message.fcm.PushNotification
import com.shinyelee.dating_app.message.fcm.RetrofitInstance
import com.shinyelee.dating_app.utils.FirebaseAuthUtils
import com.shinyelee.dating_app.utils.FirebaseRef
import com.shinyelee.dating_app.utils.MyInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyLikeActivity : AppCompatActivity() {

    // 태그
    private val TAG = "MyLikeActivity"

    // (전역변수) 바인딩 객체 선언
    private var vBinding : ActivityMyLikeBinding? = null

    // 매번 null 확인 귀찮음 -> 바인딩 변수 재선언
    private val binding get() = vBinding!!

    // UID 가져오기
    val uid = FirebaseAuthUtils.getUid()

    // 현재 사용자가 좋아하는 사용자 UID
    private val myLikeListUid = mutableListOf<String>()

    // 현재 사용자가 좋아하는 사용자 정보
    private val myLikeList = mutableListOf<UserDataModel>()

    // 현재 사용자가 좋아하는 사용자 리스트
    lateinit var listviewAdapter: ListViewAdapter

    // 현재 사용자가 보낸 메시지를 받는 사용자
    private lateinit var getterUid : String

    // 메시지를 받는 사용자의 토큰 값
    private lateinit var getterToken : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // 자동 생성된 뷰바인딩 클래스에서의 inflate 메서드 활용
        // -> 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        vBinding = ActivityMyLikeBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부 최상위에 있는 뷰의 인스턴스 활용
        // -> 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

        // 어댑터 연결
        listviewAdapter = ListViewAdapter(this, myLikeList)
        binding.myLikeListView.adapter = listviewAdapter

        // 현재 사용자가 좋아하는 사용자
        getMyLikeList()

        // 좋아요 목록 길게 클릭하면
        binding.myLikeListView.setOnItemLongClickListener { parent, view, position, id ->

            // 매칭된 상태인 경우 메시지 보낼 수 있음
            checkMatching(myLikeList[position].uid.toString())
            getterUid = myLikeList[position].uid.toString()
            getterToken = myLikeList[position].token.toString()

            return@setOnItemLongClickListener(true)

        }

        // 받은 메시지 버튼 -> 마이메시지액티비티
        binding.myMsgBtn.setOnClickListener {

            // 명시적 인텐트 -> 다른 액티비티 호출
            val intent = Intent(this, MyMsgActivity::class.java)

            // 내 메시지 액티비티 시작
            startActivity(intent)

        }

    }


    // 현재 사용자와 상대방이 서로 좋아요 했는지 확인
    private fun checkMatching(otherUid : String){

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터 스냅샷
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 다른 사용자 정보 로그로 출력
                Log.d(TAG, otherUid)
                Log.e(TAG, dataSnapshot.toString())

                // 서로 좋아요 한 상태 아니면 메시지 못 보냄
                if(dataSnapshot.children.count() == 0) { Toast.makeText(this@MyLikeActivity, "메시지를 보낼 수 없습니다", Toast.LENGTH_LONG).show() }

                // 서로 좋아요 된 상태면 메시지 보냄
                else {

                    // 데이터스냅샷 내 사용자 데이터 출력
                    for (dataModel in dataSnapshot.children) {

                        // 다른 사용자가 좋아요 한 사용자 목록에
                        val likeUserKey = dataModel.key.toString()

                        // 현재 사용자가 포함돼 있으면
                        if(likeUserKey == uid) {

                            // 메시지 입력창 띄움
                            showDialog()

                        }

                    }

                }

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "checkMatching - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userLikeRef.child(otherUid).addValueEventListener(postListener)

    }

    // 현재 사용자의 좋아요 리스트
    private fun getMyLikeList() {

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터 스냅샷
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 데이터스냅샷 내 사용자 데이터 출력 -> 현재 사용자가 좋아하는 사용자들의 UID를 myLikeList에 넣음
                for(dataModel in dataSnapshot.children) { myLikeListUid.add(dataModel.key.toString()) }

                // 전체 사용자 정보 받아옴
                getUserDataList()

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "getMyLikeList - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userLikeRef.child(uid).addValueEventListener(postListener)

    }

    // 전체 사용자 정보
    private fun getUserDataList() {

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터 스냅샷
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 데이터스냅샷 내 사용자 데이터 출력
                for(dataModel in dataSnapshot.children) {

                    // 다른 사용자 정보 받아옴
                    val user = dataModel.getValue(UserDataModel::class.java)

                    // 전체 사용자 중
                    if(myLikeListUid.contains(user?.uid)) {

                        // 현재 사용자가 좋아하는 사용자의 정보만 추가
                        myLikeList.add(user!!)

                    }

                }

                // 동기화(새로고침) -> 리스트 크기 및 아이템 변화를 어댑터에 알림
                listviewAdapter.notifyDataSetChanged()
                Log.d(TAG, myLikeList.toString())

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "getUserDataList - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userInfoRef.addValueEventListener(postListener)

    }

    // 푸시 메시지
    private fun testPush(notification : PushNotification) = CoroutineScope(Dispatchers.IO).launch {

        // 레트로핏 API 이용
        RetrofitInstance.api.postNotification(notification)

    }

    // 메시지 보내는 대화창
    private fun showDialog() {

        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("메시지 보내기")
        val mAlertDialog = mBuilder.show()

        val btn = mAlertDialog.findViewById<Button>(R.id.sendBtn)
        val text = mAlertDialog.findViewById<EditText>(R.id.sendText)

        // 메시지 보내기 버튼을 클릭하면 푸시 메시지 발송
        btn?.setOnClickListener {

            val msgText = text!!.text.toString()
            val msgModel = MsgModel(MyInfo.myNickname, msgText)

            // 파이어베이스에 메시지 업로드
            FirebaseRef.userMsgRef.child(getterUid).push().setValue(msgModel)

            val notiModel = NotiModel(MyInfo.myNickname, msgText)
            val pushModel = PushNotification(notiModel, getterToken)

            // 푸시 메시지
            testPush(pushModel)

            // 대화창
            mAlertDialog.dismiss()

        }

    }

    // 액티비티 파괴시
    override fun onDestroy() {

        // 바인딩 클래스 인스턴스 참조를 정리 -> 메모리 효율이 좋아짐
        vBinding = null
        super.onDestroy()

    }

}
