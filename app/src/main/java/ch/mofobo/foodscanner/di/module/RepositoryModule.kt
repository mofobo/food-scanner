package ch.mofobo.foodscanner.di.module

import ch.mofobo.foodscanner.data.ProductDataRepository
import org.koin.dsl.module

val repoModule =
    module {
        single {
            ProductDataRepository(
                get(),
                get()
            )
        }
    }