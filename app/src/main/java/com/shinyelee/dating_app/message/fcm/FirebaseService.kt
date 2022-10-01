package com.shinyelee.dating_app.message.fcm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.shinyelee.dating_app.R

class FirebaseService : FirebaseMessagingService() {

    // 태그
//    private val TAG = "FirebaseService"

    // 새 토큰
    override fun onNewToken(token: String) { super.onNewToken(token) }

    // 메시지 받기
    override fun onMessageReceived(message: RemoteMessage) {

        super.onMessageReceived(message)

        val title = message.data["title"].toString()
        val body = message.data["content"].toString()

        // 알림 채널 시스템에 등록
        createNotificationChannel()

        // 알림 보내기
        sendNotification(title, body)

    }

    // 알림 채널 시스템에 등록
    private fun createNotificationChannel() {

        // API 26 이상에서만 NotificationChannel을 생성(API 25 이하는 지원하지 않음)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val name = "name"
            val descriptionText = "description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("CHANNEL_ID", name, importance).apply { description = descriptionText }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            notificationManager.createNotificationChannel(channel)

        }

    }

    // 푸시 알림(메시지)
    private fun sendNotification(title : String, body : String) {

        var builder = NotificationCompat.Builder(this, "CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_baseline_local_fire_department_24)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) { notify(123, builder.build()) }

    }

}