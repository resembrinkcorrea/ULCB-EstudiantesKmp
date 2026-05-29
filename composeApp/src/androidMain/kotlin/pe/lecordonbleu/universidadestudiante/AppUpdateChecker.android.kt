package pe.lecordonbleu.universidadestudiante

import androidx.activity.ComponentActivity
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability

private const val REQ_CODE_VERSION_UPDATE = 530

@Composable
actual fun AppUpdateChecker() {
    val context = LocalContext.current
    val activity = context as? ComponentActivity ?: return

    var showInstallDialog by remember { mutableStateOf(false) }
    val appUpdateManager = remember { AppUpdateManagerFactory.create(context) }

    val installStateListener = remember {
        InstallStateUpdatedListener { state ->
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                showInstallDialog = true
            }
        }
    }

    if (showInstallDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Actualización lista") },
            text = { Text("La nueva versión está descargada. Instala y reinicia la app.") },
            confirmButton = {
                TextButton(onClick = {
                    appUpdateManager.completeUpdate()
                    showInstallDialog = false
                }) {
                    Text("INSTALAR")
                }
            }
        )
    }

    DisposableEffect(Unit) {
        appUpdateManager.registerListener(installStateListener)

        appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
            when {
                info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                        info.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE) -> {
                    appUpdateManager.startUpdateFlowForResult(
                        info, AppUpdateType.FLEXIBLE, activity, REQ_CODE_VERSION_UPDATE
                    )
                }
                info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                        info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) -> {
                    appUpdateManager.startUpdateFlowForResult(
                        info, AppUpdateType.IMMEDIATE, activity, REQ_CODE_VERSION_UPDATE
                    )
                }
                info.installStatus() == InstallStatus.DOWNLOADED -> {
                    showInstallDialog = true
                }
                info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                    appUpdateManager.startUpdateFlowForResult(
                        info, AppUpdateType.IMMEDIATE, activity, REQ_CODE_VERSION_UPDATE
                    )
                }
            }
        }

        onDispose {
            appUpdateManager.unregisterListener(installStateListener)
        }
    }
}
