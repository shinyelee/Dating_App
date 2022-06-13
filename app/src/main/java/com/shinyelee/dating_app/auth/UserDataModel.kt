package com.shinyelee.dating_app.auth

data class UserDataModel (

    // UID
    val uid : String? = null,
    // 별명
    val nickname : String? = null,
    // 성별
    val gender : String? = null,
    // 지역
    val city : String? = null,
    // 나이
    val age : String? = null,
    // 토큰
    val token : String? = null

)