package com.example.manajemenkeuangan.ui.viewmodel.kategori

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.repository.KategoriRepository
import com.example.manajemenkeuangan.ui.view.kategori.DestinasiEditKategori
import kotlinx.coroutines.launch

class KategoriEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val kategoriRepository: KategoriRepository
) : ViewModel() {

    var uiStateEdit by mutableStateOf(InsertKategoriUiState())
        private set

    val idKategoriString: String? = savedStateHandle[DestinasiEditKategori.idKategori]
    var idKategori: Int = checkNotNull(idKategoriString?.toIntOrNull()) {
        "idKategori is missing or not a valid integer"
    }

    init {
        viewModelScope.launch {
            uiStateEdit = kategoriRepository.getKategoriById(idKategori).data.toUiStateKategori()
        }
    }

    fun updateInsertKategoriState(insertKategoriUiEvent: InsertKategoriUiEvent) {
        uiStateEdit = InsertKategoriUiState(insertKategoriUiEvent = insertKategoriUiEvent)
    }

    suspend fun editKategori() {
        viewModelScope.launch {
            try {
                kategoriRepository.updateKategori(idKategori, uiStateEdit.insertKategoriUiEvent.toKategori())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
