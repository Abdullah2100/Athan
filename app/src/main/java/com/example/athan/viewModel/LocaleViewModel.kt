package com.example.athan.viewModel

import android.app.LocaleManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.os.LocaleList
import androidx.lifecycle.ViewModel
import com.example.athan.data.local.entity.Language
import com.example.athan.data.repository.LocaleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class LocaleViewModel @Inject constructor(
    private val localeRepository: LocaleRepository,
    private val appScope: CoroutineScope,
) : ViewModel() {

    val currentContext = MutableStateFlow<Context?>(null)
    val savedLocale: StateFlow<Language?> = localeRepository.getSavedLanguage()
        .stateIn(appScope, SharingStarted.Eagerly, null)


    fun updateContext(context: Context) {
        appScope.launch(Dispatchers.IO) {
            currentContext.emit(context)
        }
    }

    suspend fun updateLocale(name: String) {
        val locale = Language(0, name)
        localeRepository.saveLanguage(locale)
    }

    fun setDefaultLocale(locale: String) {
        appScope.launch {
            localeRepository.setDefaultLocale(locale)
        }
    }

    fun whenLanguageUpdateDo(locale: String) {
        // Save the locale preference to database
        appScope.launch(Dispatchers.IO) { updateLocale(locale) }

        val context = currentContext.value ?: return
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            // Android 13+: Use the native LocaleManager API
//            // This is the official Android API for per-app language preferences
//            context.getSystemService(LocaleManager::class.java).applicationLocales =
//                LocaleList.forLanguageTags(locale)
//        } else {
        // Android 24-32: Use manual configuration update
        // Note: AppCompatDelegate.setApplicationLocales() doesn't work with ComponentActivity
        // It only works with AppCompatActivity, so we use the manual approach
        val newLocale = Locale(locale)
        Locale.setDefault(newLocale)

        val configuration: Configuration = context.resources.configuration
        configuration.setLocale(newLocale)
        configuration.setLayoutDirection(newLocale)

        context.resources.updateConfiguration(configuration, context.resources.displayMetrics)
//        }
    }

}