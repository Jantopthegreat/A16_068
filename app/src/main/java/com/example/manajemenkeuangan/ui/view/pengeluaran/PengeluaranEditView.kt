package com.example.manajemenkeuangan.ui.view.pengeluaran

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkeuangan.ui.customwidget.TopAppBar
import com.example.manajemenkeuangan.ui.navigation.DestinasiNavigasi
import com.example.manajemenkeuangan.ui.view.pendapatan.DatePickerDialogWithCalendar
import com.example.manajemenkeuangan.ui.view.pendapatan.TimePickerDialogWithTime
import com.example.manajemenkeuangan.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkeuangan.ui.viewmodel.aset.HomeAsetViewModel
import com.example.manajemenkeuangan.ui.viewmodel.kategori.HomeKategoriViewModel
import com.example.manajemenkeuangan.ui.viewmodel.pengeluaran.PengeluaranEditViewModel
import kotlinx.coroutines.launch

object DestinasiEditPengeluaran : DestinasiNavigasi {
    override val route = "pengeluaran_edit"
    override val titleRes = "Edit Pengeluaran"
    const val idPengeluaran = "idPengeluaran"
    val routeWithArgs = "$route/{$idPengeluaran}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengeluaranEditView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: PengeluaranEditViewModel = viewModel(factory = PenyediaViewModel.Factory),
    asetViewModel: HomeAsetViewModel = viewModel(factory = PenyediaViewModel.Factory),
    kategoriViewModel: HomeKategoriViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    val asetUiState = asetViewModel.asetUiState
    val selectedAset by remember { mutableStateOf(asetViewModel.selectedAsetName) }

    val kategoriUiState = kategoriViewModel.kategoriUiState
    val selectedKategori by remember { mutableStateOf(kategoriViewModel.selectedKategoriName) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // State untuk datetime (tanggal dan waktu)
    val datetimeState = remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            .padding(10.dp)
            .padding(top = 18.dp),
        topBar = {
            TopAppBar(
                onBack = onBack,
                onLogoClick = {},
                showBackButton = true,
                judul = "Pengeluaran Edit")
        }
    ) { innerPadding ->
        EntryBody(
            insertPngUiState = viewModel.uiStateEdit,
            onPengeluaranValueChange = viewModel::updateInsertPngState,
            asetUiState = asetUiState,
            selectedAset = selectedAset,
            onAsetSelected = { asetViewModel.selectAset(it) },
            kategoriUiState = kategoriUiState,
            selectedKategori = selectedKategori,
            onKategoriSelected = { kategoriViewModel.selectKategori(it) },

            onSaveClick = {
                coroutineScope.launch {
                    viewModel.editPengeluaran()
                    navigateBack()
                }
            },
            showDatePicker = showDatePicker,
            onShowDatePickerChange = { showDatePicker = it },
            modifier = modifier.padding(innerPadding),
            showTimePicker = showTimePicker,
            onShowTimePickerChange = { showTimePicker = it },
            datetimeState = datetimeState // Pass datetime state

        )
    }
    // Tampilkan DatePickerDialog saat showDatePicker = true
    if (showDatePicker) {
        DatePickerDialogWithCalendar(
            onDateSelected = { selectedDate ->
                // Menyimpan tanggal dan menampilkan TimePickerDialog
                datetimeState.value = selectedDate // Simpan tanggal
                showDatePicker = false
                showTimePicker = true // Menampilkan TimePicker setelah memilih tanggal
            },
            onDismiss = { showDatePicker = false }
        )
    }

    // Tampilkan TimePickerDialog saat showTimePicker = true
    if (showTimePicker) {
        TimePickerDialogWithTime(
            onTimeSelected = { selectedTime ->
                // Gabungkan tanggal dan waktu untuk datetime
                val datetime = "${datetimeState.value} $selectedTime"
                datetimeState.value = datetime // Update datetime dengan waktu
                viewModel.updateInsertPngState(
                    viewModel.uiStateEdit.insertPngUiEvent.copy(tglPengeluaran = datetime)
                )
                showTimePicker = false // Menyembunyikan TimePicker setelah waktu dipilih
            },
            onDismiss = { showTimePicker = false }
        )
    }
}
