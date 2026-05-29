package pe.lecordonbleu.universidadestudiante

import platform.Foundation.NSURL
import platform.SafariServices.SFSafariViewController
import platform.SafariServices.SFSafariViewControllerDismissButtonStyle
import platform.UIKit.*

actual fun openMicrosoftPasswordChange(platformContext: Any?) {
    val viewController = platformContext as? UIViewController ?: return

    val url = NSURL(string = "https://account.activedirectory.windowsazure.com/changePassword.aspx")
        ?: return

    val safariVC = SFSafariViewController(uRL = url)

    safariVC.modalPresentationStyle = UIModalPresentationFormSheet
    safariVC.preferredControlTintColor = UIColor.systemBlueColor
    safariVC.dismissButtonStyle =
        SFSafariViewControllerDismissButtonStyle.SFSafariViewControllerDismissButtonStyleClose

    viewController.presentViewController(safariVC, animated = true, completion = null)
}
