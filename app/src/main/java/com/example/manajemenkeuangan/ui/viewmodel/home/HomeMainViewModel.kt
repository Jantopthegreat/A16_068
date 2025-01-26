package com.example.manajemenkeuangan.ui.viewmodel.home

sealed class HomeMainUiState {
    data class Success(
        val totalSaldo: Int,
        val totalPendapatan: Int,
        val totalPengeluaran: Int
    ) : HomeMainUiState()
    object Error : HomeMainUiState()
    object Loading : HomeMainUiState()
}
