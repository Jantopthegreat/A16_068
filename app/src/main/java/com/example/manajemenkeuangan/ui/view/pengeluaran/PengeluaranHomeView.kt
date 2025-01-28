package com.example.manajemenkeuangan.ui.view.pengeluaran

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkeuangan.R
import com.example.manajemenkeuangan.model.Pengeluaran
import com.example.manajemenkeuangan.ui.customwidget.TopAppBar
import com.example.manajemenkeuangan.ui.navigation.DestinasiNavigasi
import com.example.manajemenkeuangan.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkeuangan.ui.viewmodel.pengeluaran.PengeluaranHomeViewModel
import java.text.SimpleDateFormat
import java.util.Locale


object DestinasiHomePengeluaran : DestinasiNavigasi {
    override val route = "homePengeluaran"
    override val titleRes = "Pengeluaran"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PengeluaranHomeView(
    navigateToAddPengeluaran: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (Int) -> Unit,
    onBack: () -> Unit,
    homePengeluaranViewModel: PengeluaranHomeViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val pengeluaranUiState = homePengeluaranViewModel.pengeluaranList.collectAsState().value
    val saldo = homePengeluaranViewModel.saldo.collectAsState().value

    val isSaldoNegative = saldo != null && saldo < 0
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        homePengeluaranViewModel.getPengeluaran()
        homePengeluaranViewModel.getSaldo()
    }

    if (showDialog.value) {
        ConfirmDialog(
            onConfirm = {
                navigateToAddPengeluaran()
                showDialog.value = false
            },
            onDismiss = { showDialog.value = false }
        )
    }

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            .padding(top = 18.dp),
        topBar = {
            TopAppBar(
                onBack = onBack,
                onLogoClick = {},
                showBackButton = true,
                judul = "Pengeluaran Home")
        },
    ) { innerPadding ->
        PengeluaranStatus(
            pengeluaranList = pengeluaranUiState,
            saldo = saldo,
            retryAction = { homePengeluaranViewModel.getPengeluaran() },
            modifier = Modifier.padding(innerPadding),
            navigateToAddPengeluaran = {
                if (isSaldoNegative) {
                    showDialog.value = true
                } else {
                    navigateToAddPengeluaran() // Langsung ke input pengeluaran
                }
            },
            onDetailClick = onDetailClick
        )
    }
}



@Composable
fun PengeluaranStatus(
    pengeluaranList: List<Pengeluaran>,
    saldo: Int?,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToAddPengeluaran: () -> Unit,
    onDetailClick: (Int) -> Unit
) {
    if (saldo == null) {
        OnLoading(modifier = modifier.fillMaxSize())
    } else {
        PengeluaranLayout(
            pengeluaranList = pengeluaranList,
            saldo = saldo,
            modifier = modifier,
            navigateToAddPengeluaran = navigateToAddPengeluaran,
            onDetailClick = onDetailClick
        )
    }
}

@Composable
fun PengeluaranLayout(
    pengeluaranList: List<Pengeluaran>,
    saldo: Int,
    modifier: Modifier = Modifier,
    navigateToAddPengeluaran: () -> Unit,
    onDetailClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AddPengeluaranCard(
                navigateToAddPengeluaran = navigateToAddPengeluaran,
                modifier = Modifier.fillMaxWidth()
            )
        }
        items(pengeluaranList) { pengeluaran ->
            PengeluaranCard(
                pengeluaran = pengeluaran,
                modifier = Modifier.fillMaxWidth(),
                onDetailClick = onDetailClick
            )
        }
    }
}

@Composable
fun AddPengeluaranCard(
    navigateToAddPengeluaran: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .padding(start = 100.dp, end = 100.dp)
            .height(50.dp)
            .clickable { navigateToAddPengeluaran() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF5800A6),
                            Color(0xFFBD106D)
                        )
                    )
                ),
            contentAlignment = Alignment.Center,
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    tint = Color.White,
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Pengeluaran",
                    modifier = Modifier
                        .size(30.dp)
                        .offset(x = (-10).dp)
                )
                Text(
                    text = "Tambah",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .offset(y = (-2).dp)
                )
            }
        }
    }
}

@Composable
fun PengeluaranCard(
    pengeluaran: Pengeluaran,
    modifier: Modifier = Modifier,
    onDetailClick: (Int) -> Unit
) {
    Card(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(
                    color = Color(0xFFE56969)
                ),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box (
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = formatDateTime(
                                pengeluaran.tglPengeluaran
                            ),
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Divider(
                            modifier = Modifier
                                .padding(vertical = 8.dp),
                            color = Color.Black
                        )
                    }
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth(),
                ) {
                    Column (
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row {
                            Image(
                                painter = painterResource(id = R.drawable.total),
                                contentDescription = "",
                                modifier = Modifier.size(30.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            Text(
                                text = pengeluaran.totalPengeluaran.toString(),
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                        Row {
                            Image(
                                painter = painterResource(id = R.drawable.note),
                                contentDescription = "",
                                modifier = Modifier.size(30.dp)
                                    .align(Alignment.CenterVertically)
                            )
                            Text(
                                text = pengeluaran.catatanPengeluaran,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    IconButton(
                        onClick = { onDetailClick(pengeluaran.idPengeluaran) },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.eye),
                            contentDescription = "",
                            modifier = Modifier
                                .fillMaxWidth()
                                .size(24.dp)
                                .offset(x = 80.dp),
                            tint = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun OnLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}


@Composable
fun ConfirmDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Saldo Anda Minus") },
        text = { Text("Keuangan Anda sudah minus, apakah Anda yakin ingin menambah pengeluaran?") },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Ya")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}


fun formatDateTime(dateTime: String): String {
    return try {
        // Ubah dateTime dari string ke Date
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateTime)
        outputFormat.format(date ?: "")
    } catch (e: Exception) {
        "Invalid date"
    }
}
