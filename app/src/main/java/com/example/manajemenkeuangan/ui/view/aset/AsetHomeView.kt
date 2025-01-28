package com.example.manajemenkeuangan.ui.view.aset

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.manajemenkeuangan.R
import com.example.manajemenkeuangan.model.Aset
import com.example.manajemenkeuangan.ui.customwidget.TopAppBar
import com.example.manajemenkeuangan.ui.navigation.DestinasiNavigasi
import com.example.manajemenkeuangan.ui.viewmodel.PenyediaViewModel
import com.example.manajemenkeuangan.ui.viewmodel.aset.HomeAsetUiState
import com.example.manajemenkeuangan.ui.viewmodel.aset.HomeAsetViewModel


object DestinasiHomeAset : DestinasiNavigasi {
    override val route = "homeAset"
    override val titleRes = "Aset"
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AsetHomeView(
    navigateToAddAset: () -> Unit,
    modifier: Modifier = Modifier,
    onDetailClick: (Int) -> Unit,
    onBack: () -> Unit,
    viewModel: HomeAsetViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val homeAsetUiState = viewModel.asetUiState

LaunchedEffect (Unit){
    viewModel.getAset()
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
                judul = "Home Aset")
        }    ) { innerPadding ->
        AsetStatus(
            homeAsetUiState = homeAsetUiState,
            retryAction = { viewModel.getAset() },
            modifier = Modifier.padding(innerPadding),
            navigateToAddAset = navigateToAddAset,
            onDetailClick = onDetailClick
        )
    }
}


@Composable
fun AsetStatus(
    homeAsetUiState: HomeAsetUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToAddAset: () -> Unit,
    onDetailClick: (Int) -> Unit
) {
    when (homeAsetUiState) {
        is HomeAsetUiState.Loading -> OnLoading(modifier = modifier.fillMaxSize())
        is HomeAsetUiState.Success -> {
            AsetLayout(
                asetList = homeAsetUiState.aset,
                modifier = modifier,
                navigateToAddAset = navigateToAddAset,
                onDetailClick = onDetailClick
            )
        }
        is HomeAsetUiState.Error -> OnError(retryAction, modifier = modifier.fillMaxSize())
    }
}


@Composable
fun AsetLayout(
    asetList: List<Aset>,
    modifier: Modifier = Modifier,
    navigateToAddAset: () -> Unit,
    onDetailClick: (Int) -> Unit
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            AddAsetCard(
                navigateToAddAset = navigateToAddAset,
                modifier = Modifier.fillMaxWidth()
            )
        }
        items(asetList) { aset ->
            AsetCard(
                aset = aset,
                modifier = Modifier.fillMaxWidth(),
                onDetailClick = onDetailClick
            )
        }
    }
}

@Composable
fun AddAsetCard(
    navigateToAddAset: () -> Unit,
    modifier: Modifier = Modifier
) {

    Card(
        modifier = modifier
            .padding(start = 100.dp, end = 100.dp)
            .height(50.dp)
            .clickable { navigateToAddAset() },
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
                    contentDescription = "Add Aset",
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
fun AsetCard(
    aset: Aset,
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
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = " ${aset.namaAset}",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.White
                    )
                    IconButton(
                        onClick = { onDetailClick(aset.idAset) }
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
