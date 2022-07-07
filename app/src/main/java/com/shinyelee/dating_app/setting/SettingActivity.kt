package com.shinyelee.dating_app.setting

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.shinyelee.dating_app.auth.IntroActivity
import com.shinyelee.dating_app.databinding.ActivitySettingBinding
import com.shinyelee.dating_app.message.MyLikeActivity
import com.shinyelee.dating_app.message.MyMsgActivity

class SettingActivity : AppCompatActivity() {

    private val TAG = "SettingActivity"

    // 뷰바인딩
    private var vBinding : ActivitySettingBinding? = null
    private val binding get() = vBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        // 뷰바인딩
        vBinding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onDestroy() {
        vBinding = null
        super.onDestroy()
    }

}