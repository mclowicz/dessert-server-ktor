package com.example.models

data class User(
    override val id: String,
    val email: String,
    val hashPass: ByteArray
): Model

data class UserInput(
    val email: String,
    val password: String
)

data class UserResponse(
    val token: String,
    val user: User
)