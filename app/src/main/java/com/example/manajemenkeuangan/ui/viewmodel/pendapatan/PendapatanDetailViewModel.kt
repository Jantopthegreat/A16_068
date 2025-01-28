package com.example.manajemenkeuangan.ui.viewmodel.pendapatan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.model.Pendapatan
import com.example.manajemenkeuangan.repository.PendapatanRepository
import com.example.manajemenkeuangan.ui.view.pendapatan.DestinasiDetail
import kotlinx.coroutines.launch
import java.io.IOException

sealed class DetailPndUiState{
    data class Success(val pendapatan: Pendapatan) : DetailPndUiState()
    object Error : DetailPndUiState()
    object Loading : DetailPndUiState()
}

class PendapatanDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val pendapatanRepository: PendapatanRepository
) : ViewModel() {

    val idPendapatanString: String? = savedStateHandle[DestinasiDetail.idPendapatan]
    var idPendapatan: Int = checkNotNull(idPendapatanString?.toIntOrNull()) {
        "idPendapatan is missing or not a valid integer"
    }

    var detailPndUiState: DetailPndUiState by mutableStateOf(DetailPndUiState.Loading)
        private set

    init {
        getPndbyId()
    }

    fun getPndbyId(){
        viewModelScope.launch {
            detailPndUiState = DetailPndUiState.Loading
            detailPndUiState = try {
                DetailPndUiState.Success(pendapatan = pendapatanRepository.getPendapatanById(idPendapatan).data)
            } catch (e: IOException) {
                DetailPndUiState.Error
            }
        }
    }

    fun deletePnd(
        idPendapatan: Int,
        onDeleteSuccess: () -> Unit,
        onDeleteError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                pendapatanRepository.deletePendapatan(idPendapatan)
                onDeleteSuccess()
            } catch (e: IOException) {
                onDeleteError()
            }
        }
    }
}