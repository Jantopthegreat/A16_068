package com.example.manajemenkeuangan.ui.viewmodel.aset

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.repository.AsetRepository
import com.example.manajemenkeuangan.ui.view.aset.DestinasiEditAset
import kotlinx.coroutines.launch

class AsetEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val asetRepository: AsetRepository
) : ViewModel() {

    var uiStateEdit by mutableStateOf(InsertAsetUiState())
        private set

    val idAsetString: String? = savedStateHandle[DestinasiEditAset.idAset]
    var idAset: Int = checkNotNull(idAsetString?.toIntOrNull()) {
        "idAset is missing or not a valid integer"
    }

    init {
        viewModelScope.launch {
            uiStateEdit = asetRepository.getAsetById(idAset).data.toUiStateAset()
        }
    }

    fun updateInsertAsetState(insertAsetUiEvent: InsertAsetUiEvent) {
        uiStateEdit = InsertAsetUiState(insertAsetUiEvent = insertAsetUiEvent)
    }

    suspend fun editAset() {
        viewModelScope.launch {
            try {
                asetRepository.updateAset(idAset, uiStateEdit.insertAsetUiEvent.toAset())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
