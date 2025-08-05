package hu.piware.bricklog.feature.core.presentation

import hu.piware.bricklog.feature.settings.domain.model.LanguageOption
import platform.Foundation.NSLocale
import platform.Foundation.NSUserDefaults
import platform.Foundation.countryCode
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

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

    actual fun getCurrentLocale(): String {
        val locale = NSLocale.currentLocale
        val language = locale.languageCode
        val country = locale.countryCode
        return "${language}_${country}"
    }
}
