package com.realityexpander.noteappkmm.domain.time

import kotlinx.datetime.*

object LocalDateTimeUtil {

    fun now(): LocalDateTime {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    }

    fun toEpochMillis(dateTime: LocalDateTime): Long {
        return dateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    fun fromEpochMillis(epochMillis: Long): LocalDateTime {
        return Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(TimeZone.currentSystemDefault())
    }

    // format a LocalDateTime to MM/dd/yyyy HH:mm:ss
    val LocalDateTime.formatted: String
        get() = this.toString().replace("T", " ")

    // format a LocalDateTime to MM/dd/yyyy
    val LocalDateTime.formattedDate: String
        get() = this.toString().split("T")[0]

    // format a LocalDateTime to MONTH dd, yyyy
    val LocalDateTime.formattedDateTime: String     // 2022-09-22T21:32.000
        get() = this.toString()
            .split("T")[0]               // 2022-09-22
            .replace("-", " ")    // 2022 09 22
            .split(" ")                  // [2022, 09, 22]
            .let {
                "${month.name                       // SEP
                    .take(3).lowercase()        // sep
                    .replaceFirstChar { it.uppercase() }} " + // Sep
                "${it[2]}, ${it[0]}"                // Sep 22, 2022
            } +" "+
            this.toString()
                .split("T")[1]           // 21:32.000
                .split(".")[0]           // 21:32
                .split(":")              // [21, 32]
                .let {
                    if(it[0].toInt() > 12)
                        (it[0].toInt()-12).toString()+
                            ":"+
                            it[1]+" PM"        // 9:32 PM
                    else
                        (it[0]).toString()+
                            ":"+it[1]+
                            " AM"              // 9:32 AM
                }


    // format a LocalDateTime to MONTH dd, yyyy
    // LocalDateTime is a KotlinX class looks like: 2021-07-01T00:00.000
    fun formatNoteDate(dateTime: LocalDateTime): String {
        val month =   // first 3 letters of month, 1st char capitalized
            dateTime
                .month
                .name
                .lowercase()
                .take(3)
                .replaceFirstChar { it.uppercase() }
        val day =
            if(dateTime.dayOfMonth < 10)
                "0${dateTime.dayOfMonth}"
            else
                dateTime.dayOfMonth.toString()
        val year = dateTime.year
        val hour =
            if(dateTime.hour < 10)
                "0${dateTime.hour}"
            else
                dateTime.hour.toString()
        val minute =
            if(dateTime.minute < 10)
                "0${dateTime.minute}"
            else
                dateTime.minute.toString()

        return buildString {
            append(month)
            append(" ")
            append(day)
            append(" ")
            append(year)
            append(", ")
            append(hour)
            append(":")
            append(minute)
        }
    }
}