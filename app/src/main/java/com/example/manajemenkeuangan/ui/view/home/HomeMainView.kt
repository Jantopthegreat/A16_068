package com.example.manajemenkeuangan.ui.view.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkeuangan.R
import com.example.manajemenkeuangan.ui.navigation.DestinasiNavigasi
import com.example.manajemenkeuangan.ui.theme.groteskFont
import com.example.manajemenkeuangan.ui.theme.zodiakFont
import com.example.manajemenkeuangan.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkeuangan.ui.viewmodel.home.HomeMainUiState
import com.example.manajemenkeuangan.ui.viewmodel.home.HomeMainViewModel
import java.time.LocalTime
import kotlin.math.absoluteValue


object DestinasiHomeMain : DestinasiNavigasi {
    override val route = "homeMain"
    override val titleRes = "HomeMain"
}

@Composable
fun HomeMainView(
    onKelolaPendapatan: () -> Unit = {},
    onKelolaPengeluaran: () -> Unit = {},
    onKelolaAset: () -> Unit = {},
    onKelolaKategori: () -> Unit = {},
    homeMainViewModel: HomeMainViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val saldoUiState by homeMainViewModel.saldoUiState
    val currentTime = remember { getCurrentTimeOfDay() }
    val greeting = "Selamat $currentTime,"
    val name = "Fauzan Althaf Rianto"

    LaunchedEffect(Unit) {
        homeMainViewModel.getSaldoData()
    }

    Scaffold() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.background1),
                    contentDescription = "Background Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(bottomStart = 50.dp, bottomEnd = 50.dp)),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.TopStart
            ) {
                when (saldoUiState) {
                    is HomeMainUiState.Loading -> CircularProgressIndicator()
                    is HomeMainUiState.Success -> {
                        val totalSaldo = (saldoUiState as HomeMainUiState.Success).totalSaldo
                        val totalPendapatan = (saldoUiState as HomeMainUiState.Success).totalPendapatan
                        val totalPengeluaran = (saldoUiState as HomeMainUiState.Success).totalPengeluaran

                        val saldoColor = if (totalSaldo >= 0) {
                            Color(0xFF4CAF50)
                        } else {
                            Color(0xFFF44336)
                        }

                        Column(
                            horizontalAlignment = Alignment.Start,
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column (
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        text = greeting,
                                        fontFamily = groteskFont,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 20.sp,
                                        modifier = Modifier.padding(bottom = 4.dp)
                                    )

                                    Text(
                                        text = name,
                                        fontFamily = groteskFont,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.White,
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 25.sp,
                                        modifier = Modifier.padding(bottom = 16.dp)
                                    )
                                }
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .offset(x = (20).dp)
                                        .align(Alignment.CenterVertically)
                                )
                            }
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp),
                                modifier = Modifier
                            ) {
                                Column(
                                    verticalArrangement = Arrangement.Center,
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 10.dp)
                                ) {
                                    Text(
                                        text = "Saldo",
                                        fontFamily = groteskFont,
                                        style = MaterialTheme.typography.headlineMedium.copy(color = Color.White),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = if (totalSaldo >= 0) "+ IDR $totalSaldo" else "- IDR ${totalSaldo.absoluteValue}",
                                        color = saldoColor,
                                        fontFamily = zodiakFont,
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.displaySmall.copy(color = saldoColor),
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            Color.White,
                                            shape = RoundedCornerShape(20.dp)
                                        )
                                        .padding(10.dp)
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(100.dp)
                                                .clip(MaterialTheme.shapes.medium)
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.greencard),
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(MaterialTheme.shapes.medium),
                                                contentScale = ContentScale.Crop
                                            )
                                            Column(
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier.fillMaxSize().padding(8.dp)
                                            ) {
                                                Row (
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Center
                                                ){
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.ic_add),
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.padding(5.dp).size(20.dp)
                                                    )
                                                    Text(
                                                        text = "Pendapatan",
                                                        style = MaterialTheme.typography.headlineMedium.copy(
                                                            color = Color.White
                                                        ),
                                                        fontSize = 15.sp
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = "+ IDR $totalPendapatan",
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        color = Color.White
                                                    ),
                                                    fontSize = 20.sp
                                                )
                                            }
                                        }
                                        Box(
                                            modifier = Modifier
                                                .weight(1f)
                                                .height(100.dp)
                                                .clip(MaterialTheme.shapes.medium)
                                        ) {
                                            Image(
                                                painter = painterResource(id = R.drawable.redcard),
                                                contentDescription = "",
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    .clip(MaterialTheme.shapes.medium),
                                                contentScale = ContentScale.Crop
                                            )
                                            Column(
                                                verticalArrangement = Arrangement.Center,
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                modifier = Modifier.fillMaxSize().padding(8.dp)
                                            ) {
                                                Row (
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.Center
                                                ){
                                                    Icon(
                                                        painter = painterResource(id = R.drawable.ic_min),
                                                        contentDescription = null,
                                                        tint = Color.White,
                                                        modifier = Modifier.padding(5.dp).size(20.dp)
                                                    )
                                                    Text(
                                                        text = "Pengeluaran",
                                                        style = MaterialTheme.typography.headlineMedium.copy(
                                                            color = Color.White
                                                        ),
                                                        fontSize = 15.sp
                                                    )
                                                }
                                                Spacer(modifier = Modifier.height(8.dp))
                                                Text(
                                                    text = "- IDR $totalPengeluaran",
                                                    style = MaterialTheme.typography.bodyLarge.copy(
                                                        color = Color.White
                                                    ),
                                                    fontSize = 20.sp
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                            Column (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ){
                                Row (
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ){
                                    MenuCard(
                                        title = "Kelola Pendapatan",
                                        imageRes = R.drawable.income,
                                        onClick = onKelolaPendapatan,
                                        modifier = Modifier.weight(1f)
                                    )
                                    MenuCard(
                                        title = "Kelola Pengeluaran",
                                        imageRes = R.drawable.outcome,
                                        onClick = onKelolaPengeluaran,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                Row (
                                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ){
                                    MenuCard(
                                        title = "Kelola         Aset",
                                        imageRes = R.drawable.assets,
                                        onClick = onKelolaAset,
                                        modifier = Modifier.weight(1f)
                                    )
                                    MenuCard(
                                        title = "Kelola Kategori",
                                        imageRes = R.drawable.category,
                                        onClick = onKelolaKategori,
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                    is HomeMainUiState.Error -> Text(
                        text = "Gagal memuat data saldo",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

fun getCurrentTimeOfDay(): String {
    val hour = LocalTime.now().hour
    return when {
        hour in 5..10 -> "Pagi"
        hour in 11..14 -> "Siang"
        hour in 15..17 -> "Sore"
        else -> "Malam"
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuCard(
    title: String,
    imageRes: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .height(170.dp)
            .padding(8.dp)
            .shadow(
                elevation = 15.dp,
                shape = RoundedCornerShape(15.dp),
                ambientColor = Color.Gray,
                spotColor = Color.Black
            )
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF48008C),
                        Color(0xFFA81061)
                    )
                ),
                shape = RoundedCornerShape(15.dp)
            )
    ) {
        Card(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            colors = CardDefaults.cardColors(Color.Transparent),
            shape = RoundedCornerShape(15.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(60.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}



