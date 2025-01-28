package com.example.manajemenkeuangan.ui.viewmodel.pengeluaran

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.model.Pengeluaran
import com.example.manajemenkeuangan.repository.PengeluaranRepository
import com.example.manajemenkeuangan.ui.view.pengeluaran.DestinasiDetailPengeluaran
import kotlinx.coroutines.launch
import java.io.IOException

sealed class DetailPngUiState {
    data class Success(val pengeluaran: Pengeluaran) : DetailPngUiState()
    object Error : DetailPngUiState()
    object Loading : DetailPngUiState()
}

class PengeluaranDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val pengeluaranRepository: PengeluaranRepository
) : ViewModel() {

    val idPengeluaranString: String? = savedStateHandle[DestinasiDetailPengeluaran.idPengeluaran]
    var idPengeluaran: Int = checkNotNull(idPengeluaranString?.toIntOrNull()) {
        "idPengeluaran is missing or not a valid integer"
    }

    var detailPngUiState: DetailPngUiState by mutableStateOf(DetailPngUiState.Loading)
        private set

    init {
        getPngById()
    }

    fun getPngById() {
        viewModelScope.launch {
            detailPngUiState = DetailPngUiState.Loading
            detailPngUiState = try {
                DetailPngUiState.Success(pengeluaran = pengeluaranRepository.getPengeluaranById(idPengeluaran).data)
            } catch (e: IOException) {
                DetailPngUiState.Error
            }
        }
    }

    fun deletePng(
        idPengeluaran: Int,
        onDeleteSuccess: () -> Unit,
        onDeleteError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                pengeluaranRepository.deletePengeluaran(idPengeluaran)
                onDeleteSuccess()
            } catch (e: IOException) {
                onDeleteError()
            }
        }
    }
}
