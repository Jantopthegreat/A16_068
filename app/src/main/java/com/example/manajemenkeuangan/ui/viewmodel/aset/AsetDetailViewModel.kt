package com.example.manajemenkeuangan.ui.viewmodel.aset

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.model.Aset
import com.example.manajemenkeuangan.repository.AsetRepository
import com.example.manajemenkeuangan.ui.view.aset.DestinasiDetailAset
import kotlinx.coroutines.launch
import java.io.IOException

sealed class DetailAsetUiState{
    data class Success(val aset: Aset) : DetailAsetUiState()
    object Error : DetailAsetUiState()
    object Loading : DetailAsetUiState()
}

class AsetDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val asetRepository: AsetRepository
) : ViewModel() {

    val idAsetString: String? = savedStateHandle[DestinasiDetailAset.idAset]
    var idAset: Int = checkNotNull(idAsetString?.toIntOrNull()) {
        "idAset is missing or not a valid integer"
    }

    var detailAsetUiState: DetailAsetUiState by mutableStateOf(DetailAsetUiState.Loading)
        private set

    init {
        getAsetById()
    }

    fun getAsetById(){
        viewModelScope.launch {
            detailAsetUiState = DetailAsetUiState.Loading
            detailAsetUiState = try {
                DetailAsetUiState.Success(aset = asetRepository.getAsetById(idAset).data)
            } catch (e: IOException) {
                DetailAsetUiState.Error
            }
        }
    }

    fun deleteAset(
        idAset: Int,
        onDeleteSuccess: () -> Unit,
        onDeleteError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                asetRepository.deleteAset(idAset)
                onDeleteSuccess()
            } catch (e: IOException) {
                onDeleteError()
            }
        }
    }
}
