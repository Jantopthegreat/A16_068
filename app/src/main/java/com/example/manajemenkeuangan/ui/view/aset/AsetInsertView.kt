package com.example.manajemenkeuangan.ui.view.aset

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
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkeuangan.ui.customwidget.TopAppBar
import com.example.manajemenkeuangan.ui.navigation.DestinasiNavigasi
import com.example.manajemenkeuangan.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkeuangan.ui.viewmodel.aset.HomeAsetViewModel
import com.example.manajemenkeuangan.ui.viewmodel.aset.InsertAsetUiEvent
import com.example.manajemenkeuangan.ui.viewmodel.aset.InsertAsetUiState
import com.example.manajemenkeuangan.ui.viewmodel.aset.AsetInsertViewModel
import kotlinx.coroutines.launch


object DestinasiInsertAset : DestinasiNavigasi {
    override val route = "entry_aset"
    override val titleRes = "Entry Aset"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsetInsertView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: AsetInsertViewModel = viewModel(factory = PenyediaViewModel.Factory),
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

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
                judul = "Home Aset")
        }    ) { innerPadding ->
        EntryBody(
            insertAsetUiState = viewModel.uiStateAset,
            onAsetValueChange = viewModel::updateInsertAsetState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.insertAset()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
        )
    }
}

@Composable
fun EntryBody(
    insertAsetUiState: InsertAsetUiState,
    onAsetValueChange: (InsertAsetUiEvent) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(18.dp),
        modifier = modifier.padding(12.dp)
    ) {
        FormInput(
            insertAsetUiEvent = insertAsetUiState.insertAsetUiEvent,
            onValueChange = onAsetValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Button(
            onClick = onSaveClick,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4C0062))
        ) {
            Text(text = "Simpan")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormInput(
    insertAsetUiEvent: InsertAsetUiEvent,
    modifier: Modifier = Modifier,
    onValueChange: (InsertAsetUiEvent) -> Unit = {},
    enabled: Boolean = true
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Input Nama Aset
        OutlinedTextField(
            value = insertAsetUiEvent.namaAset,
            onValueChange = { onValueChange(insertAsetUiEvent.copy(namaAset = it)) },
            label = { Text("Nama Aset") },
            modifier = Modifier.fillMaxWidth(),
            enabled = enabled,
            singleLine = true
        )
    }
}
