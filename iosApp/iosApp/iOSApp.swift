import SwiftUI
import FirebaseCore
import FirebaseMessaging
import ComposeApp
import GoogleSignIn

class AppDelegate: NSObject, UIApplicationDelegate {
    
    func application(_ application: UIApplication, didFinishLaunchingWithOptions launchOptions: [UIApplication.LaunchOptionsKey : Any]? = nil) -> Bool {
        if (AppInitializer.shared.shouldInitializeFirebase()) {
            FirebaseApp.configure()

            // Google Sign-In setup
            guard let clientID = FirebaseApp.app()?.options.clientID else {
                return false
            }
            let config = GIDConfiguration(clientID: clientID)
            GIDSignIn.sharedInstance.configuration = config
        }

        //By default showPushNotification value is true.
        //When set showPushNotification to false foreground push  notification will not be shown.
        //You can still get notification content using #onPushNotification listener method.
        NotifierManager.shared.initialize(configuration: NotificationPlatformConfigurationIos(
                showPushNotification: true,
                askNotificationPermissionOnStart: false,
                notificationSoundName: nil
            )
        )
        
        AppInitializer.shared.initialize()
        
        return true;
    }
    
    func application(_ application: UIApplication, didReceiveRemoteNotification userInfo: [AnyHashable : Any]) async -> UIBackgroundFetchResult {
        NotifierManager.shared.onApplicationDidReceiveRemoteNotification(userInfo: userInfo)
        return UIBackgroundFetchResult.newData
    }
    
    func application(_ application: UIApplication, didRegisterForRemoteNotificationsWithDeviceToken deviceToken: Data) {
        Messaging.messaging().apnsToken = deviceToken
    }
}

@main
struct iOSApp: SwiftUI.App {
    
    @UIApplicationDelegateAdaptor(AppDelegate.self) var delegate
    
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
