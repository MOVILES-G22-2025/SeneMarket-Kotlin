package com.example.senemarketkotlin.models

import com.google.firebase.Timestamp

data class UserModel(
    var name: String? = null,
    var email: String? = null,
    var career: String? = null,
    var semester: String? = null,
    var createdAt: Timestamp? = null,
    var categoryClicks: Map<String, Long>? = null,
    var favorites: List<String>? = null,
    var fcmToken: String? = null,
    var notificationsEnabled: Boolean? = false
) {
    constructor() : this(null, null, null, null, null, null, null, null, false)
}

