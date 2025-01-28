package com.example.manajemenkeuangan.ui.viewmodel.kategori

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.model.Kategori
import com.example.manajemenkeuangan.repository.KategoriRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class HomeKategoriUiState {
    data class Success(val kategori: List<Kategori>) : HomeKategoriUiState()
    object Error : HomeKategoriUiState()
    object Loading : HomeKategoriUiState()
}

class HomeKategoriViewModel(private val kategoriRepository: KategoriRepository) : ViewModel() {
    var kategoriUiState: HomeKategoriUiState by mutableStateOf(HomeKategoriUiState.Loading)
        private set

    // Map untuk menyimpan pasangan nama dan ID
    private var kategoriMap: Map<String, Int> = emptyMap()

    // Properti untuk menyimpan ID dan nama yang dipilih
    var selectedKategoriId: Int? by mutableStateOf(null)
        private set
    var selectedKategoriName: String by mutableStateOf("")
        private set

    init {
        getKategori()
    }

    fun getKategori() {
        viewModelScope.launch {
            kategoriUiState = HomeKategoriUiState.Loading
            kategoriUiState = try {
                val kategoriList = kategoriRepository.getAllKategori().data
                kategoriMap = kategoriList.associate { it.namaKategori to it.idKategori }
                HomeKategoriUiState.Success(kategoriList)
            } catch (e: IOException) {
                HomeKategoriUiState.Error
            } catch (e: HttpException) {
                HomeKategoriUiState.Error
            }
        }
    }

    fun selectKategori(name: String) {
        selectedKategoriName = name
        selectedKategoriId = kategoriMap[name] // Ambil ID berdasarkan nama
    }
}
