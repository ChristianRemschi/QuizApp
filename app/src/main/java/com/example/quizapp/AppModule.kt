package com.example.quizapp

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.quizapp.data.repositories.ThemeRepository
import com.example.quizapp.ui.screens.theme.ThemeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore
        by preferencesDataStore("theme")
val appModule = module {
    single { get<Context>().dataStore }
    single { ThemeRepository(get()) }
    viewModel { ThemeViewModel(get()) }
}