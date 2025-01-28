package com.example.manajemenkeuangan.ui.view.aset

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
import com.example.manajemenkeuangan.ui.viewmodel.aset.AsetEditViewModel
import kotlinx.coroutines.launch

object DestinasiEditAset : DestinasiNavigasi {
    override val route = "nim_edit_aset"
    override val titleRes = "Edit Aset"
    const val idAset = "idAset"
    val routeWithArgs = "$route/{$idAset}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsetEditView(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    viewModel: AsetEditViewModel = viewModel(factory = PenyediaViewModel.Factory)
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
                judul = "Edit Aset")
        }
    ) { innerPadding ->
        EntryBody(
            insertAsetUiState = viewModel.uiStateEdit,
            onAsetValueChange = viewModel::updateInsertAsetState,
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.editAset()
                    navigateBack()
                }
            },
            modifier = modifier.padding(innerPadding)
        )
    }
}
