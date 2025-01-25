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
