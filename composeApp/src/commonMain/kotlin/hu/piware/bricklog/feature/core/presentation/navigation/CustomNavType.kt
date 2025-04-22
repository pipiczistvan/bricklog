package hu.piware.bricklog.feature.core.presentation.navigation

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import com.eygraber.uri.UriCodec
import hu.piware.bricklog.feature.set.presentation.set_detail.SetDetailArguments
import hu.piware.bricklog.feature.set.presentation.set_image.SetImageArguments
import hu.piware.bricklog.feature.set.presentation.set_list.SetListArguments
import hu.piware.bricklog.feature.set.presentation.theme_multi_select.ThemeMultiSelectArguments
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json

object CustomNavType {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    class JsonEncodedNavType<T>(private val serializer: KSerializer<T>) :
        NavType<T>(isNullableAllowed = false) {

        override fun get(bundle: SavedState, key: String): T? {
            return bundle.read {
                if (!contains(key) || isNull(key)) null else json.decodeFromString(
                    serializer,
                    getString(key)
                )
            }
        }

        override fun parseValue(value: String): T {
            return json.decodeFromString(serializer, value)
        }

        override fun serializeAsValue(value: T): String {
            return UriCodec.encode(json.encodeToString(serializer, value))
        }

        override fun put(bundle: SavedState, key: String, value: T) {
            bundle.write { putString(key, json.encodeToString(serializer, value)) }
        }
    }

    val SetListArgumentsType = JsonEncodedNavType(SetListArguments.serializer())
    val SetDetailArgumentsType = JsonEncodedNavType(SetDetailArguments.serializer())
    val SetImageArgumentsType = JsonEncodedNavType(SetImageArguments.serializer())
    val ThemeMultiSelectArgumentsType = JsonEncodedNavType(ThemeMultiSelectArguments.serializer())
}
