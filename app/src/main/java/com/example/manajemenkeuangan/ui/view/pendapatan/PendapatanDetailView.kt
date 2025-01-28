package com.example.manajemenkeuangan.ui.view.pendapatan

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkeuangan.R
import com.example.manajemenkeuangan.model.Pendapatan
import com.example.manajemenkeuangan.ui.customwidget.TopAppBar
import com.example.manajemenkeuangan.ui.navigation.DestinasiNavigasi
import com.example.manajemenkeuangan.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkeuangan.ui.viewmodel.pendapatan.DetailPndUiState
import com.example.manajemenkeuangan.ui.viewmodel.pendapatan.PendapatanDetailViewModel
import java.text.SimpleDateFormat
import java.util.Locale

object DestinasiDetail : DestinasiNavigasi {
    override val route = "nim_detail"
    override val titleRes = "Detail pendapatan"
    const val idPendapatan = "idPendapatan"
    val routeWithArgs = "$route/{$idPendapatan}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PendapatanDetailView(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    onBack: () -> Unit,
    onEditClick: (String) -> Unit,
    pendapatandetailViewModel: PendapatanDetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
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
                    judul = "Pendapatan Detail")
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val idPendapatan = (pendapatandetailViewModel.detailPndUiState as? DetailPndUiState.Success)?.pendapatan?.idPendapatan
                        if (idPendapatan != null) onEditClick(idPendapatan.toString())
                    },
                    containerColor = Color(0xFF7C0B48),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit pendapatan",
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
                    pndUiState = pendapatandetailViewModel.detailPndUiState,
                    retryAction = { pendapatandetailViewModel.getPndbyId() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(16.dp),
                    onDeleteClick = {
                        pendapatandetailViewModel.deletePnd(
                            idPendapatan = pendapatandetailViewModel.idPendapatan,
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
    pndUiState: DetailPndUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {}
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

    when (pndUiState) {
        is DetailPndUiState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)){
            DetailCard(
                pendapatan = pndUiState.pendapatan,
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

        is DetailPndUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailPndUiState.Error -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Terjadi kesalahan.")
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = retryAction,
                    ) {
                        Text(text = "Coba Lagi")
                    }
                }
            }
        }
    }
}

@Composable
fun DetailCard(
    pendapatan: Pendapatan,
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
            ComponentDetailPendapatan(
                judul = "Id Pendapatan",
                isinya = pendapatan.idPendapatan.toString()
            )
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailPendapatan(
                judul = "Tanggal",
                isinya = formatDateTimeDetail(pendapatan.tglPendapatan)
            )
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailPendapatan(
                judul = "Catatan",
                isinya = pendapatan.catatanPendapatan
            )
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailPendapatan(
                judul = "Total",
                isinya = pendapatan.totalPendapatan.toString()
            )
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailPendapatan(
                judul = "Aset",
                isinya = pendapatan.asetPendapatan
            )
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailPendapatan(
                judul = "Kategori",
                isinya = pendapatan.kategoriPendapatan
            )
        }
    }
}

@Composable
fun ComponentDetailPendapatan(
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
        text = { Text("Apakah anda yakin ingin menghapus data pendapatan ini?") },
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
