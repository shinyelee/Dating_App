package com.shinyelee.dating_app.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.shinyelee.dating_app.R
import com.shinyelee.dating_app.auth.UserDataModel
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

    private val TAG = "MyLikeActivity"

    private val uid = FirebaseAuthUtils.getUid()

    // 현재 사용자가 좋아하는 사용자 UID
    private val myLikeListUid = mutableListOf<String>()
    // 현재 사용자가 좋아하는 사용자 정보
    private val myLikeList = mutableListOf<UserDataModel>()
    // 현재 사용자가 좋아하는 사용자 리스트
    lateinit var listviewAdapter: ListViewAdapter
    // 현재 사용자가 보낸 메시지를 받는 사용자
    lateinit var getterUid : String
    // 메시지를 받는 사용자의 토큰 값
    lateinit var getterToken : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_like)

        // 어댑터
        val myLikeListView = findViewById<ListView>(R.id.myLikeListView)
        listviewAdapter = ListViewAdapter(this, myLikeList)
        myLikeListView.adapter = listviewAdapter

        // 현재 사용자가 좋아하는 사용자
        getMyLikeList()

        myLikeListView.setOnItemLongClickListener { parent, view, position, id ->
//            Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
            checkMatching(myLikeList[position].uid.toString())
            getterUid = myLikeList[position].uid.toString()
            getterToken = myLikeList[position].token.toString()
            return@setOnItemLongClickListener(true)
        }

    }

    // 현재 사용자와 상대방이 서로 좋아요 했는지 체크
    private fun checkMatching(otherUid : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                Log.d(TAG, otherUid)
                Log.e(TAG, dataSnapshot.toString())
                if(dataSnapshot.children.count() == 0) {
                    Toast.makeText(this@MyLikeActivity, "상대방의 좋아요 목록이 비어있습니다", Toast.LENGTH_LONG).show()
                } else {
                    for (dataModel in dataSnapshot.children) {
                        val likeUserKey = dataModel.key.toString()
                        if(likeUserKey == uid) {
                            Toast.makeText(this@MyLikeActivity, "매칭이 되었습니다", Toast.LENGTH_LONG).show()
                            showDialog()
                        } else {
//                            Toast.makeText(this@MyLikeActivity, "매칭 실패", Toast.LENGTH_LONG).show()
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

    // push messaging
    private fun testPush(notification : PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        RetrofitInstance.api.postNotification(notification)
    }

    // dialog
    private fun showDialog() {
        val mDialogView = LayoutInflater.from(this).inflate(R.layout.custom_dialog, null)
        val mBuilder = AlertDialog.Builder(this)
            .setView(mDialogView)
            .setTitle("메시지 보내기")
        val mAlertDialog = mBuilder.show()
        val send = mAlertDialog.findViewById<Button>(R.id.sendBtnArea)
        val textArea = mAlertDialog.findViewById<EditText>(R.id.sendTextArea)
        send?.setOnClickListener {
            val msgText = textArea!!.text.toString()
            val msgModel = MsgModel(MyInfo.myNickname, msgText)
            FirebaseRef.userMsgRef.child(getterUid).push().setValue(msgModel)
            val notiModel = NotiModel(MyInfo.myNickname, msgText)
            val pushModel = PushNotification(notiModel, getterToken)
            testPush(pushModel)
            mAlertDialog.dismiss()
        }
    }

}