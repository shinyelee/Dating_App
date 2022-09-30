package com.shinyelee.dating_app.utils

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthUtils {

    companion object {

        // 파이어베이스 인스턴스 선언
        private lateinit var auth : FirebaseAuth

        // UID 받아옴
        fun getUid() : String {

            // 파이어베이스에서
            auth = FirebaseAuth.getInstance()

            // 문자열로 변환
            return auth.currentUser?.uid.toString()

        }

    }

}