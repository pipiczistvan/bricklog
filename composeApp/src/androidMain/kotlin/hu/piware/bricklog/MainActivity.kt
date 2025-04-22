@file:OptIn(ExperimentalComposeUiApi::class)

package hu.piware.bricklog

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.firebase.FirebaseApp
import com.mmk.kmpnotifier.extensions.onCreateOrOnNewIntent
import com.mmk.kmpnotifier.notification.NotifierManager
import dev.icerock.moko.permissions.PermissionsController
import hu.piware.bricklog.feature.settings.domain.model.ThemeOption
import hu.piware.bricklog.feature.settings.domain.usecase.WatchThemeOption
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

val DefaultLightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)
val DefaultDarkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)

class MainActivity : ComponentActivity() {

    private val permissionsController: PermissionsController by inject()
    private val watchThemeOption: WatchThemeOption by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        NotifierManager.onCreateOrOnNewIntent(intent)
        installSplashScreen()
            .setKeepOnScreenCondition { !App.firstScreenLoaded }
        setContent {
            App(
                modifier = Modifier
                    .semantics { testTagsAsResourceId = true }
            )
        }

        watchThemeOption()
            .onEach { themeOption ->
                when (themeOption) {
                    ThemeOption.SYSTEM -> enableEdgeToEdge()
                    ThemeOption.LIGHT -> enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.light(
                            DefaultLightScrim,
                            DefaultDarkScrim
                        )
                    )

                    ThemeOption.DARK -> enableEdgeToEdge(
                        statusBarStyle = SystemBarStyle.dark(
                            DefaultLightScrim
                        )
                    )
                }

            }
            .launchIn(lifecycleScope)

        permissionsController.bind(this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        NotifierManager.onCreateOrOnNewIntent(intent)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
