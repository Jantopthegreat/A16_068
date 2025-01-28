package com.example.manajemenkeuangan.ui.view.kategori

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkeuangan.R
import com.example.manajemenkeuangan.model.Kategori
import com.example.manajemenkeuangan.ui.customwidget.TopAppBar
import com.example.manajemenkeuangan.ui.navigation.DestinasiNavigasi
import com.example.manajemenkeuangan.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkeuangan.ui.viewmodel.kategori.HomeKategoriUiState
import com.example.manajemenkeuangan.ui.viewmodel.kategori.HomeKategoriViewModel


object DestinasiHomeKategori : DestinasiNavigasi {
    override val route = "homeKategori"
    override val titleRes = "Kategori"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KategoriHomeView(
    navigateToAddKategori: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: HomeKategoriViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeKategoriUiState = viewModel.kategoriUiState

    Scaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()
            .padding(top = 18.dp),
        topBar = {
            TopAppBar(
                onBack = onBack,
                onLogoClick = {},
                showBackButton = true,
                judul = "Home Kategori")
        }
    ) { innerPadding ->
        KategoriStatus(
            homeKategoriUiState = homeKategoriUiState,
            retryAction = { viewModel.getKategori() },
            modifier = Modifier.padding(innerPadding),
            navigateToAddKategori = navigateToAddKategori,
            onDetailClick = onDetailClick
        )
    }
}


@Composable
fun KategoriStatus(
    homeKategoriUiState: HomeKategoriUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToAddKategori: () -> Unit,
    onDetailClick: (Int) -> Unit
) {
    when (homeKategoriUiState) {
        is HomeKategoriUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeKategoriUiState.Success -> {
            KategoriLayout(
                kategoriList = homeKategoriUiState.kategori,
                modifier = modifier,
                navigateToAddKategori = navigateToAddKategori,
                onDetailClick = onDetailClick
            )
        }
        is HomeKategoriUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}


@Composable
fun KategoriLayout(
    kategoriList: List<Kategori>,
    modifier: Modifier = Modifier,
    navigateToAddKategori: () -> Unit,
    onDetailClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AddKategoriCard(
                navigateToAddKategori = navigateToAddKategori,
                modifier = Modifier.fillMaxWidth()
            )
        }
        items(kategoriList) { kategori ->
            KategoriCard(
                kategori = kategori,
                modifier = Modifier.fillMaxWidth(),
                onDetailClick = onDetailClick
            )
        }
    }
}

@Composable
fun AddKategoriCard(
    navigateToAddKategori: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .padding(start = 100.dp, end = 100.dp)
            .height(50.dp)
            .clickable { navigateToAddKategori() },
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
                    contentDescription = "Add Kategori",
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
fun KategoriCard(
    kategori: Kategori,
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
                    color = Color(0xFF5800A6)
                ),
            contentAlignment = Alignment.Center,
        ){
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = " ${kategori.namaKategori}",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    IconButton(
                        onClick = { onDetailClick(kategori.idKategori) }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.eye),
                            contentDescription = "",
                            modifier = Modifier.size(24.dp)
                                .align(Alignment.CenterVertically),
                            tint = Color.White
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
