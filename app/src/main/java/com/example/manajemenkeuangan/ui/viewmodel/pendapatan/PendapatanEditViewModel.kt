package com.example.manajemenkeuangan.ui.viewmodel.pendapatan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.repository.PendapatanRepository
import com.example.manajemenkeuangan.ui.view.pendapatan.DestinasiEditPendapatan
import com.example.manajemenkeuangan.ui.viewmodel.aset.HomeAsetViewModel
import com.example.manajemenkeuangan.ui.viewmodel.kategori.HomeKategoriViewModel
import kotlinx.coroutines.launch


class PendapatanEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val pendapatanRepository: PendapatanRepository,
    private val asetViewModel: HomeAsetViewModel,
    private val kategoriViewModel: HomeKategoriViewModel
) : ViewModel() {

    var uiStateEdit by mutableStateOf(InsertPndUiState())
        private set

    val idPendapatanString: String? = savedStateHandle[DestinasiEditPendapatan.idPendapatan]
    var idPendapatan: Int = checkNotNull(idPendapatanString?.toIntOrNull()) {
        "idPendapatan is missing or not a valid integer"
    }

    init {
        viewModelScope.launch {
            val pendapatan = pendapatanRepository.getPendapatanById(idPendapatan).data
            uiStateEdit = pendapatan.toUiStatePnd()

            asetViewModel.selectAset(pendapatan.asetPendapatan)
            kategoriViewModel.selectKategori(pendapatan.kategoriPendapatan)
        }
    }

    fun updateInsertPndState(insertPndUiEvent: InsertPndUiEvent) {
        uiStateEdit = InsertPndUiState(insertPndUiEvent = insertPndUiEvent)
    }

    suspend fun editPendapatan(){
        viewModelScope.launch {
            try {
                pendapatanRepository.updatePendapatan(idPendapatan, uiStateEdit.insertPndUiEvent.toPnd())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}