package com.escom.examenfirebase.model

data class NotificationItem(
    var id: String = "",
    var title: String = "",
    var body: String = "",
    var senderUid: String = "",
    var senderName: String = "",
    var recipientUid: String = "",
    var timestamp: Long = System.currentTimeMillis()
)
