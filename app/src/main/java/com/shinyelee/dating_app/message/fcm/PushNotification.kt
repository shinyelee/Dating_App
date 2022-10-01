package com.shinyelee.dating_app.message.fcm

// 푸시 알림
class PushNotification (

    // 알림 메시지의 제목 및 내용
    val data : NotiModel,

    // 메시지를 받는 사용자
    val to : String

)
