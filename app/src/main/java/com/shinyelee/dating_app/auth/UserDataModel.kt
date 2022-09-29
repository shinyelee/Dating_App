package com.shinyelee.dating_app.auth

// 사용자에 대한 정보를 데이터 모델 형태로 묶음
data class UserDataModel (

    // UID
    val uid : String? = null,

    // 별명
    val nickname : String? = null,

    // 성별
    val gender : String? = null,

    // 지역
    val city : String? = null,

    // 생년
    val age : String? = null,

    // 토큰
    val token : String? = null

)