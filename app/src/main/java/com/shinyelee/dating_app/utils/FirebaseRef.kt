package com.shinyelee.dating_app.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseRef {

    companion object {

        // 파이어베이스 내
        private val database = Firebase.database

        // 사용자 정보
        val userInfoRef = database.getReference("userInfo")

        // 좋아요 정보
        val userLikeRef = database.getReference("userLike")

        // 메시지 정보
        val userMsgRef = database.getReference("userMsg")

    }

}