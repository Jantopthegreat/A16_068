package com.example.manajemenkeuangan.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.manajemenkeuangan.ui.view.aset.AsetDetailView
import com.example.manajemenkeuangan.ui.view.aset.AsetEditView
import com.example.manajemenkeuangan.ui.view.aset.AsetHomeView
import com.example.manajemenkeuangan.ui.view.aset.AsetInsertView
import com.example.manajemenkeuangan.ui.view.aset.DestinasiDetailAset
import com.example.manajemenkeuangan.ui.view.aset.DestinasiEditAset
import com.example.manajemenkeuangan.ui.view.aset.DestinasiHomeAset
import com.example.manajemenkeuangan.ui.view.aset.DestinasiInsertAset
import com.example.manajemenkeuangan.ui.view.home.DestinasiHomeMain
import com.example.manajemenkeuangan.ui.view.home.HomeMainView
import com.example.manajemenkeuangan.ui.view.kategori.DestinasiDetailKategori
import com.example.manajemenkeuangan.ui.view.kategori.DestinasiEditKategori
import com.example.manajemenkeuangan.ui.view.kategori.DestinasiHomeKategori
import com.example.manajemenkeuangan.ui.view.kategori.DestinasiInsertKategori
import com.example.manajemenkeuangan.ui.view.kategori.KategoriDetailView
import com.example.manajemenkeuangan.ui.view.kategori.KategoriEditView
import com.example.manajemenkeuangan.ui.view.kategori.KategoriHomeView
import com.example.manajemenkeuangan.ui.view.kategori.KategoriInsertView
import com.example.manajemenkeuangan.ui.view.pendapatan.DestinasiDetail
import com.example.manajemenkeuangan.ui.view.pendapatan.DestinasiDetail.idPendapatan
import com.example.manajemenkeuangan.ui.view.pendapatan.DestinasiEditPendapatan
import com.example.manajemenkeuangan.ui.view.pendapatan.DestinasiHomePendapatan
import com.example.manajemenkeuangan.ui.view.pendapatan.DestinasiInsertPendapatan
import com.example.manajemenkeuangan.ui.view.pendapatan.EntryPndScreen
import com.example.manajemenkeuangan.ui.view.pendapatan.HomePendapatanView
import com.example.manajemenkeuangan.ui.view.pendapatan.PendapatanDetailView
import com.example.manajemenkeuangan.ui.view.pendapatan.PendapatanEditView
import com.example.manajemenkeuangan.ui.view.pengeluaran.DestinasiDetailPengeluaran
import com.example.manajemenkeuangan.ui.view.pengeluaran.DestinasiEditPengeluaran
import com.example.manajemenkeuangan.ui.view.pengeluaran.DestinasiHomePengeluaran
import com.example.manajemenkeuangan.ui.view.pengeluaran.DestinasiInsertPengeluaran
import com.example.manajemenkeuangan.ui.view.pengeluaran.PengeluaranDetailView
import com.example.manajemenkeuangan.ui.view.pengeluaran.PengeluaranEditView
import com.example.manajemenkeuangan.ui.view.pengeluaran.PengeluaranHomeView
import com.example.manajemenkeuangan.ui.view.pengeluaran.PengeluaranInsertView


