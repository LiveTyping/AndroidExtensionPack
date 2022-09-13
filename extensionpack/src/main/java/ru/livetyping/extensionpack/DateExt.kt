package ru.livetyping.extensionpack

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.*
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.TimeUnit

const val DATE_PATTERN_DAY_MONTH_DAY_WEEK = "dd MMMM, EEEE"

const val DATE_PATTERN_DATE_TIME_SECONDS_GMT = "yyyy-MM-dd'T'HH:mm:ss"

const val DATE_PATTERN_DATE_TIME_SECONDS_TIMEZONE_GMT = "yyyy-MM-dd'T'HH:mm:ssZ"

const val DATE_PATTERN_DATE_TIME_MILLS_TIME_ZONE = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX"

const val DATE_PATTERN_DATE_TIME_MILLS_GMT = "yyyy-MM-dd'T'HH:mm:ss.SSS ZZZZ"

const val DATE_PATTERN_DATE_TIME_SHORT_GMT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"

const val DATE_PATTERN_DATE_TIME_MILLS = "yyyy-MM-dd'T'HH:mm:ss.SSS"

const val DATE_PATTERN_DATE_WEEK_TIME = "dd.MM.yyyy, EEEE, HH:mm"

const val TIME_DAY_PATTERN = "HH:mm dd.MM.yyyy"

const val DATE_PATTERN_DATE_SHORTWEEK_TIME = "dd.MM.yyyy, EE, HH:mm"

const val DATE_PATTERN_DATE_TIME = "dd.MM.yyyy HH:mm"

const val DATE_PATTERN_DATE_TIME_SECONDS = "dd.MM.yyyy HH:mm:ss"

const val DATE_PATTERN_YEAR_DATE_TIME_SECONDS = "yyyy.MM.dd HH:mm:ss"

const val DATE_PATTERN_YEAR_DASH_DATE_TIME_SECONDS = "yyyy-MM-dd HH:mm:ss"

const val DATE_PATTERN_YEAR_MONTH_DAY_DASH = "yyyy-MM-dd"

const val YEAR_MONTH_DAY_DOT_PATTERN = "yyyy.MM.dd"

const val DAY_MONTH_YEAR_PATTERN = "dd.MM.yyyy"

const val DAY_MONTH_YEAR_WEEK_PATTERN = "dd.MM.yyyy EE"

const val DAY_FULL_MONTH_YEAR_PATTERN = "dd MMMM yyyy"

const val DATE_PATTERN_DAY_FULL_MONTH_YEAR_HOUR_MINUTES = "dd MMMM yyyy HH:mm"

const val DATE_PATTERN_DAY_CUT_MONTH_YEAR_HOUR_MINUTES = "dd MMM yyyy, HH:mm"

const val DATE_PATTERN_FULL_MONTH_YEAR_PATTERN = "MMMM yyyy"

const val DATE_PATTERN_DAY_SHORT_MONTH = "dd MMM"

const val DATE_PATTERN_MONTH_SHORT = "LLL"

const val DATE_PATTERN_MONTH_FULL = "LLLL"

const val DATE_PATTERN_MONTH_YEAR_PATTERN = "LLLL yyyy"

const val DAY_FULL_MONTH_WEEKDAY_PATTERN = "dd MMMM (EE)"

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


@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.getFormatDate(pattern: String): String =
    this.format(DateTimeFormatter.ofPattern(pattern))


@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.getFormatDateWithTimeZone(pattern: String): String =
    this.format(DateTimeFormatter.ofPattern(pattern)) + " ${
        TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT)
    }"

@RequiresApi(Build.VERSION_CODES.O)
fun Long.toLocalDate() = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()


@RequiresApi(Build.VERSION_CODES.O)
fun Long.toLocalDateTime() = Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()

@RequiresApi(Build.VERSION_CODES.O)
fun LocalDateTime.toMillis() = this.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
