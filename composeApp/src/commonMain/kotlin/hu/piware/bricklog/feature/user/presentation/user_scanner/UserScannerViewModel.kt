package hu.piware.bricklog.feature.user.presentation.user_scanner

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class UserScannerViewModel : ViewModel() {

    private val userRegex =
        Regex("""^([A-Za-z0-9]+):([A-Za-z0-9_]+)$""") // group1 = userId, group2 = userName

    private val _eventChannel = Channel<UserScannerEvent>()
    val eventChannel = _eventChannel.receiveAsFlow()

    fun onAction(action: UserScannerAction) {
        when (action) {
            is UserScannerAction.OnBarcodeDetected -> {
                action.detections
                    .firstNotNullOfOrNull { userRegex.matchEntire(it.data) }
                    ?.run {
                        _eventChannel.trySend(
                            UserScannerEvent.UserScanned(
                                userId = groupValues[1],
                                userName = groupValues[2],
                            ),
                        )
                    }
            }

            else -> Unit
        }
    }
}
