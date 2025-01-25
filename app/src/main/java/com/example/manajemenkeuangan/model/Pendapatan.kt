package com.example.manajemenkeuangan.model

import kotlinx.serialization.Serializable




@Serializable
data class Pendapatan (
    val idPendapatan: Int,
    val tglPendapatan: String,
    val totalPendapatan: Int,
    val catatanPendapatan: String,
    val asetPendapatan: String,
    val kategoriPendapatan: String

)