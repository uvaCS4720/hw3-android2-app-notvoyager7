package edu.virginia.cs.androidapp2

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

object DateTimeUtil {
    // Google Gemini 3 Pro generated this function
    // Gets the month (MM) as a zero-padded String (e.g., "01" - "12") from epoch ms
    fun getMonthStringFromMS(ms: Long): String {
        val formatter = DateTimeFormatter.ofPattern("MM")
        return Instant.ofEpochMilli(ms)
            .atZone(ZoneId.of("UTC"))
            .format(formatter)
    }

    // Google Gemini 3 Pro generated this function
    // Gets the day (dd) as a zero-padded String (e.g., "01" - "31") from epoch ms
    fun getDayStringFromMS(ms: Long): String {
        val formatter = DateTimeFormatter.ofPattern("dd")
        return Instant.ofEpochMilli(ms)
            .atZone(ZoneId.of("UTC"))
            .format(formatter)
    }

    // Google Gemini 3 Pro generated this function
    // Gets the year (yyyy) as a 4-digit String (e.g., "2024") from epoch ms
    fun getYearStringFromMS(ms: Long): String {
        val formatter = DateTimeFormatter.ofPattern("yyyy")
        return Instant.ofEpochMilli(ms)
            .atZone(ZoneId.of("UTC"))
            .format(formatter)
    }

    // convert epoch ms to local date for querying API and displaying date on picker button
//    fun convertToLocalDateFromMS(ms: Long): LocalDate {
//        return Instant.ofEpochMilli(ms).atZone(ZoneId.of("UTC")).toLocalDate()
//    }

    // Google Gemini 3 Pro generated this function
    // convert from stored date in epoch ms to string for date picker button
    fun convertToLocalDateStringFromMS(ms: Long?): String {
        if (ms == null) return "Select Date"

        val formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy")

        return Instant.ofEpochMilli(ms)
            .atZone(ZoneId.of("UTC"))
            .format(formatter)
    }

    // Google Gemini 3 Pro generated this function
    // convert from stored start time in epoch seconds to a String in ET to display start time
    fun getETTimeStringFromSeconds(s: Long): String {
        val formatter = DateTimeFormatter.ofPattern("h:mm a 'ET'")
            .withZone(ZoneId.of("America/New_York"))

        return formatter.format(Instant.ofEpochSecond(s))
    }

    // Google Gemini 3 Pro generated this function
    // This gets the current time in epoch ms for the date picker to start out with
    // I updated this to get the time at midnight UTC to ensure standardization
    fun getCurrentEpochMs(): Long {
        return LocalDate.now() // 1. Get current date in the user's local timezone
            .atStartOfDay(ZoneOffset.UTC) // 2. Force it to midnight in UTC
            .toInstant() // 3. Convert to Instant
            .toEpochMilli() // 4. Get the raw milliseconds
    }
}