package hu.piware.bricklog.feature.core.presentation

import hu.piware.bricklog.feature.settings.domain.model.LanguageOption
import java.util.Locale

actual class LocaleManager {
    actual fun changeLanguage(language: LanguageOption) {
        // TODO: Not working with system language
//        val locale = Locale.of(language.isoFormat)
//        Locale.setDefault(locale)
    }

    actual fun getCurrentLocale(): String {
        val locale = Locale.getDefault()
        return "${locale.language}_${locale.country}"
    }
}
