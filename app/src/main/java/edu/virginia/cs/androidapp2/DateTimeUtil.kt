package edu.virginia.cs.androidapp2

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateTimeUtil {
    // convert epoch ms to local date for querying API and displaying date on picker button
    fun convertToLocalDateFromMS(ms: Long): LocalDate {
        return Instant.ofEpochMilli(ms).atZone(ZoneId.of("UTC")).toLocalDate()
    }

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
    fun getCurrentEpochMs(): Long {
        return Instant.now().toEpochMilli()
    }
}