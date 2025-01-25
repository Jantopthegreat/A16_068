package com.example.manajemenkeuangan.service

import com.example.manajemenkeuangan.model.Pendapatan
import com.example.manajemenkeuangan.model.PendapatanResponse
import com.example.manajemenkeuangan.model.PendapatanDetailResponse
import retrofit2.http.*

interface PendapatanService {

    @Headers(
        "Accept:application/json",
        "Content-Type:application/json",
    )

    @GET("manajemenkeuangan/pendapatan")
    suspend fun getPendapatan(): PendapatanResponse

    @GET("manajemenkeuangan/pendapatan/{idPendapatan}")
    suspend fun getPendapatanById(@Path("idPendapatan") idPendapatan: Int): PendapatanDetailResponse

    @POST("manajemenkeuangan/pendapatan")
    suspend fun addPendapatan(@Body pendapatan: Pendapatan)

    @PUT("manajemenkeuangan/pendapatan/{idPendapatan}")
    suspend fun updatePendapatan(@Path("idPendapatan") idPendapatan: Int, @Body pendapatan: Pendapatan): PendapatanDetailResponse

    @DELETE("manajemenkeuangan/pendapatan/{idPendapatan}")
    suspend fun deletePendapatan(@Path("idPendapatan") idPendapatan: Int)


}
