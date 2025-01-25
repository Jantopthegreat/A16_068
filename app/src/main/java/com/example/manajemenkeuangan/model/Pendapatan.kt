package com.example.manajemenkeuangan.model

import kotlinx.serialization.Serializable


@Serializable
data class PendapatanResponse(
    val status : Boolean,
    val data: List<Pendapatan>
)

@Serializable
data class PendapatanDetailResponse(
    val status: Boolean,
    val data: Pendapatan
)

@Serializable
data class Pendapatan (
    val idPendapatan: Int,
    val tglPendapatan: String,
    val totalPendapatan: Int,
    val catatanPendapatan: String,
    val asetPendapatan: String,
    val kategoriPendapatan: String

)