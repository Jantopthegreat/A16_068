package com.example.manajemenkeuangan.model

import kotlinx.serialization.Serializable


@Serializable
data class PengeluaranResponse(
    val status : Boolean,
    val data: List<Pengeluaran>
)

@Serializable
data class PengeluaranDetailResponse(
    val status: Boolean,
    val data: Pengeluaran
)

@Serializable
data class Pengeluaran (
    val idPengeluaran: Int,
    val tglPengeluaran: String,
    val totalPengeluaran: Int,
    val catatanPengeluaran: String,
    val asetPengeluaran: String,
    val kategoriPengeluaran: String
    )