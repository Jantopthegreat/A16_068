package com.example.manajemenkeuangan.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
data class Aset (
    val idAset: Int,
    val namaAset: String,
)