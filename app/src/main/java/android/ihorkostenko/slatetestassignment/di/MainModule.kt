package android.ihorkostenko.slatetestassignment.di

import android.ihorkostenko.slatetestassignment.ui.viewmodels.MainViewModel
import org.koin.android.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val mainModule = module {
    viewModel { MainViewModel(get(), get(), get(), get()) }
}

