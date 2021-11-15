package com.example.kodetrainee.entity

import java.util.*


data class User(
    val id : String?,
    val avatarUrl : String?,
    val firstName : String?,
    val lastName : String?,
    val userTag : String?,
    val department : String?,
    val position : String?,
    val birthday : Date?,
    val phone: String?
)