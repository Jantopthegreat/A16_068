package com.example.manajemenkeuangan.model

import kotlinx.serialization.Serializable




@Serializable
data class Kategori (
    val idKategori: Int,
    val namaKategori: String,
)