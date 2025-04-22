package hu.piware.bricklog.feature.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import hu.piware.bricklog.feature.settings.domain.model.LanguageOption
import hu.piware.bricklog.feature.settings.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.onEach
import org.koin.compose.koinInject

val LocalLocalization = staticCompositionLocalOf { LanguageOption.SYSTEM }

@Composable
fun LocalizedApp(content: @Composable () -> Unit) {
    val settingsRepository = koinInject<SettingsRepository>()
    val localeManager = koinInject<LocaleManager>()
    var language by remember { mutableStateOf(LanguageOption.SYSTEM) }

    settingsRepository.languageOption
        .onEach { languageOption ->
            localeManager.changeLanguage(languageOption)
            language =
                languageOption // Trigger language state change only after platform specific locale is set
        }
        .collectAsStateWithLifecycle(LanguageOption.SYSTEM)

    CompositionLocalProvider(
        LocalLocalization provides language,
        content = content
    )
}

expect class LocaleManager {
    fun changeLanguage(language: LanguageOption)
}
