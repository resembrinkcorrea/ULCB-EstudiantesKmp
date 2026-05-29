// androidMain
package pe.lecordonbleu.universidadestudiante

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

actual fun getTodayLocalDate(): LocalDate {
    return Clock.System
        .now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date
}

actual fun getTodayLocalDateTime(): LocalDateTime {
    return Clock.System
        .now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
}
