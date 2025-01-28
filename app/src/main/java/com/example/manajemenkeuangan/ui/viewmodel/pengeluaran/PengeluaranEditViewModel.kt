package com.example.manajemenkeuangan.ui.viewmodel.pengeluaran

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.repository.PengeluaranRepository
import com.example.manajemenkeuangan.ui.view.pengeluaran.DestinasiEditPengeluaran
import kotlinx.coroutines.launch

class PengeluaranEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val pengeluaranRepository: PengeluaranRepository
) : ViewModel() {

    var uiStateEdit by mutableStateOf(InsertPngUiState())
        private set

    val idPengeluaranString: String? = savedStateHandle[DestinasiEditPengeluaran.idPengeluaran]
    var idPengeluaran: Int = checkNotNull(idPengeluaranString?.toIntOrNull()) {
        "idPengeluaran is missing or not a valid integer"
    }

    init {
        viewModelScope.launch {
            uiStateEdit = pengeluaranRepository.getPengeluaranById(idPengeluaran).data.toUiStatePng()
        }
    }

    fun updateInsertPngState(insertPngUiEvent: InsertPngUiEvent) {
        uiStateEdit = InsertPngUiState(insertPngUiEvent = insertPngUiEvent)
    }

    suspend fun editPengeluaran() {
        viewModelScope.launch {
            try {
                pengeluaranRepository.updatePengeluaran(idPengeluaran, uiStateEdit.insertPngUiEvent.toPng())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
