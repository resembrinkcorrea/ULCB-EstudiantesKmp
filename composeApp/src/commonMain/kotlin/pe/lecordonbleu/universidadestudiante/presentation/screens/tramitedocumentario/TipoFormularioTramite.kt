package pe.lecordonbleu.universidadestudiante.presentation.screens.tramitedocumentario

enum class TipoFormularioTramite {
    A, B, C, D, E, F, G, H, L, DESCONOCIDO;

    companion object {
        fun desdeTipoTramite(idTipoTramite: Int?): TipoFormularioTramite =
            when (idTipoTramite) {
                in setOf(125, 126, 133, 134, 135, 136, 138, 139, 98) -> A
                in setOf(67, 107, 1) -> B
                in setOf(2, 3, 9, 13, 19, 20, 64, 69, 73, 74, 146, 148, 28, 109) -> C
                in setOf(56, 115) -> D
                in setOf(46, 87) -> E
                in setOf(63, 62, 60, 57, 10, 11, 7, 8, 47, 52, 53, 41, 44, 66, 68, 106, 70, 71, 110, 111, 112, 113, 114, 116, 117, 72, 118, 77, 120, 78, 121, 79, 122, 80, 85) -> F
                in setOf(124, 129) -> G
                in setOf(54, 101, 128, 119, 76, 81) -> H
                in setOf(29, 51, 40, 103) -> L
                else -> DESCONOCIDO
            }
    }
}
