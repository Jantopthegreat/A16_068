package com.example.manajemenkeuangan.repository

import com.example.manajemenkeuangan.model.Pengeluaran
import com.example.manajemenkeuangan.model.PengeluaranResponse
import com.example.manajemenkeuangan.model.PengeluaranDetailResponse
import com.example.manajemenkeuangan.service.PengeluaranService

interface PengeluaranRepository {
    suspend fun getPengeluaran(): PengeluaranResponse

    suspend fun getPengeluaranById(idPengeluaran: Int): PengeluaranDetailResponse

    suspend fun addPengeluaran(pengeluaran: Pengeluaran): PengeluaranDetailResponse

    suspend fun updatePengeluaran(idPengeluaran: Int, pengeluaran: Pengeluaran): PengeluaranDetailResponse

    suspend fun deletePengeluaran(idPengeluaran: Int)

}

    class NetworkPengeluaranRepository(
        private val pengeluaranService: PengeluaranService
    ) : PengeluaranRepository {

        override suspend fun getPengeluaran(): PengeluaranResponse {
            return pengeluaranService.getPengeluaran()
        }

        override suspend fun getPengeluaranById(idPengeluaran: Int): PengeluaranDetailResponse {
            return pengeluaranService.getPengeluaranById(idPengeluaran)
        }

        override suspend fun addPengeluaran(pengeluaran: Pengeluaran): PengeluaranDetailResponse {
            return pengeluaranService.addPengeluaran(pengeluaran)
        }

        override suspend fun updatePengeluaran(
            idPengeluaran: Int,
            pengeluaran: Pengeluaran
        ): PengeluaranDetailResponse {
            return pengeluaranService.updatePengeluaran(idPengeluaran, pengeluaran)
        }

        override suspend fun deletePengeluaran(idPengeluaran: Int) {
            return pengeluaranService.deletePengeluaran(idPengeluaran)
        }
    }

