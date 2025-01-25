package com.example.manajemenkeuangan.service

import com.example.manajemenkeuangan.model.Kategori
import com.example.manajemenkeuangan.model.KategoriDetailResponse
import com.example.manajemenkeuangan.model.KategoriResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface KategoriService {

    @Headers(
        "Accept:application/json",
        "Content-Type:application/json",
    )

    @GET("manajemenkeuangan/kategori")
    suspend fun getAllKategori(): KategoriResponse

    @GET("manajemenkeuangan/kategori/{idKategori}")
    suspend fun getKategoriById(@Path("idKategori") idKategori: Int): KategoriDetailResponse

    @POST("manajemenkeuangan/kategori")
    suspend fun insertKategori(@Body kategori: Kategori): Response<Void>

    @PUT("manajemenkeuangan/kategori/{idKategori}")
    suspend fun updateKategori(@Path("idKategori") idKategori: Int, @Body kategori: Kategori): Response<Void>

    @DELETE("manajemenkeuangan/kategori/{idKategori}")
    suspend fun deleteKategori(@Path("idKategori") idKategori: Int): Response<Void>
}