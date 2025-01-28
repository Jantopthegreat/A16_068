package com.example.manajemenkeuangan.ui.view.kategori

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkeuangan.ui.customwidget.TopAppBar
import com.example.manajemenkeuangan.ui.navigation.DestinasiNavigasi
import com.example.manajemenkeuangan.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkeuangan.ui.viewmodel.kategori.KategoriEditViewModel
import kotlinx.coroutines.launch

object DestinasiEditKategori : DestinasiNavigasi {
    override val route = "nim_edit_kategori"
    override val titleRes = "Edit Kategori"
    const val idKategori = "idKategori"
    val routeWithArgs = "$route/{$idKategori}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KategoriEditView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: KategoriEditViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold (
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            .padding(10.dp)
            .padding(top = 18.dp),
        topBar = {
            TopAppBar(
                onBack = onBack,
                onLogoClick = {},
                showBackButton = true,
                judul = "Edit Kategori")
        }
    ){ innerPadding ->
        EntryBody(
            insertKategoriUiState = viewModel.uiStateEdit,
            onKategoriValueChange = viewModel::updateInsertKategoriState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.editKategori()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}
