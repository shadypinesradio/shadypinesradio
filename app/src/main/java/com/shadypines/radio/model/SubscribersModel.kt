package com.shadypines.radio.model

data class SubscribersListModel(
    val success: Boolean,
    val message: String,
    val data: List<Subscriber>
)

data class Subscriber(
    val id: Int,
    val user: User,
    val isSubscribe: Boolean,
    val userId: Int,
    val schId: Int
)

data class User(
    val id: Int,
    val name: String,
    val username: String?,
    val create_at: String
    // Include other fields as needed
)
