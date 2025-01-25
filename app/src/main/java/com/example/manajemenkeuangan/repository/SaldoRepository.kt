package com.example.manajemenkeuangan.repository

import com.example.manajemenkeuangan.model.SaldoResponse
import com.example.manajemenkeuangan.service.SaldoService

interface SaldoRepository {
    suspend fun getSaldo(): SaldoResponse
}

class NetworkSaldoRepository(
    private val saldoService: SaldoService
) : SaldoRepository {

    override suspend fun getSaldo(): SaldoResponse {
        return saldoService.getSaldo()
    }
}