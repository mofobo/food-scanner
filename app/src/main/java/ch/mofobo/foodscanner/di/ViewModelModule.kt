package ch.mofobo.foodscanner.di

import ch.mofobo.foodscanner.features.about.AboutViewModel
import ch.mofobo.foodscanner.features.common.SharedViewModel
import ch.mofobo.foodscanner.features.details.DetailsViewModel
import ch.mofobo.foodscanner.features.history.HistoryViewModel
import ch.mofobo.foodscanner.features.scanner.ScannerViewModel
import ch.mofobo.foodscanner.features.scanner.camera.CameraViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel { ScannerViewModel() }
        viewModel { HistoryViewModel(get()) }

        viewModel { CameraViewModel() }
        viewModel { DetailsViewModel(get(), get()) }
        viewModel { AboutViewModel() }

    }