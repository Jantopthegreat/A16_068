package com.example.manajemenkeuangan.ui.view.pengeluaran

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
import com.example.manajemenkeuangan.model.Pengeluaran
import com.example.manajemenkeuangan.ui.customwidget.TopAppBar
import com.example.manajemenkeuangan.ui.navigation.DestinasiNavigasi
import com.example.manajemenkeuangan.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkeuangan.ui.viewmodel.pengeluaran.DetailPngUiState
import com.example.manajemenkeuangan.ui.viewmodel.pengeluaran.PengeluaranDetailViewModel
import java.text.SimpleDateFormat
import java.util.Locale

object DestinasiDetailPengeluaran : DestinasiNavigasi {
    override val route = "pengeluaran_detail"
    override val titleRes = "Detail Pengeluaran"
    const val idPengeluaran = "idPengeluaran"
    val routeWithArgs = "$route/{$idPengeluaran}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengeluaranDetailView(
    modifier: Modifier = Modifier,
    onBack: () -> Unit,
    navigateBack: () -> Unit,
    onEditClick: (String) -> Unit,
    pengeluaranDetailViewModel: PengeluaranDetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
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
                TopAppBar(
                    onBack = onBack,
                    onLogoClick = {},
                    showBackButton = true,
                    judul = "Pengeluaran Detail")
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val idPengeluaran = (pengeluaranDetailViewModel.detailPngUiState as? DetailPngUiState.Success)?.pengeluaran?.idPengeluaran
                        if (idPengeluaran != null) onEditClick(idPengeluaran.toString())
                    },
                    containerColor = Color(0xFF7C0B48),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Detail Pengeluaran",
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
                    pngUiState = pengeluaranDetailViewModel.detailPngUiState,
                    retryAction = { pengeluaranDetailViewModel.getPngById() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(16.dp),
                    onDeleteClick = {
                        pengeluaranDetailViewModel.deletePng(
                            idPengeluaran = pengeluaranDetailViewModel.idPengeluaran,
                            onDeleteSuccess = navigateBack, // Navigasi jika berhasil
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
    pngUiState: DetailPngUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {}
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

    when (pngUiState) {
        is DetailPngUiState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailCard(
                    pengeluaran = pngUiState.pengeluaran,
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
                            onDeleteClick()
                        },
                        onDeleteCancel = { deleteConfirmationRequired = false },
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }
        }

        is DetailPngUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailPngUiState.Error -> {
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
    pengeluaran: Pengeluaran,
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
            ComponentDetailPengeluaran(
                judul = "Id Pengeluaran",
                isinya = pengeluaran.idPengeluaran.toString()
            )
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailPengeluaran(
                judul = "Tanggal",
                isinya = formatDateTimeDetail(pengeluaran.tglPengeluaran)
            )
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailPengeluaran(
                judul = "Catatan",
                isinya = pengeluaran.catatanPengeluaran
            )
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailPengeluaran(
                judul = "Total",
                isinya = pengeluaran.totalPengeluaran.toString()
            )
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailPengeluaran(
                judul = "Aset",
                isinya = pengeluaran.asetPengeluaran
            )
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailPengeluaran(
                judul = "Kategori",
                isinya = pengeluaran.kategoriPengeluaran
            )
        }
    }
}

@Composable
fun ComponentDetailPengeluaran(
    modifier: Modifier = Modifier,
    judul: String,
    isinya: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center
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
        text = { Text("Apakah anda yakin ingin menghapus data pengeluaran ini?") },
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

fun formatDateTimeDetail(dateTime: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateTime)
        outputFormat.format(date ?: "")
    } catch (e: Exception) {
        "Invalid date"
    }
}