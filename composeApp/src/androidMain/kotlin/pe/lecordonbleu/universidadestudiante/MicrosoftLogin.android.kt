package pe.lecordonbleu.universidadestudiante

import android.app.Activity
import android.content.Context
import android.util.Log
import com.microsoft.identity.client.AcquireTokenSilentParameters
import com.microsoft.identity.client.AuthenticationCallback
import com.microsoft.identity.client.IAccount
import com.microsoft.identity.client.IAuthenticationResult
import com.microsoft.identity.client.ISingleAccountPublicClientApplication
import com.microsoft.identity.client.PublicClientApplication
import com.microsoft.identity.client.exception.MsalException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

actual fun MicrosoftLogin(platformContext: Any?, listener: MicrosoftLoginListener) {

    val context = platformContext as? Context
        ?: throw IllegalArgumentException("Se requiere un Context válido en Android")

    val activity = context as? Activity
        ?: throw IllegalArgumentException("El Context debe ser una Activity en Android")

    CoroutineScope(Dispatchers.Main).launch {

        val msalApp = withContext(Dispatchers.IO) {
            PublicClientApplication.createSingleAccountPublicClientApplication(
                activity,
                R.raw.auth_config
            )
        }

        msalApp.getCurrentAccountAsync(
            object : ISingleAccountPublicClientApplication.CurrentAccountCallback {

                override fun onAccountLoaded(activeAccount: IAccount?) {

                    if (activeAccount != null) {

                        Log.d("MSAL", "Cuenta activa encontrada")

                        CoroutineScope(Dispatchers.IO).launch {

                            try {

                                val silentParameters =
                                    AcquireTokenSilentParameters.Builder()
                                        .forAccount(activeAccount)
                                        .fromAuthority(activeAccount.authority)
                                        .withScopes(listOf("User.Read"))
                                        .build()

                                val authenticationResult =
                                    msalApp.acquireTokenSilent(silentParameters)

                                val accessToken = authenticationResult.accessToken

                                val lastPasswordChangeDate =
                                    fetchLastPasswordChangeDate(accessToken)

                                withContext(Dispatchers.Main) {
                                    fetchUserInfo(
                                        accessToken,
                                        listener,
                                        lastPasswordChangeDate
                                    )
                                }

                            } catch (exception: Exception) {

                                Log.d("MSAL", "Silent token falló, iniciar login")

                                withContext(Dispatchers.Main) {
                                    iniciarSesion(msalApp, activity, listener)
                                }
                            }
                        }

                    } else {

                        Log.d("MSAL", "No existe sesión, iniciar login")

                        iniciarSesion(msalApp, activity, listener)
                    }
                }

                override fun onAccountChanged(priorAccount: IAccount?, currentAccount: IAccount?) {}

                override fun onError(exception: MsalException) {
                    listener.onError("Error al cargar cuenta: ${exception.message}")
                }
            }
        )
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

        val lastDate =
            json["lastPasswordChangeDateTime"]?.jsonPrimitive?.content ?: ""

        Log.d("MSGraph", "Último cambio contraseña: $lastDate")

        lastDate

    } catch (e: Exception) {

        Log.e("MSGraph", "Error obteniendo fecha: ${e.message}")

        ""

    } finally {
        client.close()
    }
}

private fun iniciarSesion(
    msalApp: ISingleAccountPublicClientApplication,
    activity: Activity,
    listener: MicrosoftLoginListener
) {

    val scopes = arrayOf("User.Read")

    msalApp.signIn(activity, null, scopes, object : AuthenticationCallback {

        override fun onSuccess(authenticationResult: IAuthenticationResult?) {

            authenticationResult?.let { authResult ->

                val accessToken = authResult.accessToken

                CoroutineScope(Dispatchers.Main).launch {

                    val lastPasswordChangeDate =
                        fetchLastPasswordChangeDate(accessToken)

                    fetchUserInfo(
                        accessToken,
                        listener,
                        lastPasswordChangeDate
                    )
                }
            }
        }

        override fun onError(exception: MsalException?) {
            listener.onError(exception?.message ?: "Error desconocido")
        }

        override fun onCancel() {
            listener.onError("Login cancelado")
        }
    })
}

actual fun logout(platformContext: Any?, listener: MicrosoftLoginListener) {

    val context = platformContext as? Context
        ?: throw IllegalArgumentException("Se requiere Context")

    val activity = context as? Activity
        ?: throw IllegalArgumentException("Context debe ser Activity")

    CoroutineScope(Dispatchers.Main).launch {

        val msalApp = withContext(Dispatchers.IO) {
            PublicClientApplication.createSingleAccountPublicClientApplication(
                activity,
                R.raw.auth_config
            )
        }

        msalApp.signOut(object :
            ISingleAccountPublicClientApplication.SignOutCallback {

            override fun onSignOut() {

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

            override fun onError(exception: MsalException) {
                listener.onError("Error logout: ${exception.message}")
            }
        })
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
