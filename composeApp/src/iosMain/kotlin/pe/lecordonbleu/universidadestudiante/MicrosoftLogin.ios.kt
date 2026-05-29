package pe.lecordonbleu.universidadestudiante
import cocoapods.MSAL.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.cinterop.*
import kotlinx.coroutines.*
import kotlinx.serialization.json.*
import platform.Foundation.NSError
import platform.UIKit.UIViewController

@OptIn(ExperimentalForeignApi::class)
actual fun logout(platformContext: Any?, listener: MicrosoftLoginListener) {

    val viewController = platformContext as? UIViewController
        ?: run {
            listener.onError("Se requiere un UIViewController válido para iOS")
            return
        }

    val clientId = "036df2c1-f8b5-4552-aa7b-5101a1ad1139"
    val redirectUri = "msauth.pe.lecordonbleu.universidadestudiante://auth"

    memScoped {

        val appError = alloc<ObjCObjectVar<NSError?>>()

        val config = MSALPublicClientApplicationConfig(
            clientId,
            redirectUri,
            null
        ).apply {
            cacheConfig.keychainSharingGroup = "com.microsoft.adalcache"
        }

        val msalApp = MSALPublicClientApplication(config, appError.ptr)

        appError.value?.let {
            listener.onError("Error inicializando MSAL: ${it.localizedDescription}")
            return@memScoped
        }

        val accountParams = MSALAccountEnumerationParameters()

        msalApp.getCurrentAccountWithParameters(accountParams) { account, _, error ->

            if (error != null) {
                listener.onError("Error obteniendo cuenta: ${error.localizedDescription}")
                return@getCurrentAccountWithParameters
            }

            if (account == null) {
                listener.onError("No hay cuenta activa.")
                return@getCurrentAccountWithParameters
            }

            val webviewParameters = MSALWebviewParameters(viewController)

            val signoutParameters = MSALSignoutParameters(webviewParameters).apply {
                signoutFromBrowser = false
                wipeAccount = true
            }

            msalApp.signoutWithAccount(account, signoutParameters) { success, signOutError ->

                if (signOutError != null) {

                    listener.onError("Error cerrando sesión: ${signOutError.localizedDescription}")

                } else if (success) {

                    CoroutineScope(Dispatchers.Main).launch {

                        listener.onSuccess(
                            "",
                            "",
                            "",
                            "",
                            "",
                            null,
                            ""
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
actual fun MicrosoftLogin(platformContext: Any?, listener: MicrosoftLoginListener) {

    val viewController = platformContext as? UIViewController
        ?: run {
            listener.onError("Se requiere un UIViewController válido para iOS")
            return
        }

    val clientId = "036df2c1-f8b5-4552-aa7b-5101a1ad1139"
    val redirectUri = "msauth.pe.lecordonbleu.universidadestudiante://auth"

    memScoped {

        val error = alloc<ObjCObjectVar<NSError?>>()

        val config = MSALPublicClientApplicationConfig(
            clientId,
            redirectUri,
            null
        ).apply {
            cacheConfig.keychainSharingGroup = "com.microsoft.adalcache"
        }

        val msalApp = MSALPublicClientApplication(config, error.ptr)

        error.value?.let {
            listener.onError("Error inicializando MSAL: ${it.localizedDescription}")
            return@memScoped
        }

        val accountParams = MSALAccountEnumerationParameters()

        msalApp.getCurrentAccountWithParameters(accountParams) { account, _, accountError ->

            if (accountError != null) {
                listener.onError("Error obteniendo cuenta: ${accountError.localizedDescription}")
                return@getCurrentAccountWithParameters
            }

            val scopes = listOf("User.Read")

            if (account != null) {

                val silentParameters = MSALSilentTokenParameters(
                    scopes,
                    account
                )

                msalApp.acquireTokenSilentWithParameters(silentParameters) { result, silentError ->

                    if (silentError == null && result != null) {

                        val accessToken = result.accessToken

                        CoroutineScope(Dispatchers.Main).launch {

                            val lastPassword = fetchLastPasswordChangeDate(accessToken)
                            fetchUserInfo(accessToken, listener, lastPassword)

                        }

                    } else {

                        iniciarLoginInteractivo(msalApp, viewController, listener)

                    }
                }

            } else {

                iniciarLoginInteractivo(msalApp, viewController, listener)

            }
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun iniciarLoginInteractivo(
    msalApp: MSALPublicClientApplication,
    viewController: UIViewController,
    listener: MicrosoftLoginListener
) {

    val scopes = listOf("User.Read")
    val webviewParameters = MSALWebviewParameters(viewController)

    val parameters = MSALInteractiveTokenParameters(
        scopes,
        webviewParameters
    ).apply {
        promptType = MSALPromptTypeSelectAccount
    }

    msalApp.acquireTokenWithParameters(parameters) { result, error ->

        if (error != null) {

            listener.onError("Error autenticando: ${error.localizedDescription}")
            return@acquireTokenWithParameters

        }

        val accessToken = result?.accessToken ?: return@acquireTokenWithParameters

        CoroutineScope(Dispatchers.Main).launch {

            val lastPassword = fetchLastPasswordChangeDate(accessToken)

            fetchUserInfo(
                accessToken,
                listener,
                lastPassword
            )
        }
    }
}

private suspend fun fetchLastPasswordChangeDate(accessToken: String): String {

    val client = HttpClient()

    return try {

        val responseText = client.get(
            "https://graph.microsoft.com/v1.0/me?\$select=lastPasswordChangeDateTime"
        ) {
            header("Authorization", "Bearer $accessToken")
        }.bodyAsText()

        val json = Json.parseToJsonElement(responseText).jsonObject

        json["lastPasswordChangeDateTime"]
            ?.jsonPrimitive
            ?.content ?: ""

    } catch (e: Exception) {

        ""

    } finally {

        client.close()

    }
}

private suspend fun fetchUserInfo(
    accessToken: String,
    listener: MicrosoftLoginListener,
    lastPasswordChangeDate: String
) {

    val client = HttpClient()

    try {

        val responseText =
            client.get("https://graph.microsoft.com/v1.0/me") {
                header("Authorization", "Bearer $accessToken")
            }.bodyAsText()

        val json = Json.parseToJsonElement(responseText).jsonObject

        val displayName =
            json["displayName"]?.jsonPrimitive?.content ?: ""

        val jobTitle =
            json["jobTitle"]?.jsonPrimitive?.content ?: ""

        val email =
            json["mail"]?.jsonPrimitive?.content ?: ""

        val officeLocation =
            json["officeLocation"]?.jsonPrimitive?.content ?: ""

        val mobilePhone =
            json["mobilePhone"]?.jsonPrimitive?.content ?: ""

        val photo = fetchPhoto(accessToken)

        withContext(Dispatchers.Main) {

            listener.onSuccess(
                email,
                displayName,
                jobTitle,
                officeLocation,
                mobilePhone,
                photo,
                lastPasswordChangeDate
            )
        }

    } catch (e: Exception) {

        withContext(Dispatchers.Main) {
            listener.onError("Error obteniendo usuario: ${e.message}")
        }

    } finally {

        client.close()

    }
}

private suspend fun fetchPhoto(accessToken: String): ByteArray? {

    val client = HttpClient()

    return try {

        client.get(
            "https://graph.microsoft.com/v1.0/me/photo/\$value"
        ) {
            header("Authorization", "Bearer $accessToken")
        }.body()

    } catch (e: Exception) {

        null

    } finally {

        client.close()

    }
}