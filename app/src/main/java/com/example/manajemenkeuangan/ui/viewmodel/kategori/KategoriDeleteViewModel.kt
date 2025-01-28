package com.example.manajemenkeuangan.ui.viewmodel.kategori

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.model.Kategori
import com.example.manajemenkeuangan.repository.KategoriRepository
import com.example.manajemenkeuangan.ui.view.kategori.DestinasiDetailKategori
import kotlinx.coroutines.launch
import java.io.IOException

sealed class DetailKategoriUiState{
    data class Success(val kategori: Kategori) : DetailKategoriUiState()
    object Error : DetailKategoriUiState()
    object Loading : DetailKategoriUiState()
}

class KategoriDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val kategoriRepository: KategoriRepository
) : ViewModel() {

    val idKategoriString: String? = savedStateHandle[DestinasiDetailKategori.idKategori]
    var idKategori: Int = checkNotNull(idKategoriString?.toIntOrNull()) {
        "idKategori is missing or not a valid integer"
    }

    var detailKategoriUiState: DetailKategoriUiState by mutableStateOf(DetailKategoriUiState.Loading)
        private set

    init {
        getKategoriById()
    }

    fun getKategoriById(){
        viewModelScope.launch {
            detailKategoriUiState = DetailKategoriUiState.Loading
            detailKategoriUiState = try {
                DetailKategoriUiState.Success(kategori = kategoriRepository.getKategoriById(idKategori).data)
            } catch (e: IOException) {
                DetailKategoriUiState.Error
            }
        }
    }

    fun deleteKategori(
        idKategori: Int,
        onDeleteSuccess: () -> Unit,
        onDeleteError: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                kategoriRepository.deleteKategori(idKategori)
                onDeleteSuccess()
            } catch (e: IOException) {
                onDeleteError()
            }
        }
    }
}
