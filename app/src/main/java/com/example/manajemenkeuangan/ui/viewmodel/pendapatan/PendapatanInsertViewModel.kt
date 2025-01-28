package com.example.manajemenkeuangan.ui.viewmodel.pendapatan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.model.Pendapatan
import com.example.manajemenkeuangan.repository.PendapatanRepository
import kotlinx.coroutines.launch


class PendapatanInsertViewModel (private val pnd : PendapatanRepository) : ViewModel() {
    var uiStatePnd by mutableStateOf(InsertPndUiState())
        private set

    fun updateInsertPndState(insertPndUiEvent : InsertPndUiEvent) {
        uiStatePnd = InsertPndUiState(insertPndUiEvent = insertPndUiEvent)
    }


    suspend fun insertPnd() {
        viewModelScope.launch {
            try {
                pnd.addPendapatan(uiStatePnd.insertPndUiEvent.toPnd())

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class InsertPndUiState (
    val insertPndUiEvent : InsertPndUiEvent = InsertPndUiEvent()
)

data class InsertPndUiEvent(
    val idPendapatan: Int? = null,
    val tglPendapatan: String = "",
    val totalPendapatan: Int = 0,
    val catatanPendapatan: String = "",
    val asetPendapatan: String = "",
    val kategoriPendapatan: String = ""
)

fun InsertPndUiEvent.toPnd(): Pendapatan = Pendapatan(

    tglPendapatan = tglPendapatan,
    totalPendapatan = totalPendapatan,
    catatanPendapatan = catatanPendapatan,
    idPendapatan = 0,
    asetPendapatan = asetPendapatan,
    kategoriPendapatan = asetPendapatan,
)

fun Pendapatan.toUiStatePnd(): InsertPndUiState = InsertPndUiState(
    insertPndUiEvent = toInsertPndUiEvent()
)

fun Pendapatan.toInsertPndUiEvent(): InsertPndUiEvent = InsertPndUiEvent(
    tglPendapatan = tglPendapatan,
    totalPendapatan = totalPendapatan,
    catatanPendapatan = catatanPendapatan,
    asetPendapatan = asetPendapatan,
    kategoriPendapatan = kategoriPendapatan
)

