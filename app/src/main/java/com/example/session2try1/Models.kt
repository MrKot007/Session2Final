package com.example.session2try1

data class ModelError(
    val error: String
)
data class User(
    val id: Int,
    val firstname: String,
    val lastname: String,
    val patronymic: String,
    val email: String,
    val sex: String,
    val dateBirthDay: String
)
data class ModelIdentity(
    val token: String,
    val body: User
)
data class ModelAuth(
    val email: String,
    val password: String
)
data class ModelReg(
    val firstname: String,
    val lastname: String,
    val patronymic: String,
    val email: String,
    val password: String,
    val dateBirthDay: String,
    val sex: String
)