package com.example.manajemenkeuangan.repository

import com.example.manajemenkeuangan.model.Kategori
import com.example.manajemenkeuangan.model.KategoriDetailResponse
import com.example.manajemenkeuangan.model.KategoriResponse
import com.example.manajemenkeuangan.service.KategoriService

interface KategoriRepository {

    suspend fun getAllKategori(): KategoriResponse

    suspend fun insertKategori(kategori: Kategori)

    suspend fun updateKategori(idKategori: Int, kategori: Kategori)

    suspend fun deleteKategori(idKategori: Int)

    suspend fun getKategoriById(idKategori: Int): KategoriDetailResponse
}

class NetworkKategoriRepository(
    private val kategoriApiService: KategoriService
) : KategoriRepository {

    override suspend fun insertKategori(kategori: Kategori) {
        kategoriApiService.insertKategori(kategori)
    }

    override suspend fun updateKategori(idKategori: Int, kategori: Kategori) {
        kategoriApiService.updateKategori(idKategori, kategori)
    }

    override suspend fun deleteKategori(idKategori: Int) {
        try {
            val response = kategoriApiService.deleteKategori(idKategori)
            if (!response.isSuccessful) {
                throw Exception("Gagal menghapus kategori, HTTP Status Code: " + "${response.code()}")
            } else {
                response.message()
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getAllKategori(): KategoriResponse {
        return kategoriApiService.getAllKategori()
    }

    override suspend fun getKategoriById(idKategori: Int): KategoriDetailResponse {
        return kategoriApiService.getKategoriById(idKategori)
    }
}