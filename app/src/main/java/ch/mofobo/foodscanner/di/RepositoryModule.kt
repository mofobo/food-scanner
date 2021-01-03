package ch.mofobo.foodscanner.di

import ch.mofobo.foodscanner.data.product.ProductDataRepository
import ch.mofobo.foodscanner.data.product.locale.LocaleProductDataSource
import ch.mofobo.foodscanner.data.product.locale.LocaleProductDataSourceImpl
import ch.mofobo.foodscanner.data.product.remote.RemoteProductDataSource
import ch.mofobo.foodscanner.data.product.remote.RemoteProductDataSourceImpl
import ch.mofobo.foodscanner.data.settings.SettingsDataRepository
import ch.mofobo.foodscanner.data.settings.locale.LocaleSettingsDataSource
import ch.mofobo.foodscanner.data.settings.locale.LocaleSettingsDataSourceImpl
import ch.mofobo.foodscanner.domain.repository.ProductRepository
import ch.mofobo.foodscanner.domain.repository.SettingsRepository
import org.koin.dsl.module

val repositoryModule =
    module {
        // Repositories
        single<ProductRepository> { return@single ProductDataRepository(get(), get()) }
        single<SettingsRepository> { return@single SettingsDataRepository(get()) }

        // DataSources
        single<RemoteProductDataSource> { return@single RemoteProductDataSourceImpl(get()) }
        single<LocaleProductDataSource> { return@single LocaleProductDataSourceImpl(get()) }

        single<LocaleSettingsDataSource> { return@single LocaleSettingsDataSourceImpl(get()) }
    }