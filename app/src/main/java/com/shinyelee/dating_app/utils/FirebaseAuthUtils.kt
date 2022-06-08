package com.shinyelee.dating_app.utils

import com.google.firebase.auth.FirebaseAuth

class FirebaseAuthUtils {

    companion object {

        private lateinit var auth : FirebaseAuth

        // UID 가져옴
        fun getUid() : String {
            auth = FirebaseAuth.getInstance()
            return auth.currentUser?.uid.toString()
        }

    }
}