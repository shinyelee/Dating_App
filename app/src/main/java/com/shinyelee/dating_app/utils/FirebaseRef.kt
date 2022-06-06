package com.shinyelee.dating_app.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef {

    companion object {

        // 파이어베이스 내
        val database = Firebase.database

        // userInfo 노드 하위의 데이터
        val userInfoRef = database.getReference("userInfo")

    }

}