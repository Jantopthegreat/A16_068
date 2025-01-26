package com.example.manajemenkeuangan

import android.app.Application
import com.example.manajemenkeuangan.di.AppContainer
import com.example.manajemenkeuangan.di.ManajemenUangContainerApp


class ManajemenKeuanganApp : Application() {

        lateinit var container: AppContainer

        override fun onCreate() {
            super.onCreate()
            container = ManajemenUangContainerApp()
        }
    }
