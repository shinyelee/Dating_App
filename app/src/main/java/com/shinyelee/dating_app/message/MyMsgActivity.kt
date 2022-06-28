package com.shinyelee.dating_app.message

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.shinyelee.dating_app.R
import com.shinyelee.dating_app.databinding.ActivityMyMsgBinding
import com.shinyelee.dating_app.utils.FirebaseAuthUtils
import com.shinyelee.dating_app.utils.FirebaseRef

class MyMsgActivity : AppCompatActivity() {

    private val TAG: String = "MyMsgActivity"

    // 뷰바인딩
    private var vBinding : ActivityMyMsgBinding? = null
    private val binding get() = vBinding!!

    lateinit var listviewAdapter : MsgAdapter
    val msgList = mutableListOf<MsgModel>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // 뷰바인딩
        vBinding = ActivityMyMsgBinding.inflate(layoutInflater)
        setContentView(binding.root)

        listviewAdapter = MsgAdapter(this, msgList)
        binding.msgListView.adapter = listviewAdapter

        getMyMsg()

    }

    private fun getMyMsg() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                msgList.clear()
                // 데이터스냅샷 내 사용자 데이터 출력
                for(dataModel in dataSnapshot.children) {
                    val msg = dataModel.getValue(MsgModel::class.java)
                    msgList.add(msg!!)
                    Log.d(TAG, msg.toString())
                }
                msgList.reverse()
                listviewAdapter.notifyDataSetChanged()
            }
            override fun onCancelled(databaseError: DatabaseError) {
                // 실패
                Log.w(TAG, "getMyMsg - loadPost:onCancelled", databaseError.toException())
            }
        }
        FirebaseRef.userMsgRef.child(FirebaseAuthUtils.getUid()).addValueEventListener(postListener)
    }

}