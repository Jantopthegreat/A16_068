package com.example.manajemenkeuangan.ui.view.aset

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkeuangan.R
import com.example.manajemenkeuangan.model.Aset
import com.example.manajemenkeuangan.ui.navigation.DestinasiNavigasi
import com.example.manajemenkeuangan.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkeuangan.ui.viewmodel.aset.AsetDetailViewModel
import com.example.manajemenkeuangan.ui.viewmodel.aset.DetailAsetUiState

object DestinasiDetailAset : DestinasiNavigasi {
    override val route = "aset_detail"
    override val titleRes = "Detail Aset"
    const val idAset = "idAset"
    val routeWithArgs = "$route/{$idAset}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsetDetailView(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit, // Navigasi kembali ke layar sebelumnya
    onEditClick: (String) -> Unit, // Aksi untuk navigasi ke halaman edit
    onBack: () -> Unit,
    asetDetailViewModel: AsetDetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Box(
        modifier = Modifier.fillMaxSize()
            .padding(top = 18.dp),

        ) {
        Image(
            painter = painterResource(id = R.drawable.bgdetail0),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        Scaffold(
            modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            topBar = {
                com.example.manajemenkeuangan.ui.customwidget.TopAppBar(
                    onBack = onBack,
                    onLogoClick = {},
                    showBackButton = true,
                    judul = "Pendapatan Detail"
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val idAset = (asetDetailViewModel.detailAsetUiState as? DetailAsetUiState.Success)?.aset?.idAset
                        if (idAset != null) onEditClick(idAset.toString())
                    },
                    containerColor = Color(0xFF7C0B48),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Aset",
                        tint = Color.White
                    )
                }
            },
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(innerPadding)
            ) {
                DetailStatus(
                    asetUiState = asetDetailViewModel.detailAsetUiState,
                    retryAction = { asetDetailViewModel.getAsetById() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(16.dp),
                    onDeleteClick = {
                        asetDetailViewModel.deleteAset(
                            idAset = asetDetailViewModel.idAset,
                            onDeleteSuccess = navigateBack,
                            onDeleteError = {
                            }
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun DetailStatus(
    asetUiState: DetailAsetUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {}
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

    when (asetUiState) {
        is DetailAsetUiState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailCard(
                    aset = asetUiState.aset,
                    modifier = modifier
                )
                Button(
                    onClick = {
                        deleteConfirmationRequired = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp),
                    colors = ButtonDefaults
                        .buttonColors(
                        containerColor = Color(0xFF4C0062)
                    )
                ) {
                    Text(text = "Delete")
                }
                if (deleteConfirmationRequired) {
                    DeleteConfirmationDialog(
                        onDeleteConfirm = {
                            deleteConfirmationRequired = false
                            onDeleteClick() // Panggil aksi penghapusan
                        },
                        onDeleteCancel = { deleteConfirmationRequired = false },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        is DetailAsetUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailAsetUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Terjadi kesalahan.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = retryAction) {
                        Text(text = "Coba Lagi")
                    }
                }
            }
        }
    }
}

@Composable
fun DetailCard(
    aset: Aset,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent.copy(alpha = 0.2f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ComponentDetailAset(judul = "Id Aset", isinya = aset.idAset.toString())
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailAset(judul = "Nama Aset", isinya = aset.namaAset)
        }
    }
}

@Composable
fun ComponentDetailAset(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            text = "$judul:",
            fontSize = 15.sp,
            color = Color.White
        )
        Text(
            text = isinya,
            fontSize = 20.sp,
            color = Color.White
        )
    }
}

@Composable
private fun DeleteConfirmationDialog (
    onDeleteConfirm: () -> Unit, onDeleteCancel: () -> Unit, modifier: Modifier =
        Modifier
) {
    AlertDialog(onDismissRequest = { },
        title = { Text("Delete Data") },
        text = { Text("Apakah anda yakin ingin menghapus data aset ini?") },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(text = "Cancel")
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(text = "Yes")
            }
        }
    )
}
