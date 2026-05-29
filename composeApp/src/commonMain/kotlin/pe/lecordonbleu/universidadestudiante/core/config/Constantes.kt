package pe.lecordonbleu.universidadestudiante.core.config

enum class Entorno {
    DESARROLLO,
    PREPRODUCCION,
    PRODUCCION
}

private data class ConfigEntorno(
    val baseRootIntranet: String,
    val baseDomain: String,
    val basePdfFlywire: String,
    val urlPdfFlywirePecano: String,
    val envDomain: String,
    val returnDomain: String,
    val returnTramite: String
)

object Constantes {

    private val entornoActual = Entorno.DESARROLLO

    private val config = when (entornoActual) {
        Entorno.DESARROLLO -> ConfigEntorno(
            baseRootIntranet = "http://74.249.92.43:8080",
            baseDomain = "https://devalumno",
            basePdfFlywire = "https://predataread.cordonbleu.edu.pe:8081/verboletas/boleta/",
            urlPdfFlywirePecano = "https://devintegracion.cordonbleu.edu.pe/resources/boleta/",
            envDomain = "demo",
            returnDomain = "https://devecommerce",
            returnTramite = "https://devalumno"
        )
        Entorno.PREPRODUCCION -> ConfigEntorno(
            baseRootIntranet = "http://sslcbpsaa.eastus2.cloudapp.azure.com:8080",
            baseDomain = "https://prealumno",
            basePdfFlywire = "https://predataread.cordonbleu.edu.pe:8081/verboletas/boleta/",
            urlPdfFlywirePecano = "https://preintegracion.cordonbleu.edu.pe/resources/boleta/",
            envDomain = "demo",
            returnDomain = "https://preecommerce",
            returnTramite = "https://prealumno"
        )
        Entorno.PRODUCCION -> ConfigEntorno(
            baseRootIntranet = "http://sslcbpopen.eastus2.cloudapp.azure.com:8080",
            baseDomain = "https://alumno",
            basePdfFlywire = "https://dataread.cordonbleu.edu.pe:8082/verboletas/boleta/",
            urlPdfFlywirePecano = "https://integracion.cordonbleu.edu.pe/resources/boleta/",
            envDomain = "production",
            returnDomain = "https://ecommerce",
            returnTramite = "https://alumno"
        )
    }

    val SUCCESS = 200
    val AUTH = "Bearer "
    val AUTH_PHOTO = "https://graph.microsoft.com/v1.0/me/photo/"

    val BASE_ROOT_INTRANET = config.baseRootIntranet
    val URL_BASE_INTRANET = "/saa-rest/webresources/intranetSAA/"
    val URL_BASE_GENERAL = "/saa-rest/webresources/"

    val BASE_DOMAIN = config.baseDomain
    val BASE_PDF_FLYWIRE = config.basePdfFlywire
    val URL_PDF_FLYWIREPECANO = config.urlPdfFlywirePecano

    val BASE_FICHA_MTR = "https://alumno.ulcb.edu.pe/"

    //val RECIPIENT_DOMAIN = "ILP"
    var RECIPIENT_DOMAIN = "LEP" //Universidad

    val ID_SISTEMA = 23 //Universidad 23 - Instituto 24 - Android
    val BASE_UNEG = "ulcb"

    val ENV_DOMAIN = config.envDomain  //cuenta corriente

    val RETURN_DOMAIN = config.returnDomain
    val RETURN_TRAMITE = config.returnTramite
}
