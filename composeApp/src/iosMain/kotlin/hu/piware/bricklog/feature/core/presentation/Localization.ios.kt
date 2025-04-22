package hu.piware.bricklog.feature.core.presentation

import hu.piware.bricklog.feature.settings.domain.model.LanguageOption
import platform.Foundation.NSUserDefaults

actual class LocaleManager {
    actual fun changeLanguage(language: LanguageOption) {
        if (language == LanguageOption.SYSTEM) {
            NSUserDefaults.standardUserDefaults.removeObjectForKey("AppleLanguages")
        } else {
            NSUserDefaults.standardUserDefaults.setObject(
                arrayListOf(language.isoFormat),
                "AppleLanguages"
            )
        }
    }
}
