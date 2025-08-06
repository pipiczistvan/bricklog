package hu.piware.bricklog.feature.core.data.database

import androidx.room.Room
import androidx.room.RoomDatabase
import java.io.File

actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<BricklogDatabase> {
        val os = System.getProperty("os.name").lowercase()
        val userHome = System.getProperty("user.home")
        val appDataDir = when {
            os.contains("win") -> File(System.getenv("APPDATA"), "Bricklog")
            os.contains("mac") -> File(userHome, "Library/Application Support/Bricklog")
            else -> File(userHome, ".local/share/Bricklog")
        }

        if (!appDataDir.exists()) {
            appDataDir.mkdirs()
        }

        val dbFile = File(appDataDir, BricklogDatabase.DB_NAME)
        return Room.databaseBuilder(dbFile.absolutePath)
    }
}
