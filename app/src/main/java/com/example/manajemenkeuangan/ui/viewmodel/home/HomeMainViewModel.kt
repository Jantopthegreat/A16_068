package com.example.manajemenkeuangan.ui.viewmodel.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.repository.PendapatanRepository
import com.example.manajemenkeuangan.repository.PengeluaranRepository
import com.example.manajemenkeuangan.repository.SaldoRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed class HomeMainUiState {
    data class Success(
        val totalSaldo: Int,
        val totalPendapatan: Int,
        val totalPengeluaran: Int
    ) : HomeMainUiState()
    object Error : HomeMainUiState()
    object Loading : HomeMainUiState()
}

class HomeMainViewModel(
    private val saldoRepository: SaldoRepository,
    private val pendapatanRepository: PendapatanRepository,
    private val pengeluaranRepository: PengeluaranRepository
) : ViewModel() {

    var saldoUiState = mutableStateOf<HomeMainUiState>(HomeMainUiState.Loading)
        private set

    var totalPendapatan: Int = 0
    var totalPengeluaran: Int = 0

    init {
        getSaldoData()
    }

    fun getSaldoData() {
        viewModelScope.launch {
            saldoUiState.value = HomeMainUiState.Loading
            try {

                val saldoResponse = saldoRepository.getSaldo()
                val pendapatanResponse = pendapatanRepository.getPendapatan()
                val pengeluaranResponse = pengeluaranRepository.getPengeluaran()
                val saldo = saldoResponse.data

                totalPendapatan = pendapatanResponse.data.sumOf {
                    it.totalPendapatan.toInt()
                }

                totalPengeluaran = pengeluaranResponse.data.sumOf {
                    it.totalPengeluaran.toInt()
                }

                saldoUiState.value = HomeMainUiState.Success(
                    saldo, totalPendapatan, totalPengeluaran
                )

            } catch (e: IOException) {
                saldoUiState.value = HomeMainUiState.Error
            } catch (e: HttpException) {
                saldoUiState.value = HomeMainUiState.Error
            }
        }
    }
}
