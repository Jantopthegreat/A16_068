package com.example.manajemenkeuangan.ui.viewmodel.pendapatan

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.manajemenkeuangan.model.Pendapatan
import com.example.manajemenkeuangan.repository.PendapatanRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


sealed class HomePendapatanUiState{
    data class Success( val pendapatan : List<Pendapatan>) : HomePendapatanUiState()
    object Error: HomePendapatanUiState()
    object Loading: HomePendapatanUiState()
}


class HomePendapatanViewModel(private val pnd: PendapatanRepository) : ViewModel() {
    var pndUiState: HomePendapatanUiState by mutableStateOf(HomePendapatanUiState.Loading)
        private set


    init {
        getPendapatan()
    }

    fun getPendapatan() {
        viewModelScope.launch {
            pndUiState = HomePendapatanUiState.Loading
            pndUiState = try {
                HomePendapatanUiState.Success(pnd.getPendapatan().data)
            } catch (e: IOException) {
                HomePendapatanUiState.Error
            } catch (e: HttpException) {
                HomePendapatanUiState.Error
            }
        }
    }


}
