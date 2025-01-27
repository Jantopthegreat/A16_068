package com.example.manajemenkeuangan.ui.viewmodel.aset

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.model.Aset
import com.example.manajemenkeuangan.repository.AsetRepository
import kotlinx.coroutines.launch

class AsetInsertViewModel(private val asetRepository: AsetRepository) : ViewModel() {
    var uiStateAset by mutableStateOf(InsertAsetUiState())
        private set

    fun updateInsertAsetState(insertAsetUiEvent: InsertAsetUiEvent) {
        uiStateAset = InsertAsetUiState(insertAsetUiEvent = insertAsetUiEvent)
    }

    suspend fun insertAset() {
        viewModelScope.launch {
            try {
                asetRepository.insertAset(uiStateAset.insertAsetUiEvent.toAset())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

data class InsertAsetUiState(
    val insertAsetUiEvent: InsertAsetUiEvent = InsertAsetUiEvent()
)

data class InsertAsetUiEvent(
    val idAset: Int? = null,
    val namaAset: String = "",
)

fun InsertAsetUiEvent.toAset(): Aset = Aset(
    idAset = 0, // Assuming this is auto-generated or not needed during insertion
    namaAset = namaAset,

)

fun Aset.toUiStateAset(): InsertAsetUiState = InsertAsetUiState(
    insertAsetUiEvent = toInsertAsetUiEvent()
)

fun Aset.toInsertAsetUiEvent(): InsertAsetUiEvent = InsertAsetUiEvent(
    namaAset = namaAset,

)