@Composable
fun PengelolaHalaman(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
){
    NavHost(
        navController = navController,
        startDestination = DestinasiHomeMain.route,
        modifier = Modifier
    ) {
        composable(
            route = DestinasiHomeMain.route
        ) {
            HomeMainView(
                onKelolaPendapatan = { navController.navigate(DestinasiHomePendapatan.route) },
                onKelolaPengeluaran = { navController.navigate(DestinasiHomePengeluaran.route) },
                onKelolaAset = {navController.navigate(DestinasiHomeAset.route) },
                onKelolaKategori = {navController.navigate(DestinasiHomeKategori.route) }
            )
        }
        composable(
            route = DestinasiHomePendapatan.route
        ) {
            HomePendapatanView(
                modifier = Modifier,
                navigateToAddPendapatan = { navController.navigate(DestinasiInsertPendapatan.route) },
                onBack = {navController.popBackStack()},
                onDetailClick = { idPendapatan ->
                    navController.navigate("${DestinasiDetail.route}/$idPendapatan")
                    println(idPendapatan)
                }

            )
        }
        composable(
            route = DestinasiDetail.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetail.idPendapatan) {
                type = NavType.StringType
            })
        ) { backStackEntry ->
            val idPendapatan = backStackEntry.arguments?.getString(DestinasiDetail.idPendapatan)
            idPendapatan?.let {
                PendapatanDetailView(
                    navigateBack = {
                        navController.popBackStack()
                    },
                    onBack = {navController.popBackStack()},
                    onEditClick = { navController.navigate("${DestinasiEditPendapatan.route}/$idPendapatan") }
                )
            }
        }
        composable(
            route = DestinasiInsertPendapatan.route
        ) {
            EntryPndScreen(
                onBack = {navController.popBackStack()},

                navigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = DestinasiEditPendapatan.routeWithArgs,
            arguments = listOf(navArgument(DestinasiEditPendapatan.idPendapatan) {
                type = NavType.StringType
            })){
            val idPendapatan = it.arguments?.getString(DestinasiEditPendapatan.idPendapatan)
            idPendapatan?.let {
                PendapatanEditView(
                    navigateBack = { navController.navigateUp() },
                            onBack = {navController.popBackStack()}

                    )
            }

        }

        // ASET

        composable(
            DestinasiHomeAset.route) {
            AsetHomeView(
                modifier = Modifier,
                navigateToAddAset = { navController.navigate(DestinasiInsertAset.route) },
                onDetailClick = { idAset ->
                    navController.navigate("${DestinasiDetailAset.route}/$idAset")
                    println(idAset)
                },
                onBack = {navController.popBackStack()}
                )
        }
        composable(
            route = DestinasiInsertAset.route
        ) {
            AsetInsertView(
                navigateBack = { navController.navigateUp() },
                modifier = Modifier,
                onBack = {navController.popBackStack()}
            )
        }
        composable(
            route = DestinasiEditAset.routeWithArgs,
            arguments = listOf(navArgument(DestinasiEditAset.idAset) {
                type = NavType.StringType
            }
            )
        ) {
            val idAset = it.arguments?.getString(DestinasiEditAset.idAset)
            idAset?.let {
                AsetEditView(
                    navigateBack = { navController.navigateUp() },
                    onBack = {navController.popBackStack()}


                )
            }
        }
        composable(
            route = DestinasiDetailAset.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetailAset.idAset) {
                type = NavType.StringType
            })){
            val idAset = it.arguments?.getString(DestinasiDetailAset.idAset)
            idAset?.let {
                AsetDetailView(
                    navigateBack = {
                        navController.popBackStack()
                    },
                    onEditClick = {
                        navController.navigate("${DestinasiEditAset.route}/$idAset")
                    },
                    onBack = {navController.popBackStack()}
                )

            }
        }
        // Pengeluaran

        // Pengeluaran Routes
        composable(
            route = DestinasiHomePengeluaran.route
        ) {
            PengeluaranHomeView(
                modifier = Modifier,
                navigateToAddPengeluaran = { navController.navigate(DestinasiInsertPengeluaran.route) },
                onBack = {navController.popBackStack()},

                onDetailClick = { idPengeluaran ->
                    navController.navigate("${DestinasiDetailPengeluaran.route}/$idPengeluaran")
                    println(idPengeluaran)
                }
            )
        }

        composable(
            route = DestinasiInsertPengeluaran.route
        ) {
            PengeluaranInsertView(
                onBack = {navController.popBackStack()},

                navigateBack = { navController.navigateUp() }
            )
        }

        composable(
            route = DestinasiEditPengeluaran.routeWithArgs,
            arguments = listOf(navArgument(DestinasiEditPengeluaran.idPengeluaran) {
                type = NavType.StringType
            })
        ) {
            val idPengeluaran = it.arguments?.getString(DestinasiEditPengeluaran.idPengeluaran)
            idPengeluaran?.let {
                PengeluaranEditView(
                    onBack = {navController.popBackStack()},

                    navigateBack = { navController.popBackStack() }
                )
            }
        }

        composable(
            route = DestinasiDetailPengeluaran.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetailPengeluaran.idPengeluaran) {
                type = NavType.StringType
            })
        ) {
            val idPengeluaran = it.arguments?.getString(DestinasiDetailPengeluaran.idPengeluaran)
            idPengeluaran?.let {
                PengeluaranDetailView(
                    navigateBack = { navController.popBackStack() },
                    onBack = {navController.popBackStack()},
                    onEditClick = { navController.navigate("${DestinasiEditPengeluaran.route}/$idPengeluaran") }
                )
            }
        }
        composable(
            DestinasiHomeKategori.route
        ) {
            KategoriHomeView(
                modifier = Modifier,
                navigateToAddKategori = { navController.navigate(DestinasiInsertKategori.route) },
                onBack = {navController.popBackStack()},
                onDetailClick = { idKategori ->
                    navController.navigate("${DestinasiDetailKategori.route}/$idKategori")
                }
            )
        }

        composable(
            route = DestinasiInsertKategori.route
        ) {
            KategoriInsertView(
                navigateBack = { navController.navigateUp() },
                onBack = {navController.popBackStack()},
                modifier = Modifier,
            )
        }

        composable(
            route = DestinasiEditKategori.routeWithArgs,
            arguments = listOf(navArgument(DestinasiEditKategori.idKategori) {
                type = NavType.StringType
            })
        ) {
            val idKategori = it.arguments?.getString(DestinasiEditKategori.idKategori)
            idKategori?.let {
                KategoriEditView(
                    navigateBack = { navController.popBackStack() },
                    onBack = {navController.popBackStack()}

                )
            }
        }

        composable(
            route = DestinasiDetailKategori.routeWithArgs,
            arguments = listOf(navArgument(DestinasiDetailKategori.idKategori) {
                type = NavType.StringType
            })
        ) {
            val idKategori = it.arguments?.getString(DestinasiDetailKategori.idKategori)
            idKategori?.let {
                KategoriDetailView(
                    navigateBack = { navController.popBackStack() },
                    onEditClick = { navController.navigate("${DestinasiEditKategori.route}/$idKategori") },
                    onBack = {navController.popBackStack()}
                )
            }
        }
    }
}