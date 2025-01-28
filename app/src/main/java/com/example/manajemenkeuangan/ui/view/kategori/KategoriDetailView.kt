package com.example.manajemenkeuangan.ui.view.kategori

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.manajemenkeuangan.model.Kategori
import com.example.manajemenkeuangan.ui.navigation.DestinasiNavigasi
import com.example.manajemenkeuangan.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkeuangan.ui.viewmodel.kategori.KategoriDetailViewModel
import com.example.manajemenkeuangan.ui.viewmodel.kategori.DetailKategoriUiState

object DestinasiDetailKategori : DestinasiNavigasi {
    override val route = "kategori_detail"
    override val titleRes = "Detail Kategori"
    const val idKategori = "idKategori"
    val routeWithArgs = "$route/{$idKategori}"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KategoriDetailView(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit, // Navigasi kembali ke layar sebelumnya
    onEditClick: (String) -> Unit, // Aksi untuk navigasi ke halaman edit
    kategoriDetailViewModel: KategoriDetailViewModel = viewModel(factory = PenyediaViewModel.Factory),
    onBack: () -> Unit,

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
                    judul = "Detail Kategori"
                )
            },      floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        val idKategori = (kategoriDetailViewModel.detailKategoriUiState as? DetailKategoriUiState.Success)?.kategori?.idKategori
                        if (idKategori != null) onEditClick(idKategori.toString())
                    },
                    containerColor = Color(0xFF7C0B48),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Kategori",
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
                    kategoriUiState = kategoriDetailViewModel.detailKategoriUiState,
                    retryAction = { kategoriDetailViewModel.getKategoriById() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Center)
                        .padding(16.dp),
                    onDeleteClick = {
                        kategoriDetailViewModel.deleteKategori(
                            idKategori = kategoriDetailViewModel.idKategori,
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
    kategoriUiState: DetailKategoriUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onDeleteClick: () -> Unit = {}
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

    when (kategoriUiState) {
        is DetailKategoriUiState.Success -> {
            Column(
                modifier = modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DetailCard(
                    kategori = kategoriUiState.kategori,
                    modifier = Modifier
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

        is DetailKategoriUiState.Loading -> {
            Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailKategoriUiState.Error -> {
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
    kategori: Kategori,
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
            ComponentDetailKategori(
                judul = "Id Kategori",
                isinya = kategori.idKategori.toString()
            )
            Spacer(modifier = Modifier.height(8.dp))
            ComponentDetailKategori(
                judul = "Nama Kategori",
                isinya = kategori.namaKategori
            )
        }
    }
}

@Composable
fun ComponentDetailKategori(
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
        text = { Text("Apakah anda yakin ingin menghapus data kategori ini?") },
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
