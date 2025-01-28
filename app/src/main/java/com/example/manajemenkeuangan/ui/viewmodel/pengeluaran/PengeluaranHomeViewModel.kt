package com.example.manajemenkeuangan.ui.viewmodel.pengeluaran

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.model.Pengeluaran
import com.example.manajemenkeuangan.repository.PengeluaranRepository
import com.example.manajemenkeuangan.repository.SaldoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


sealed class HomePengeluaranUiState {
    object Loading : HomePengeluaranUiState()
    data class Success(
        val pengeluaran: List<Pengeluaran>,
        val saldo: Int // Tambahkan saldo ke UI state
    ) : HomePengeluaranUiState()
    object Error : HomePengeluaranUiState()
}


class PengeluaranHomeViewModel(
    private val pengeluaranRepository: PengeluaranRepository,
    private val saldoRepository: SaldoRepository
) : ViewModel() {

    private val _pengeluaranList = MutableStateFlow<List<Pengeluaran>>(emptyList())
    val pengeluaranList: StateFlow<List<Pengeluaran>> = _pengeluaranList

    private val _saldo = MutableStateFlow<Int?>(null)
    val saldo: StateFlow<Int?> = _saldo

    init {
        getPengeluaran()
        getSaldo()
    }

    fun getPengeluaran() {
        viewModelScope.launch {
            try {
                val pengeluaran = pengeluaranRepository.getPengeluaran().data
                _pengeluaranList.value = pengeluaran
            } catch (e: Exception) {
                e.printStackTrace()
                _pengeluaranList.value = emptyList() // Jika ada error
            }
        }
    }

    fun getSaldo() {
        viewModelScope.launch {
            try {
                val response = saldoRepository.getSaldo()
                if (response.status) {
                    _saldo.value = response.data
                } else {
                    _saldo.value = null // Error pada response
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _saldo.value = null // Jika ada error pada request
            }
        }
    }
}
