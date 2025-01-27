package com.example.manajemenkeuangan.ui.viewmodel.aset

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.model.Aset
import com.example.manajemenkeuangan.repository.AsetRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class HomeAsetUiState {
    data class Success(val aset: List<Aset>) : HomeAsetUiState()
    object Error : HomeAsetUiState()
    object Loading : HomeAsetUiState()
}

class HomeAsetViewModel(private val asetRepository: AsetRepository) : ViewModel() {
    var asetUiState: HomeAsetUiState by mutableStateOf(HomeAsetUiState.Loading)
        private set

    // Map untuk menyimpan pasangan nama dan ID
    private var asetMap: Map<String, Int> = emptyMap()

    // Properti untuk menyimpan ID dan nama yang dipilih
    var selectedAsetId: Int? by mutableStateOf(null)
        private set
    var selectedAsetName: String by mutableStateOf("")
        private set

    init {
        getAset()
    }

    fun getAset() {
        viewModelScope.launch {
            asetUiState = HomeAsetUiState.Loading
            asetUiState = try {
                val asetList = asetRepository.getAllAset().data
                asetMap = asetList.associate { it.namaAset to it.idAset }
                HomeAsetUiState.Success(asetList)
            } catch (e: IOException) {
                HomeAsetUiState.Error
            } catch (e: HttpException) {
                HomeAsetUiState.Error
            }
        }
    }

    fun selectAset(name: String) {
        selectedAsetName = name
        selectedAsetId = asetMap[name] // Ambil ID berdasarkan nama
    }
}
