package com.escom.examenfirebase.model

data class User(
    var uid: String = "",
    var name: String = "",
    var email: String = "",
    var phone: String = "",
    var role: String = "user", // "user" o "admin"
    var fcmToken: String = "",
    var createdAt: Long = System.currentTimeMillis()
)
