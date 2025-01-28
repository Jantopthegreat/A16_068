package com.example.manajemenkeuangan.ui.viewmodel.pengeluaran

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.model.Pengeluaran
import com.example.manajemenkeuangan.repository.PengeluaranRepository
import kotlinx.coroutines.launch

class PengeluaranInsertViewModel(private val png: PengeluaranRepository) : ViewModel() {
    var uiStatePng by mutableStateOf(InsertPngUiState())
        private set

    fun updateInsertPngState(insertPngUiEvent: InsertPngUiEvent) {
        uiStatePng = InsertPngUiState(insertPngUiEvent = insertPngUiEvent)
    }

    suspend fun insertPng() {
        viewModelScope.launch {
            try {
                png.addPengeluaran(uiStatePng.insertPngUiEvent.toPng())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class InsertPngUiState(
    val insertPngUiEvent: InsertPngUiEvent = InsertPngUiEvent()
)

data class InsertPngUiEvent(
    val idPengeluaran: Int? = null,
    val tglPengeluaran: String = "",
    val totalPengeluaran: Int = 0,
    val catatanPengeluaran: String = "",
    val asetPengeluaran: String = "",
    val kategoriPengeluaran: String = ""
)

fun InsertPngUiEvent.toPng(): Pengeluaran = Pengeluaran(
    tglPengeluaran = tglPengeluaran,
    totalPengeluaran = totalPengeluaran,
    catatanPengeluaran = catatanPengeluaran,
    idPengeluaran = 0,
    asetPengeluaran = asetPengeluaran,
    kategoriPengeluaran = kategoriPengeluaran,
)

fun Pengeluaran.toUiStatePng(): InsertPngUiState = InsertPngUiState(
    insertPngUiEvent = toInsertPngUiEvent()
)

fun Pengeluaran.toInsertPngUiEvent(): InsertPngUiEvent = InsertPngUiEvent(
    tglPengeluaran = tglPengeluaran,
    totalPengeluaran = totalPengeluaran,
    catatanPengeluaran = catatanPengeluaran,
    asetPengeluaran = asetPengeluaran,
    kategoriPengeluaran = kategoriPengeluaran
)
