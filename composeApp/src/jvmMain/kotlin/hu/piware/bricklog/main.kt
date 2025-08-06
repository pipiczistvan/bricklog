package hu.piware.bricklog

import android.app.Application
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import bricklog.composeapp.generated.resources.Res
import bricklog.composeapp.generated.resources.app_name
import com.google.firebase.FirebasePlatform
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import hu.piware.bricklog.feature.core.data.datastore.createDataStore
import hu.piware.bricklog.util.AppInitializer
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import org.jetbrains.compose.resources.stringResource
import java.io.File

object MyFirebasePlatform : FirebasePlatform() {
    val storage = createDataStore { "bricklog-firebase.preferences_pb" }
    override fun store(key: String, value: String) {
        runBlocking {
            storage.edit { preferences ->
                preferences[stringPreferencesKey(key)] = value
            }
        }
    }

    override fun retrieve(key: String): String? {
        return runBlocking {
            val preferences = storage.data.firstOrNull()
            preferences?.get(stringPreferencesKey(key))
        }
    }

    override fun clear(key: String) {
        runBlocking {
            storage.edit { preferences ->
                preferences.remove(stringPreferencesKey(key))
            }
        }
    }

    override fun log(msg: String) = println(msg)
    override fun getDatabasePath(name: String): File {
        // TODO: duplicated code
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir = when {
            os.contains("win") -> File(System.getenv("APPDATA"), "Bricklog")
            os.contains("mac") -> File(userHome, "Library/Application Support/Bricklog")
            else -> File(userHome, ".local/share/Bricklog")
        }
        return File(appDataDir, name)
    }
}

fun main() = application {
    if (AppInitializer.shouldInitializeFirebase()) {
        FirebasePlatform.initializeFirebasePlatform(MyFirebasePlatform)

        val options = FirebaseOptions(
            projectId = BuildKonfig.FIREBASE_PROJECT_ID,
            applicationId = BuildKonfig.FIREBASE_WEB_APP_ID,
            apiKey = BuildKonfig.FIREBASE_WEB_API_KEY
        )

        Firebase.initialize(Application(), options)
    }

    AppInitializer.initialize()
    Window(
        onCloseRequest = ::exitApplication,
        title = stringResource(Res.string.app_name),
    ) {
        App()
    }
}
