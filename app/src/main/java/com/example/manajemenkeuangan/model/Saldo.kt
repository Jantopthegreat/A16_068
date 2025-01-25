package com.example.manajemenkeuangan.model

import kotlinx.serialization.Serializable


@Serializable
data class SaldoResponse(
    val status : Boolean,
    val message : String,
    val data: Int
)



