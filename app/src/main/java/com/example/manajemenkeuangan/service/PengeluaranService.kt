package com.example.manajemenkeuangan.service

import com.example.manajemenkeuangan.model.Pengeluaran
import com.example.manajemenkeuangan.model.PengeluaranResponse
import com.example.manajemenkeuangan.model.PengeluaranDetailResponse
import retrofit2.http.*

interface PengeluaranService {

    @Headers(
        "Accept:application/json",
        "Content-Type:application/json",
    )

    @GET("manajemenkeuangan/pengeluaran")
    suspend fun getPengeluaran(): PengeluaranResponse

    @GET("manajemenkeuangan/pengeluaran/{idPengeluaran}")
    suspend fun getPengeluaranById(@Path("idPengeluaran") idPengeluaran: Int): PengeluaranDetailResponse

    @POST("manajemenkeuangan/pengeluaran")
    suspend fun addPengeluaran(@Body pengeluaran: Pengeluaran): PengeluaranDetailResponse

    @PUT("manajemenkeuangan/pengeluaran/{idPengeluaran}")
    suspend fun updatePengeluaran(@Path("idPengeluaran") idPengeluaran: Int, @Body pengeluaran: Pengeluaran): PengeluaranDetailResponse

    @DELETE("manajemenkeuangan/pengeluaran/{idPengeluaran}")
    suspend fun deletePengeluaran(@Path("idPengeluaran") idPengeluaran: Int)

}
