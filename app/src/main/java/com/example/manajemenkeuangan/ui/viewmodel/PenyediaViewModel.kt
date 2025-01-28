package com.example.manajemenkeuangan.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.manajemenkeuangan.ManajemenKeuanganApp
import com.example.manajemenkeuangan.ui.viewmodel.aset.AsetDetailViewModel
import com.example.manajemenkeuangan.ui.viewmodel.aset.AsetEditViewModel
import com.example.manajemenkeuangan.ui.viewmodel.aset.AsetInsertViewModel
import com.example.manajemenkeuangan.ui.viewmodel.aset.HomeAsetViewModel
import com.example.manajemenkeuangan.ui.viewmodel.home.HomeMainViewModel
import com.example.manajemenkeuangan.ui.viewmodel.kategori.HomeKategoriViewModel
import com.example.manajemenkeuangan.ui.viewmodel.kategori.KategoriDetailViewModel
import com.example.manajemenkeuangan.ui.viewmodel.kategori.KategoriEditViewModel
import com.example.manajemenkeuangan.ui.viewmodel.kategori.KategoriInsertViewModel
import com.example.manajemenkeuangan.ui.viewmodel.pendapatan.HomePendapatanViewModel
import com.example.manajemenkeuangan.ui.viewmodel.pendapatan.PendapatanDetailViewModel
import com.example.manajemenkeuangan.ui.viewmodel.pendapatan.PendapatanEditViewModel
import com.example.manajemenkeuangan.ui.viewmodel.pendapatan.PendapatanInsertViewModel
import com.example.manajemenkeuangan.ui.viewmodel.pengeluaran.PengeluaranDetailViewModel
import com.example.manajemenkeuangan.ui.viewmodel.pengeluaran.PengeluaranEditViewModel
import com.example.manajemenkeuangan.ui.viewmodel.pengeluaran.PengeluaranHomeViewModel
import com.example.manajemenkeuangan.ui.viewmodel.pengeluaran.PengeluaranInsertViewModel

object PenyediaViewModel {
    val Factory = viewModelFactory {

        //Home Main
        initializer { HomeMainViewModel(
                manajemenUangApp().container.saldoRepository,
                manajemenUangApp().container.pendapatanRepository,
                manajemenUangApp().container.pengeluaranRepository
            )
        }

        //Pendapatan
        initializer { HomePendapatanViewModel(manajemenUangApp().container.pendapatanRepository) }
        initializer { PendapatanInsertViewModel(manajemenUangApp().container.pendapatanRepository) }
        initializer { PendapatanDetailViewModel(createSavedStateHandle(), manajemenUangApp().container.pendapatanRepository) }
        initializer {
            PendapatanEditViewModel(
                createSavedStateHandle(),
                manajemenUangApp().container.pendapatanRepository,
                HomeAsetViewModel(manajemenUangApp().container.asetRepository),
                HomeKategoriViewModel(manajemenUangApp().container.kategoriRepository)
            )
        }



        //Aset
        initializer { HomeAsetViewModel(manajemenUangApp().container.asetRepository) }
        initializer { AsetInsertViewModel(manajemenUangApp().container.asetRepository) }
        initializer { AsetDetailViewModel(createSavedStateHandle(), manajemenUangApp().container.asetRepository) }
        initializer { AsetEditViewModel(createSavedStateHandle(), manajemenUangApp().container.asetRepository) }

        //Pengeluaran
        initializer { PengeluaranHomeViewModel(manajemenUangApp().container.pengeluaranRepository , manajemenUangApp().container.saldoRepository) }
        initializer { PengeluaranDetailViewModel(createSavedStateHandle(), manajemenUangApp().container.pengeluaranRepository) }
        initializer { PengeluaranEditViewModel(createSavedStateHandle(), manajemenUangApp().container.pengeluaranRepository) }
        initializer { PengeluaranInsertViewModel(manajemenUangApp().container.pengeluaranRepository) }

        //Kategori
        initializer { HomeKategoriViewModel(manajemenUangApp().container.kategoriRepository) }
        initializer { KategoriInsertViewModel(manajemenUangApp().container.kategoriRepository) }
        initializer { KategoriDetailViewModel(createSavedStateHandle(), manajemenUangApp().container.kategoriRepository) }
        initializer { KategoriEditViewModel(createSavedStateHandle(), manajemenUangApp().container.kategoriRepository) }

    }
}

fun CreationExtras.manajemenUangApp(): ManajemenKeuanganApp =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as ManajemenKeuanganApp)