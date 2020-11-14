package ch.mofobo.foodscanner.di

import ch.mofobo.foodscanner.data.ProductDataRepository
import ch.mofobo.foodscanner.data.locale.LocaleProductDataSource
import ch.mofobo.foodscanner.data.locale.LocaleProductDataSourceImpl
import ch.mofobo.foodscanner.data.remote.RemoteProductDataSource
import ch.mofobo.foodscanner.data.remote.RemoteProductDataSourceImpl
import ch.mofobo.foodscanner.domain.repository.ProductRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        // Repositories
        single<ProductRepository> { return@single ProductDataRepository(get(), get()) }

        // DataSources
        single<RemoteProductDataSource> { return@single RemoteProductDataSourceImpl(get()) }
        single<LocaleProductDataSource> { return@single LocaleProductDataSourceImpl(get()) }
    }