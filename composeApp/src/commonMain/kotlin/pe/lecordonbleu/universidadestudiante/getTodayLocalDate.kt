// commonMain
package pe.lecordonbleu.universidadestudiante

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

expect fun getTodayLocalDate(): LocalDate
expect fun getTodayLocalDateTime(): LocalDateTime
expect fun currentTimeMillis(): Long
