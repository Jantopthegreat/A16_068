package com.example.manajemenkeuangan.model

import kotlinx.serialization.Serializable



@Serializable
data class Pengeluaran (
    val idPengeluaran: Int,
    val tglPengeluaran: String,
    val totalPengeluaran: Int,
    val catatanPengeluaran: String,
    val asetPengeluaran: String,
    val kategoriPengeluaran: String
    )