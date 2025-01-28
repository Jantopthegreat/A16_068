package com.example.manajemenkeuangan.ui.view.pendapatan

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.manajemenkeuangan.model.Pendapatan
import com.example.manajemenkeuangan.ui.customwidget.TopAppBar
import com.example.manajemenkeuangan.ui.navigation.DestinasiNavigasi
import com.example.manajemenkeuangan.ui.theme.groteskFont
import com.example.manajemenkeuangan.ui.theme.zodiakFont
import com.example.manajemenkeuangan.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkeuangan.ui.viewmodel.aset.HomeAsetUiState
import com.example.manajemenkeuangan.ui.viewmodel.aset.HomeAsetViewModel
import com.example.manajemenkeuangan.ui.viewmodel.pendapatan.HomePendapatanUiState
import com.example.manajemenkeuangan.ui.viewmodel.pendapatan.HomePendapatanViewModel
import java.text.SimpleDateFormat
import java.util.Locale


object DestinasiHomePendapatan : DestinasiNavigasi {
    override val route = "homePendapatan"
    override val titleRes = "Pendapatan"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePendapatanView(
    navigateToAddPendapatan: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: HomePendapatanViewModel = viewModel(factory = PenyediaViewModel.Factory),
    asetViewModel: HomeAsetViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homePendapatanUiState = viewModel.pndUiState

    val asetUiState = asetViewModel.asetUiState

    LaunchedEffect (Unit)
    {
        viewModel.getPendapatan()
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
                judul = "Pendapatan Home")
        }
    )
    { innerPadding ->
        when (asetUiState) {
            is HomeAsetUiState.Loading -> Text("Loading...")
            is HomeAsetUiState.Error -> Text("Failed to load assets")
            is HomeAsetUiState.Success -> {
                val asetList = (asetUiState as HomeAsetUiState.Success).aset.map { it.namaAset }
            }
        }
        PendapatanStatus(
            homePendapatanUiState = homePendapatanUiState,
            retryAction = { viewModel.getPendapatan() },
            modifier = Modifier.padding(innerPadding),
            navigateToAddPendapatan = navigateToAddPendapatan,
            onDetailClick = onDetailClick
        )
    }
}


@Composable
fun PendapatanStatus(
    homePendapatanUiState: HomePendapatanUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToAddPendapatan: () -> Unit,
    onDetailClick: (Int) -> Unit
) {
    when (homePendapatanUiState) {
        is HomePendapatanUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomePendapatanUiState.Success -> {
            PendapatanLayout(
                pendapatanList = homePendapatanUiState.pendapatan,
                modifier = modifier,
                navigateToAddPendapatan = navigateToAddPendapatan,
                onDetailClick = onDetailClick
            )
        }
        is HomePendapatanUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}



@Composable
fun PendapatanLayout(
    pendapatanList: List<Pendapatan>,
    modifier: Modifier = Modifier,
    navigateToAddPendapatan: () -> Unit,
    onDetailClick:(Int)-> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AddPendapatanCard(
                navigateToAddPendapatan = navigateToAddPendapatan,
                modifier = Modifier.fillMaxWidth()
            )
        }
        items(pendapatanList) { pendapatan ->
            PendapatanCard(
                pendapatan = pendapatan,
                onDetailClick = onDetailClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun AddPendapatanCard(
    navigateToAddPendapatan: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(start = 100.dp, end = 100.dp)
            .height(50.dp)
            .clickable { navigateToAddPendapatan() },
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.linearGradient(
                        colors = listOf(
                            Color(0xFF5800A6),
                            Color(0xFFBD106D)
                        )
                    )
                ),
            contentAlignment = Alignment.Center,
        ) {
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
                    contentDescription = "Add Pendapatan",
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
fun PendapatanCard(
    pendapatan: Pendapatan,
    modifier: Modifier = Modifier,
    onDetailClick: (Int) -> Unit
) {
    Card(
        modifier = modifier
            .padding(start = 10.dp, end = 10.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),

    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
                .background(
                    color = Color(0xFF76D270)
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
                    ){
                        Text(
                            text = formatDateTime(
                                pendapatan.tglPendapatan
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
                ){
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
                                text = pendapatan.totalPendapatan.toString(),
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
                                text = pendapatan.catatanPendapatan,
                                style = MaterialTheme.typography.bodyLarge,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                    IconButton(
                        onClick = { onDetailClick(pendapatan.idPendapatan) },
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
fun OnError(retryAction: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Terjadi kesalahan. Coba lagi.")
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = retryAction) {
            Text(text = "Coba Lagi")
        }
    }
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
