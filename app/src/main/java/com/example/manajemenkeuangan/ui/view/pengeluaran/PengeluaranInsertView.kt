package com.example.manajemenkeuangan.ui.view.pengeluaran

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkeuangan.ui.customwidget.TopAppBar
import com.example.manajemenkeuangan.ui.navigation.DestinasiNavigasi
import com.example.manajemenkeuangan.ui.view.pendapatan.DatePickerDialogWithCalendar
import com.example.manajemenkeuangan.ui.view.pendapatan.DropDownMenu
import com.example.manajemenkeuangan.ui.view.pendapatan.TimePickerDialogWithTime
import com.example.manajemenkeuangan.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkeuangan.ui.viewmodel.aset.HomeAsetUiState
import com.example.manajemenkeuangan.ui.viewmodel.aset.HomeAsetViewModel
import com.example.manajemenkeuangan.ui.viewmodel.kategori.HomeKategoriUiState
import com.example.manajemenkeuangan.ui.viewmodel.kategori.HomeKategoriViewModel
import com.example.manajemenkeuangan.ui.viewmodel.pengeluaran.InsertPngUiEvent
import com.example.manajemenkeuangan.ui.viewmodel.pengeluaran.InsertPngUiState
import com.example.manajemenkeuangan.ui.viewmodel.pengeluaran.PengeluaranInsertViewModel
import kotlinx.coroutines.launch
import java.util.Calendar


object DestinasiInsertPengeluaran : DestinasiNavigasi {
    override val route = "entry_pengeluaran"
    override val titleRes = "Entry Pengeluaran"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengeluaranInsertView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: PengeluaranInsertViewModel = viewModel(factory = PenyediaViewModel.Factory),
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
        modifier = modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            .padding(top = 18.dp),
        topBar = {
            TopAppBar(
                onBack = onBack,
                onLogoClick = {},
                showBackButton = true,
                judul = "Pengeluaran Insert")
        }
    ) { innerPadding ->
        EntryBody(
            insertPngUiState = viewModel.uiStatePng,
            onPengeluaranValueChange = viewModel::updateInsertPngState,
            asetUiState = asetUiState,
            selectedAset = selectedAset,
            onAsetSelected = { asetViewModel.selectAset(it) },
            kategoriUiState = kategoriUiState,
            selectedKategori = selectedKategori,
            onKategoriSelected = { kategoriViewModel.selectKategori(it) },
            onShowDatePickerChange = { showDatePicker = it },

            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertPng()
                    navigateBack()
                }
            },

            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            showDatePicker = showDatePicker,
            showTimePicker = showTimePicker, onShowTimePickerChange = { showTimePicker = it },
            datetimeState = datetimeState

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
                    viewModel.uiStatePng.insertPngUiEvent.copy(tglPengeluaran = datetime)
                )
                showTimePicker = false // Menyembunyikan TimePicker setelah waktu dipilih
            },
            onDismiss = { showTimePicker = false }
        )
    }
}

@Composable
fun EntryBody(
    insertPngUiState: InsertPngUiState,
    onPengeluaranValueChange: (InsertPngUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    asetUiState: HomeAsetUiState,
    selectedAset: String,
    onAsetSelected: (String) -> Unit,
    kategoriUiState: HomeKategoriUiState,
    selectedKategori: String,
    onKategoriSelected: (String) -> Unit,
    onShowDatePickerChange: (Boolean) -> Unit,
    showDatePicker: Boolean,
    showTimePicker: Boolean,
    onShowTimePickerChange: (Boolean) -> Unit,
    datetimeState: MutableState<String> // Menambahkan state datetime

    ) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInput(
            insertPngUiEvent = insertPngUiState.insertPngUiEvent,
            onValueChange = onPengeluaranValueChange,
            modifier = Modifier.fillMaxWidth(),
            asetUiState = asetUiState,
            selectedAset = selectedAset,
            onAsetSelected = onAsetSelected,
            kategoriUiState = kategoriUiState,
            selectedKategori = selectedKategori,
            onKategoriSelected = onKategoriSelected,
            onShowDatePickerChange = onShowDatePickerChange,
            datetimeState = datetimeState // Pass datetime state

        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF310062)
            )
        ) {
            Text(text = "Simpan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInput(
    insertPngUiEvent: InsertPngUiEvent,
    modifier: Modifier = Modifier,
    asetUiState: HomeAsetUiState,
    selectedAset: String,
    onAsetSelected: (String) -> Unit,
    kategoriUiState: HomeKategoriUiState,
    selectedKategori: String,
    onKategoriSelected: (String) -> Unit,
    onShowDatePickerChange: (Boolean) -> Unit,
    onValueChange: (InsertPngUiEvent) -> Unit = {},
    enabled: Boolean = true,
    datetimeState: MutableState<String> // Menambahkan state datetime
) {
    // State untuk menampilkan atau menyembunyikan dialog DatePicker
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
        value = insertPngUiEvent.tglPengeluaran,
        onValueChange = {}, // Tidak langsung diubah manual
        label = { Text("Tanggal Pendapatan") },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled,
        singleLine = true,
        readOnly = true, // Hanya untuk menampilkan hasil
        trailingIcon = {
            Button(
                onClick = { onShowDatePickerChange(true) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF310062)
                )
            ) {
                Text("Pilih")
            }
        }
    )

        // Input lainnya
        OutlinedTextField(
            value = insertPngUiEvent.totalPengeluaran.toString(),
            onValueChange = {
                val value = it.toIntOrNull() ?: 0
                onValueChange(insertPngUiEvent.copy(totalPengeluaran = value))
            },
            label = { Text("Total Pengeluaran") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertPngUiEvent.catatanPengeluaran,
            onValueChange = { onValueChange(insertPngUiEvent.copy(catatanPengeluaran = it)) },
            label = { Text("Catatan Pengeluaran") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        when (asetUiState) {
            is HomeAsetUiState.Loading -> {
                Text("Memuat data aset...")
            }
            is HomeAsetUiState.Error -> {
                Text("Gagal memuat data aset.")
            }
            is HomeAsetUiState.Success -> {
                DropDownMenu(
                    tittle = "Pilih Aset",
                    options = asetUiState.aset.map { it.namaAset },
                    selectedOption = selectedAset,
                    onOptionSelected = { name ->
                        onAsetSelected(name)
                        // Ambil ID berdasarkan nama yang dipilih
                        val asetId = asetUiState.aset.find { it.namaAset == name }?.idAset
                        if (asetId != null) {
                            onValueChange(insertPngUiEvent.copy(asetPengeluaran = asetId.toString()))
                        }
                    }
                )
            }
        }
        when (kategoriUiState) {
            is HomeKategoriUiState.Loading -> {
                Text("Memuat data Kategori...")
            }

            is HomeKategoriUiState.Error -> {
                Text("Gagal memuat data Kategori.")
            }

            is HomeKategoriUiState.Success -> {
                DropDownMenu(
                    tittle = "Pilih Kategori",
                    options = kategoriUiState.kategori.map { it.namaKategori },
                    selectedOption = selectedKategori,
                    onOptionSelected = { name ->
                        onKategoriSelected(name)
                        // Ambil ID berdasarkan nama yang dipilih
                        val kategoriId =
                            kategoriUiState.kategori.find { it.namaKategori == name }?.idKategori
                        if (kategoriId != null) {
                            onValueChange(insertPngUiEvent.copy(kategoriPengeluaran = kategoriId.toString()))
                        }
                    }
                )
            }
        }
    }
}

