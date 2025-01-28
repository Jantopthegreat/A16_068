package com.example.manajemenkeuangan.ui.viewmodel.kategori

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.model.Kategori
import com.example.manajemenkeuangan.repository.KategoriRepository
import kotlinx.coroutines.launch

class KategoriInsertViewModel(private val kategoriRepository: KategoriRepository) : ViewModel() {
    var uiStateKategori by mutableStateOf(InsertKategoriUiState())
        private set

    fun updateInsertKategoriState(insertKategoriUiEvent: InsertKategoriUiEvent) {
        uiStateKategori = InsertKategoriUiState(insertKategoriUiEvent = insertKategoriUiEvent)
    }

    suspend fun insertKategori() {
        viewModelScope.launch {
            try {
                kategoriRepository.insertKategori(uiStateKategori.insertKategoriUiEvent.toKategori())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class InsertKategoriUiState(
    val insertKategoriUiEvent: InsertKategoriUiEvent = InsertKategoriUiEvent()
)

data class InsertKategoriUiEvent(
    val idKategori: Int? = null,
    val namaKategori: String = "",
)

fun InsertKategoriUiEvent.toKategori(): Kategori = Kategori(
    idKategori = 0, // Assuming this is auto-generated or not needed during insertion
    namaKategori = namaKategori,
)

fun Kategori.toUiStateKategori(): InsertKategoriUiState = InsertKategoriUiState(
    insertKategoriUiEvent = toInsertKategoriUiEvent()
)

fun Kategori.toInsertKategoriUiEvent(): InsertKategoriUiEvent = InsertKategoriUiEvent(
    namaKategori = namaKategori,
)
