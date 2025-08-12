package com.example.quizapp

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.quizapp.data.database.QuizDatabase
import com.example.quizapp.data.repositories.QuizRepository
import com.example.quizapp.data.repositories.SettingsRepository
import com.example.quizapp.data.repositories.ThemeRepository
import com.example.quizapp.ui.QuizViewModel
import com.example.quizapp.ui.screens.settings.SettingsViewModel
import com.example.quizapp.ui.screens.theme.ThemeViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore by preferencesDataStore("settings")
val appModule = module {
    single { get<Context>().dataStore }

    single {
        Room.databaseBuilder(
            get(),
            QuizDatabase::class.java,
            "Quiz"
        )
            .build()
    }


    single { SettingsRepository(get()) }

    single {
        QuizRepository(get<QuizDatabase>().QuizDAO(), get<Context>().contentResolver)
    }

    single {ThemeRepository(get())}

    viewModel { SettingsViewModel(get()) }

    viewModel { QuizViewModel(get()) }

    viewModel { ThemeViewModel(get()) }
}