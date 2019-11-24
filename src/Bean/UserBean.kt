package com.zlz2000.Bean

data class RegisterBean(
    val phoneNumber:String,
    val code:String,
    val workId:String,
    val password:String,
    val name:String
)

data class LoginBean(
    val type:Boolean,
    val number:String,
    val password: String
)

data class ForgetBean(
    val type: Boolean,
    val number: String,
    val code: String,
    val password: String
)

data class UserDataBean(
    val name: String,
    val phoneNumber: String,
    val workId: String,
    val password: String
)