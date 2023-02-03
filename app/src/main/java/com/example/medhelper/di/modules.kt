package com.example.medhelper.di

import com.example.medhelper.repository.RepositoryAPI
import com.example.medhelper.repository.RepositoryAPIImpl
import com.example.medhelper.ui.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainKoinModule = module {
    single<RepositoryAPI> { RepositoryAPIImpl() }
    viewModel { MainViewModel(get(), get()) }
}