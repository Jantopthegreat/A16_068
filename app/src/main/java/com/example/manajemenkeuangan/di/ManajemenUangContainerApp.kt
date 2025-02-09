package com.example.manajemenkeuangan.di

import com.example.manajemenkeuangan.repository.*
import com.example.manajemenkeuangan.service.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit

interface AppContainer {
    val asetRepository: AsetRepository
    val kategoriRepository: KategoriRepository
    val pendapatanRepository: PendapatanRepository
    val pengeluaranRepository: PengeluaranRepository
    val saldoRepository: SaldoRepository
}

class ManajemenUangContainerApp : AppContainer {

    private val baseUrl = "http://10.0.2.2:3000/" // Sesuaikan dengan endpoint API Anda

    private val json = Json { ignoreUnknownKeys = true }

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    // Services
    private val asetService: AsetService by lazy { retrofit.create(AsetService::class.java) }
    private val kategoriService: KategoriService by lazy { retrofit.create(KategoriService::class.java) }
    private val pendapatanService: PendapatanService by lazy { retrofit.create(PendapatanService::class.java) }
    private val pengeluaranService: PengeluaranService by lazy { retrofit.create(PengeluaranService::class.java) }
    private val saldoService: SaldoService by lazy { retrofit.create(SaldoService::class.java) }

    // Repositories
    override val asetRepository: AsetRepository by lazy { NetworkAsetRepository(asetService) }
    override val kategoriRepository: KategoriRepository by lazy { NetworkKategoriRepository(kategoriService) }
    override val pendapatanRepository: PendapatanRepository by lazy { NetworkPendapatanRepository(pendapatanService) }
    override val pengeluaranRepository: PengeluaranRepository by lazy { NetworkPengeluaranRepository(pengeluaranService) }
    override val saldoRepository: SaldoRepository by lazy { NetworkSaldoRepository(saldoService) }
}
