package com.example.manajemenkeuangan.ui.view.pendapatan

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkeuangan.ui.customwidget.TopAppBar
import com.example.manajemenkeuangan.ui.navigation.DestinasiNavigasi
import com.example.manajemenkeuangan.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkeuangan.ui.viewmodel.aset.HomeAsetUiState
import com.example.manajemenkeuangan.ui.viewmodel.aset.HomeAsetViewModel
import com.example.manajemenkeuangan.ui.viewmodel.kategori.HomeKategoriUiState
import com.example.manajemenkeuangan.ui.viewmodel.kategori.HomeKategoriViewModel
import com.example.manajemenkeuangan.ui.viewmodel.pendapatan.InsertPndUiEvent
import com.example.manajemenkeuangan.ui.viewmodel.pendapatan.InsertPndUiState
import com.example.manajemenkeuangan.ui.viewmodel.pendapatan.PendapatanInsertViewModel
import kotlinx.coroutines.launch
import java.util.Calendar


object DestinasiInsertPendapatan : DestinasiNavigasi {
    override val route = "entry_pendapatan"
    override val titleRes = "Entry Pendapatan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryPndScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: PendapatanInsertViewModel = viewModel(factory = PenyediaViewModel.Factory),
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
                judul = "Pendapatan Insert"
            )
        }
    ) { innerPadding ->
        EntryBody(
            insertPndUiState = viewModel.uiStatePnd,
            onPendapatanValueChange = viewModel::updateInsertPndState,
            asetUiState = asetUiState,
            selectedAset = selectedAset,
            onAsetSelected = { asetViewModel.selectAset(it) },
            kategoriUiState = kategoriUiState,
            selectedKategori = selectedKategori,
            onKategoriSelected = { kategoriViewModel.selectKategori(it) },

            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertPnd()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth(),
            showDatePicker = showDatePicker,
            showTimePicker = showTimePicker,
            onShowDatePickerChange = { showDatePicker = it },
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
                viewModel.updateInsertPndState(
                    viewModel.uiStatePnd.insertPndUiEvent.copy(tglPendapatan = datetime)
                )
                showTimePicker = false // Menyembunyikan TimePicker setelah waktu dipilih
            },
            onDismiss = { showTimePicker = false }
        )
    }
}


@Composable
fun EntryBody(
    insertPndUiState: InsertPndUiState,
    onPendapatanValueChange: (InsertPndUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    asetUiState: HomeAsetUiState,
    selectedAset: String,
    onAsetSelected: (String) -> Unit,
    kategoriUiState: HomeKategoriUiState,
    selectedKategori: String,
    onKategoriSelected: (String) -> Unit,
    showDatePicker: Boolean,
    showTimePicker: Boolean,
    onShowTimePickerChange: (Boolean) -> Unit,
    onShowDatePickerChange: (Boolean) -> Unit,
    datetimeState: MutableState<String>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(24.dp)
    ) {
        // Form input
        FormInput(
            insertPndUiEvent = insertPndUiState.insertPndUiEvent,
            onValueChange = onPendapatanValueChange,
            modifier = Modifier.fillMaxWidth(),
            asetUiState = asetUiState,
            selectedAset = selectedAset,
            onAsetSelected = onAsetSelected,
            kategoriUiState = kategoriUiState,
            selectedKategori = selectedKategori,
            onKategoriSelected = onKategoriSelected,
            onShowDatePickerChange = onShowDatePickerChange,
            datetimeState = datetimeState
        )

        // Save Button
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

@Composable
fun FormInput(
    insertPndUiEvent: InsertPndUiEvent,
    modifier: Modifier = Modifier,
    asetUiState: HomeAsetUiState,
    selectedAset: String,
    onAsetSelected: (String) -> Unit,
    kategoriUiState: HomeKategoriUiState,
    selectedKategori: String,
    onKategoriSelected: (String) -> Unit,
    onValueChange: (InsertPndUiEvent) -> Unit = {},
    onShowDatePickerChange: (Boolean) -> Unit,
    enabled: Boolean = true,
    datetimeState: MutableState<String> // Menambahkan state datetime
) {
    val context = LocalContext.current

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedTextField(
            value = insertPndUiEvent.tglPendapatan,
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
        OutlinedTextField(
            value = insertPndUiEvent.totalPendapatan.toString(),
            onValueChange = {
                val value = it.toIntOrNull() ?: 0
                onValueChange(insertPndUiEvent.copy(totalPendapatan = value))
            },
            label = { Text("Total Pendapatan") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
        OutlinedTextField(
            value = insertPndUiEvent.catatanPendapatan,
            onValueChange = { onValueChange(insertPndUiEvent.copy(catatanPendapatan = it)) },
            label = { Text("Catatan Pendapatan") },
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
                            onValueChange(insertPndUiEvent.copy(asetPendapatan = asetId.toString()))
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
                        val kategoriId = kategoriUiState.kategori.find { it.namaKategori == name }?.idKategori
                        if (kategoriId != null) {
                            onValueChange(insertPndUiEvent.copy(kategoriPendapatan = kategoriId.toString()))
                        }
                    }
                )
            }
        }
    }
}




@Composable
fun DropDownMenu(
    tittle: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    var currentSelected by remember { mutableStateOf(selectedOption) }

    Column {
        OutlinedTextField(
            value = currentSelected,
            onValueChange = {},
            readOnly = true,
            label = { Text(text = tittle) },
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(
                        imageVector = if (expanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropDown,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = option) },
                    onClick = {
                        onOptionSelected(option)
                        currentSelected = option
                        expanded = false
                    }
                )
            }
        }
        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                color = Color.Red
            )
        }
    }
}

@Composable
fun DatePickerDialogWithCalendar(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val calendar = Calendar.getInstance()
    val context = LocalContext.current

    val datePickerDialog = remember {
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                val formattedDate = String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth)
                onDateSelected(formattedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    datePickerDialog.show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7C0B48)
                )
            ) {
                Text("Pilih Tanggal")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7C0B48)
                )
            ) {
                Text("Batal")
            }
        },
        text = { Text("Pilih tanggal untuk pendapatan") }
    )
}

@Composable
fun TimePickerDialogWithTime(
    onTimeSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    val calendar = Calendar.getInstance()
    val timePickerDialog = remember {
        TimePickerDialog(
            context,
            { _, hourOfDay, minute ->
                val formattedTime = String.format("%02d:%02d", hourOfDay, minute)
                onTimeSelected(formattedTime)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = {
                    timePickerDialog.show()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7C0B48)
                )
            ) {
                Text("Pilih Waktu")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF7C0B48)
                )
            ) {
                Text("Batal")
            }
        },
        text = { Text("Pilih waktu untuk pendapatan") }
    )
}