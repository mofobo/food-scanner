package ch.mofobo.foodscanner.di.module

import ch.mofobo.foodscanner.features.scanner.ScannerViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule =
    module {
        viewModel {
            ScannerViewModel(
                get(),
                get()
            )
        }
    }