package pe.lecordonbleu.universidadestudiante

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import platform.Foundation.NSTimer
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication

@Composable
actual fun showToast(message: String) {
    SideEffect {
        val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
        val alertController = UIAlertController.alertControllerWithTitle(
            title = null,
            message = message,
            preferredStyle = UIAlertControllerStyleAlert
        )
        rootViewController?.presentViewController(alertController, animated = true, completion = null)
        NSTimer.scheduledTimerWithTimeInterval(2.0, repeats = false) { _ ->
            alertController.dismissViewControllerAnimated(true, completion = null)
        }
    }
}
