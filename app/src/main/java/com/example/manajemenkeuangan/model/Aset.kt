package com.example.manajemenkeuangan.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AsetResponse(
    val status : Boolean,
    val data: List<Aset>
)

@Serializable
data class AsetDetailResponse(
    val status: Boolean,
    val data: Aset
)

@Serializable
data class Aset (
    val idAset: Int,
    val namaAset: String,
)