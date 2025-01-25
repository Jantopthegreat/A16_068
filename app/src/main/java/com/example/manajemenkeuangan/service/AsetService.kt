package com.example.manajemenkeuangan.service

import com.example.manajemenkeuangan.model.Aset
import com.example.manajemenkeuangan.model.AsetDetailResponse
import com.example.manajemenkeuangan.model.AsetResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface AsetService {

    @Headers(
        "Accept:application/json",
        "Content-Type:application/json",
    )

    @GET("manajemenkeuangan/aset")
    suspend fun getAllAset(): AsetResponse

    @GET("manajemenkeuangan/aset/{idAset}")
    suspend fun getAsetById(@Path("idAset") idAset: Int): AsetDetailResponse

    @POST("manajemenkeuangan/aset")
    suspend fun insertAset(@Body aset: Aset): Response<Void>

    @PUT("manajemenkeuangan/aset/{idAset}")
    suspend fun updateAset(@Path("idAset") idAset: Int, @Body aset: Aset): Response<Void>

    @DELETE("manajemenkeuangan/aset/{idAset}")
    suspend fun deleteAset(@Path("idAset") idAset: Int): Response<Void>
}
