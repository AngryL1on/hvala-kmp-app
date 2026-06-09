package tech.appard.hvala.shared.feature.profile.domain

import kotlin.time.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

internal fun currentReviewIsoDate(): String {
    val date = Clock.System.todayIn(TimeZone.currentSystemDefault())
    val month = date.monthNumber.toString().padStart(2, '0')
    val day = date.dayOfMonth.toString().padStart(2, '0')
    return "${date.year}-$month-$day"
}
