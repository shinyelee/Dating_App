package com.shinyelee.dating_app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shinyelee.dating_app.auth.IntroActivity
import com.shinyelee.dating_app.slider.CardStackAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 세팅 버튼
        val setting = findViewById<ImageView>(R.id.settingIcon)

        // 을 클릭하면
        setting.setOnClickListener {

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
        // 테스트
        val testList = mutableListOf<String>()
        testList.add("a")
        testList.add("b")
        testList.add("c")

        cardStackAdapter = CardStackAdapter(baseContext, testList)
        cardStackView.layoutManager = manager
        cardStackView.adapter = cardStackAdapter
    }
}