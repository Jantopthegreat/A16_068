package com.example.manajemenkeuangan.service

import com.example.manajemenkeuangan.model.SaldoResponse
import retrofit2.http.GET
import retrofit2.http.Headers

interface SaldoService {

    @Headers
    ("Accept:application/json",
    "Content-Type:application/json",)

    @GET("manajemenkeuangan/saldo")
    suspend fun getSaldo(): SaldoResponse
}