package ch.mofobo.foodscanner.di.module

import ch.mofobo.foodscanner.features.history.HistoryViewModel
import ch.mofobo.foodscanner.features.home.HomeViewModel
import ch.mofobo.foodscanner.features.scanner.ScannerViewModel
import ch.mofobo.foodscanner.features.scanner.camera.CameraViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel { HomeViewModel() }
        viewModel { ScannerViewModel(get(), get()) }
        viewModel { HistoryViewModel() }

        viewModel {CameraViewModel() }
    }