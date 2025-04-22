package hu.piware.bricklog.feature.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual class DatastoreFactory(
    private val context: Context
) {
    actual fun create(): DataStore<Preferences> {
        return createDataStore {
            context.filesDir.resolve(DATA_STORE_FILE_NAME).absolutePath
        }
    }
}
