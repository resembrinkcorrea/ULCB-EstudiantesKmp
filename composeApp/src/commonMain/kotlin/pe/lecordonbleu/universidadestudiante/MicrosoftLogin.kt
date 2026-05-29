package pe.lecordonbleu.universidadestudiante


interface MicrosoftLoginListener {
    suspend fun onSuccess(
        emailM: String,
        displayNameM: String,
        jobTitleM: String,
        officeLocationM: String,
        mobilePhoneM: String,
        photoBytesM: ByteArray?,
        lastPasswordChangeDateM: String
    )
    fun onError(errorMessage: String)
}


expect fun MicrosoftLogin(
    platformContext: Any?,
    listener: MicrosoftLoginListener
)

expect fun logout(
    platformContext: Any?,
    listener: MicrosoftLoginListener
)
