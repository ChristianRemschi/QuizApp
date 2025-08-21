package com.example.quizapp

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.example.quizapp.data.database.QuizDatabase
import com.example.quizapp.data.repositories.AuthStateManager
import com.example.quizapp.data.repositories.QuizRepository
import com.example.quizapp.data.repositories.SettingsRepository
import com.example.quizapp.data.repositories.ThemeRepository
import com.example.quizapp.ui.QuizViewModel
import com.example.quizapp.ui.screens.play.PlayViewModel
import com.example.quizapp.ui.screens.profile.ProfileViewModel
import com.example.quizapp.ui.screens.settings.SettingsViewModel
import com.example.quizapp.ui.screens.theme.ThemeViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")
val appModule = module {
    single { androidApplication().dataStore }

    single {
        Room.databaseBuilder(
            androidApplication(),
            QuizDatabase::class.java,
            "Quiz"
        )
            .fallbackToDestructiveMigration()
            .build()
    }


    single { SettingsRepository(get()) }

    single {
        QuizRepository(get<QuizDatabase>().QuizDAO())
    }

    single {ThemeRepository(get())}

    single { AuthStateManager(get()) }

    viewModel { SettingsViewModel(get()) }

    viewModel { QuizViewModel(get(), authStateManager = get()) }

    viewModel { ThemeViewModel(get()) }

    viewModel { PlayViewModel(get<QuizDatabase>().QuizDAO()) }

    viewModel { ProfileViewModel( get() , authStateManager = get()) }
}

