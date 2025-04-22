package hu.piware.bricklog.feature.core.presentation

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import hu.piware.bricklog.feature.settings.domain.model.LanguageOption

actual class LocaleManager(
    private val context: Context,
) {
    actual fun changeLanguage(language: LanguageOption) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                if (language == LanguageOption.SYSTEM) {
                    LocaleList.getEmptyLocaleList()
                } else {
                    LocaleList.forLanguageTags(language.isoFormat)
                }
        } else {
            AppCompatDelegate.setApplicationLocales(
                if (language == LanguageOption.SYSTEM) {
                    LocaleListCompat.getEmptyLocaleList()
                } else {
                    LocaleListCompat.forLanguageTags(language.isoFormat)
                }
            )
        }
    }
}
