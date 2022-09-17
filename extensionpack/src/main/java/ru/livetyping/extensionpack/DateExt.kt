package ru.livetyping.extensionpack

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

const val DATE_PATTERN_DAY_MONTH_DAY_WEEK = "dd MMMM, EEEE"

const val TIME_DAY_PATTERN = "HH:mm dd.MM.yyyy"

const val DATE_PATTERN_DATE_TIME = "dd.MM.yyyy HH:mm"

const val DATE_PATTERN_YEAR_MONTH_DAY_DASH = "yyyy-MM-dd"

const val YEAR_MONTH_DAY_DOT_PATTERN = "yyyy.MM.dd"

const val DAY_MONTH_YEAR_PATTERN = "dd.MM.yyyy"

const val DAY_MONTH_YEAR_WEEK_PATTERN = "dd.MM.yyyy EE"

const val DAY_FULL_MONTH_YEAR_PATTERN = "dd MMMM yyyy"

const val DATE_PATTERN_FULL_MONTH_YEAR_PATTERN = "MMMM yyyy"

const val DATE_PATTERN_DAY_SHORT_MONTH = "dd MMM"

const val YEAR_MONTH_DAY_PATTERN = "yyyy-MM-dd"

const val DATE_PATTERN_TIME = "HH:mm"

const val DATE_PATTERN_DAY_MONTH = "d MMMM"

fun Date.getFormatDate(pattern: String): String =
    SimpleDateFormat(pattern, Locale.getDefault()).format(this)

fun String.getDate(pattern: String): Date =
    SimpleDateFormat(pattern, Locale.getDefault()).parse(this)!!

@RequiresApi(Build.VERSION_CODES.O)
fun String.getLocalDate(pattern: String): LocalDate =
    LocalDate.parse(this, DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))


@RequiresApi(Build.VERSION_CODES.O)
fun String.getLocalDateTime(pattern: String): LocalDateTime =
    LocalDateTime.parse(this, DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))

@RequiresApi(Build.VERSION_CODES.O)
fun String.getLocalDateTimeWithTimeZone(pattern: String): LocalDateTime =
    LocalDateTime.parse(this, DateTimeFormatter.ofPattern(pattern, Locale.getDefault()))
        .plusHours(TimeUnit.MILLISECONDS.toHours(TimeZone.getDefault().rawOffset.toLong()))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.getFormatDate(pattern: String): String =
    this.format(DateTimeFormatter.ofPattern(pattern))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalTime.getFormatDate(pattern: String): String =
    this.format(DateTimeFormatter.ofPattern(pattern))

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDate.getDate(): Date = Date.from(this.atStartOfDay(ZoneId.systemDefault()).toInstant())

@RequiresApi(Build.VERSION_CODES.O)
fun Date.toLocalDate(pattern: String): LocalDate =
    SimpleDateFormat(pattern, Locale.getDefault()).format(this).getLocalDate(pattern)

@RequiresApi(Build.VERSION_CODES.O)
fun Date.toLocalDate(): LocalDate = toInstant().atZone(ZoneId.systemDefault()).toLocalDate()

@RequiresApi(Build.VERSION_CODES.O)
fun Date.toLocalDateTime(): LocalDateTime = toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()