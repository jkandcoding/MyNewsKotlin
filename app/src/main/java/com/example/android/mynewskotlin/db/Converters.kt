package com.example.android.mynewskotlin.db

import androidx.room.TypeConverter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter


class Converters {

    companion object {
        private val formatter: DateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME

        @TypeConverter
        @JvmStatic
        fun fromStringToOffsetDateTime(value: String): OffsetDateTime {
            return OffsetDateTime.parse(value)
        }

        @TypeConverter
        @JvmStatic
        fun fromOffsetDateTimeToString(value: OffsetDateTime): String {
            return value.format(formatter)
        }
    }
}