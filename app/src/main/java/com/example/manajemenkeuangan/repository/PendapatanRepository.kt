package com.example.manajemenkeuangan.repository

import com.example.manajemenkeuangan.model.Pendapatan
import com.example.manajemenkeuangan.model.PendapatanResponse
import com.example.manajemenkeuangan.model.PendapatanDetailResponse
import com.example.manajemenkeuangan.service.PendapatanService

interface PendapatanRepository {
    suspend fun getPendapatan(): PendapatanResponse

    suspend fun getPendapatanById(idPendapatan: Int): PendapatanDetailResponse

    suspend fun addPendapatan(pendapatan: Pendapatan)

    suspend fun updatePendapatan(idPendapatan: Int, pendapatan: Pendapatan): PendapatanDetailResponse

    suspend fun deletePendapatan(idPendapatan: Int)

}
class NetworkPendapatanRepository(
    private val pendapatanService: PendapatanService
) : PendapatanRepository {

    override suspend fun getPendapatan(): PendapatanResponse {
        return pendapatanService.getPendapatan()
    }

    override suspend fun getPendapatanById(idPendapatan: Int): PendapatanDetailResponse {
        return pendapatanService.getPendapatanById(idPendapatan)
    }

    override suspend fun addPendapatan(pendapatan: Pendapatan) {
        return pendapatanService.addPendapatan(pendapatan)
    }

    override suspend fun updatePendapatan(idPendapatan: Int, pendapatan: Pendapatan): PendapatanDetailResponse {
        return pendapatanService.updatePendapatan(idPendapatan, pendapatan)
    }

    override suspend fun deletePendapatan(idPendapatan: Int) {
        return pendapatanService.deletePendapatan(idPendapatan)
    }



}