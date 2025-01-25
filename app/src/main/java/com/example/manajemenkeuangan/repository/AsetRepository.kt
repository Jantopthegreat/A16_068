package com.example.manajemenkeuangan.repository

import com.example.manajemenkeuangan.model.Aset
import com.example.manajemenkeuangan.model.AsetDetailResponse
import com.example.manajemenkeuangan.model.AsetResponse
import com.example.manajemenkeuangan.service.AsetService

interface AsetRepository {

    suspend fun getAllAset(): AsetResponse

    suspend fun insertAset(aset: Aset)

    suspend fun updateAset(idAset: Int, aset: Aset)

    suspend fun deleteAset(idAset: Int)

    suspend fun getAsetById(idAset: Int): AsetDetailResponse
}

class NetworkAsetRepository(
    private val asetApiService: AsetService
) : AsetRepository {

    override suspend fun insertAset(aset: Aset) {
        asetApiService.insertAset(aset)
    }

    override suspend fun updateAset(idAset: Int, aset: Aset) {
        asetApiService.updateAset(idAset, aset)
    }

    override suspend fun deleteAset(idAset: Int) {
        try {
            val response = asetApiService.deleteAset(idAset)
            if (!response.isSuccessful) {
                throw Exception("Gagal menghapus aset, HTTP Status Code: " + "${response.code()}")
            } else {
                response.message()
                println(response.message())
            }
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getAllAset(): AsetResponse {
        return asetApiService.getAllAset()
    }

    override suspend fun getAsetById(idAset: Int): AsetDetailResponse {
        return asetApiService.getAsetById(idAset)
    }
}