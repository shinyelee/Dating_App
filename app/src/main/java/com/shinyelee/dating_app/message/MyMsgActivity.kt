package com.shinyelee.dating_app.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.shinyelee.dating_app.databinding.ActivityMyMsgBinding
import com.shinyelee.dating_app.utils.FirebaseAuthUtils
import com.shinyelee.dating_app.utils.FirebaseRef

class MyMsgActivity : AppCompatActivity() {

    // 태그
    private val TAG: String = "MyMsgActivity"

    // (전역변수) 바인딩 객체 선언
    private var vBinding : ActivityMyMsgBinding? = null

    // 매번 null 확인 귀찮음 -> 바인딩 변수 재선언
    private val binding get() = vBinding!!

    // 메시지 어댑터
    lateinit var listviewAdapter : MsgAdapter

    // 현재 사용자가 받은 메시지 리스트
    val msgList = mutableListOf<MsgModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        // 자동 생성된 뷰바인딩 클래스에서의 inflate 메서드 활용
        // -> 액티비티에서 사용할 바인딩 클래스의 인스턴스 생성
        vBinding = ActivityMyMsgBinding.inflate(layoutInflater)

        // getRoot 메서드로 레이아웃 내부 최상위에 있는 뷰의 인스턴스 활용
        // -> 생성된 뷰를 액티비티에 표시
        setContentView(binding.root)

        // 어댑터 연결
        listviewAdapter = MsgAdapter(this, msgList)
        binding.msgListView.adapter = listviewAdapter

        // 내 메시지 불러오기
        getMyMsg()

        // 뒤로가기 버튼 -> 쪽지함 액티비티 종료
        binding.backBtn.setOnClickListener { finish() }

    }

    // 내 메시지 불러오기
    private fun getMyMsg() {

        // 데이터베이스에서 컨텐츠의 세부정보를 검색
        val postListener = object : ValueEventListener {

            // 데이터 스냅샷
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // 중복 출력 방지 위해 메시지 목록 비워줌
                msgList.clear()

                // 데이터스냅샷 내 사용자 데이터 출력
                for(dataModel in dataSnapshot.children) {

                    // 메시지 모델에서
                    val msg = dataModel.getValue(MsgModel::class.java)
                    msgList.add(msg!!)
                    Log.d(TAG, msg.toString())

                }

                // 역순 정렬
                msgList.reverse()

                // 동기화(새로고침) -> 리스트 크기 및 아이템 변화를 어댑터에 알림
                listviewAdapter.notifyDataSetChanged()

            }

            // 실패
            override fun onCancelled(databaseError: DatabaseError) { Log.w(TAG, "getMyMsg - loadPost:onCancelled", databaseError.toException()) }

        }

        // 파이어베이스 내 데이터의 변화(추가)를 알려줌
        FirebaseRef.userMsgRef.child(FirebaseAuthUtils.getUid()).addValueEventListener(postListener)

    }

    // 액티비티 파괴시
    override fun onDestroy() {

        // 바인딩 클래스 인스턴스 참조를 정리 -> 메모리 효율이 좋아짐
        vBinding = null
        super.onDestroy()

    }

}