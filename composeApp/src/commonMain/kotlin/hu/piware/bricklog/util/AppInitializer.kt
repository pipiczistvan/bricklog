package hu.piware.bricklog.util

import co.touchlab.kermit.Logger
import com.mmk.kmpnotifier.notification.NotifierManager
import hu.piware.bricklog.di.initKoin
import org.koin.dsl.KoinAppDeclaration

object AppInitializer {

    private val logger = Logger.withTag("AppInitializer")

    fun initialize(config: KoinAppDeclaration? = null) {
        initKoin(config)

        NotifierManager.addListener(object : NotifierManager.Listener {
            override fun onNewToken(token: String) {
                logger.d("FirebaseOnNewToken: $token")
            }
        })
    }
}
