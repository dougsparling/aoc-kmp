import SwiftUI
import ComposeApp
import CryptoKit

@main
struct iOSApp: App {
    init() {
        AocQ5_iosKt.actualMd5Hex = { (s: String) -> String in
            let digest = Insecure.MD5.hash(data: Data(s.utf8))
            return digest.map { String(format: "%02hhx", $0) }.joined()
        }
    }
    var body: some Scene {
        WindowGroup {
            ContentView()
        }
    }
}
