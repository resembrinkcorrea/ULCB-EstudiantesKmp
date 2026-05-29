package pe.lecordonbleu.universidadestudiante.presentation.vo

import ulcbintranetkmp.composeapp.generated.resources.Res
import ulcbintranetkmp.composeapp.generated.resources.unicampusa
import ulcbintranetkmp.composeapp.generated.resources.unicampusb
import ulcbintranetkmp.composeapp.generated.resources.unicampusc
import org.jetbrains.compose.resources.DrawableResource

data class OnBoardingSlide(
    val image: DrawableResource,
    val title: String,
    val subtitle: String
)

object ImagesCarousel {
    val slides = listOf(
        OnBoardingSlide(
            image = Res.drawable.unicampusa,
            title = "EXCELENCIA ACADÉMICA",
            subtitle = "Formando líderes a nivel global."
        ),
        OnBoardingSlide(
            image = Res.drawable.unicampusb,
            title = "PORTAL INSTITUCIONAL",
            subtitle = "Toda tu información en la palma de tu mano."
        ),
        OnBoardingSlide(
            image = Res.drawable.unicampusc,
            title = "TU FUTURO EMPIEZA AQUÍ",
            subtitle = "Únete a la red de profesionales más exclusiva."
        )
    )
}
