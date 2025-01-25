package com.example.manajemenkeuangan.model

import kotlinx.serialization.Serializable


@Serializable
data class KategoriResponse(
    val status : Boolean,
    val data: List<Kategori>
)

@Serializable
data class KategoriDetailResponse(
    val status: Boolean,
    val data: Kategori
)

@Serializable
data class Kategori (
    val idKategori: Int,
    val namaKategori: String,
)