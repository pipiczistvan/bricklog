package hu.piware.bricklog.feature.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

actual class DatastoreFactory {
    actual fun create(): DataStore<Preferences> {
        return createDataStore {
            DATA_STORE_FILE_NAME
        }
    }
}
