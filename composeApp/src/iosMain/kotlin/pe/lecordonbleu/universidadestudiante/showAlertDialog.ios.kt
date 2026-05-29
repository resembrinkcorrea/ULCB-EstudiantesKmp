package pe.lecordonbleu.universidadestudiante

import androidx.compose.runtime.Composable
import platform.UIKit.UIAlertAction
import platform.UIKit.UIAlertActionStyleDefault
import platform.UIKit.UIAlertController
import platform.UIKit.UIAlertControllerStyleAlert
import platform.UIKit.UIApplication

@Composable
actual fun showAlertDialog(
    title: String,
    message: String,
    positiveButtonText: String,
    onPositiveAction: () -> Unit,
    icon: (@Composable (() -> Unit))?
) {
    val rootViewController = UIApplication.sharedApplication.keyWindow?.rootViewController
    val alertController = UIAlertController.alertControllerWithTitle(
        title = title,
        message = message,
        preferredStyle = UIAlertControllerStyleAlert
    )
    val positiveAction = UIAlertAction.actionWithTitle(positiveButtonText, UIAlertActionStyleDefault) { _ ->
        onPositiveAction()
    }
    alertController.addAction(positiveAction)
    rootViewController?.presentViewController(alertController, animated = true, completion = null)
}
